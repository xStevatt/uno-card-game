package unibs.pajc.uno.controller.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.view.CardView;
import unibs.pajc.uno.view.DialogSelectNewColor;
import unibs.pajc.uno.view.TableView;
import unibs.pajc.uno.view.events.CardDrawnEvent;
import unibs.pajc.uno.view.events.CardSelectedEvent;

public class NetClient
{
	private final String IP_ADDRESS;
	private final int port;
	private boolean isConnected = false;

	private Socket clientSocket;
	private ObjectInputStream objInputStream = null;
	private ObjectOutputStream objOutputStream = null;
	private Object objReceivedGame = null;

	private String playerNameClient;
	private String playerNameServer;

	private final TableView view;
	private GameModel model;

	private CardSelectedEvent mouseListener;
	private final CardDrawnEvent mouseListenerDrawnCard;

	private ExecutorService executor;

	private final Object syncCardSelected = new Object();
	private final Object syncObjectModel = new Object();
	private final Object syncObjectChat = new Object();

	public NetClient(String IP_ADDRESS, int port, String playerName)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.port = port;
		this.playerNameClient = playerName;

		startClient();

		view = new TableView(null, null, false, syncObjectChat);
		view.setTitle(playerNameClient);

		mouseListenerDrawnCard = new CardDrawnEvent(syncCardSelected);
		view.getCardDeckView().addMouseListener(mouseListenerDrawnCard);

		executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		executor.execute(this::listenForNewMessagesToSend);
		executor.execute(this::listenToServer);
		executor.execute(this::runGameLogic);
	}

	/**
	 * Inizializza la view del gioco
	 */
	private void initView()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				view.setVisible(true);
				view.setResizable(false);

				view.setMatchLabel(model.getPlayers().get(1).getNamePlayer(),
						model.getPlayers().get(0).getNamePlayer());

				view.setTitle(model.getPlayers().get(1).getNamePlayer());

				updateView();
			}
		});
	}

	/**
	 * Aggiorna la view del gioco con i dati correnti di gioco
	 */
	private void updateView()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				// CHECKS IF DECK CARD SHOULD BE ENABLED / CHECKS IF "SAY UNO" BUTTON IS VISIBLE
				view.setMiddleCardClickable(model.getCurrentPlayerIndex() == 1);
				view.setSayUnoButtonVisibile(model.hasPlayerOneCard() && model.getCurrentPlayerIndex() == 1, 0);
				view.setUnoButtonPressed(false);

				// SETS LABELS
				view.setPanelTitles(model.getPlayers().get(1).getNamePlayer(),
						model.getPlayers().get(0).getNamePlayer());
				view.setTurn(model.getCurrentPlayer().getNamePlayer());

				// LOADS CARDS (Player and adversary)
				view.loadCards(model.getPlayers().get(1).getHandCards(), 0);
				view.loadCardsAdversary(model.getPlayers().get(0).getHandCards().getNumberOfCards());

				// CHANGES THE LAST CARD USED
				view.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());

				// ADDS ACTION LISTENERS
				ArrayList<CardView> panelPlayerOneCards = view.getAllCards(0,
						model.getPlayers().get(0).getHandCards().getNumberOfCards());

				mouseListener = new CardSelectedEvent(syncCardSelected);
				panelPlayerOneCards.forEach(e -> e.addMouseListener(mouseListener));

				// ENABLES / DISABLES CARDS
				view.enableViewPlayer(0, model.getCurrentPlayerIndex() == 1);

				// REPAINTS VIEW
				view.repaint();
			}
		});
	}

	/**
	 * Gestisce le meccaniche di gioco
	 */
	private void runGameLogic()
	{
		synchronized (syncObjectModel)
		{
			try
			{
				syncObjectModel.wait();
				this.model = waitForServer();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		this.playerNameServer = model.getPlayers().get(0).getNamePlayer();
		this.playerNameClient = model.getPlayers().get(1).getNamePlayer();

		initView();

		while (!model.isGameOver())
		{
			updateView();

			if (model.getCurrentPlayerIndex() == 0)
			{
				GameModel updatedModel = null;

				synchronized (syncObjectModel)
				{
					try
					{
						syncObjectModel.wait();
						updatedModel = (GameModel) objReceivedGame;
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}

				this.model = updatedModel;
				updatedModel = null;
				objReceivedGame = null;
			}
			else if (model.getCurrentPlayerIndex() != 0)
			{
				synchronized (syncCardSelected)
				{
					try
					{
						syncCardSelected.wait();
						playerSaidUno();
						turnGame();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				sendToServer(model);
			}
		}

		gameOver();
	}

	/**
	 * Gestisce la fine del gioco
	 */
	public void gameOver()
	{
		if (model.getWinnerPlayer().getIndex() == 1)
		{
			JOptionPane.showMessageDialog(null, model.getWinnerPlayer().getNamePlayer()
					+ " vincitore! Congratulazioni! Non hai vinto assolutamente nulla, se non un briciolo di misera gloria!");
		}
		else
		{
			JOptionPane.showMessageDialog(null,
					model.getWinnerPlayer().getNamePlayer() + " hai perso! L'importante è partecipare?");
		}

		// CHIUDE IL SOCKET
		try
		{
			clientSocket.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.exit(0);
	}

	/**
	 * Gestisce il turno di gioco
	 */
	private void turnGame()
	{
		if (mouseListener.getCardSelected() != null)
		{
			System.out.println("[CLIENT] - Card selected");
			manageCardSelected();
		}
		if (mouseListenerDrawnCard.isCardDrawn() && model.getCurrentPlayer().getHandCards().getNumberOfCards() < 30)
		{
			System.out.println("[CLIENT] - Card drawn");
			manageCardDrawn();
		}
		else if (mouseListenerDrawnCard.isCardDrawn()
				&& model.getCurrentPlayer().getHandCards().getNumberOfCards() == 30)
		{
			JOptionPane.showMessageDialog(view, "Hai già troppe carte!");
		}
	}

	/**
	 * Gestisce l'evento di carta pescata dal mazzo del tavolo, richiamando il model
	 */
	private void manageCardDrawn()
	{
		model.getCurrentPlayer().addCard(model.getCardFromDeck());
		mouseListenerDrawnCard.setCardDrawn(false);
		model.nextTurn();
	}

	/**
	 * Gestisce l'evento di carta selezionata dal proprio mazzo, richiamando il
	 * model
	 */
	private void manageCardSelected()
	{
		if (model.isPlacedCardValid(mouseListener.getCardSelected()))
		{
			view.changeDroppedCardView(mouseListener.getCardSelected(), model.getCurrentCardColor());
			boolean newColorSelection = model.evalMossa(mouseListener.getCardSelected());

			if (newColorSelection)
			{
				boolean colorSelectionValid = false;

				while (colorSelectionValid == false)
				{
					try
					{
						DialogSelectNewColor dialogColor = new DialogSelectNewColor(view);
						CardColor cardColor = dialogColor.show();
						model.setCurrentCardColor(cardColor);
						colorSelectionValid = true;
					}
					catch (NullPointerException e)
					{
						colorSelectionValid = false;
					}
				}
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please select a valid card!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Controlla se il giocatore abbia detto uno o meno
	 */
	private void playerSaidUno()
	{
		if (model.hasPlayerOneCard(model.getCurrentPlayer()) && view.isUnoButtonPressed() == false)
		{
			JOptionPane.showMessageDialog(view, "You didn't say UNO! Two more cards for you.");
			model.playerDidNotSayUno(model.getCurrentPlayerIndex());
		}
	}

	/**
	 * Aspetta che il server gli mandi un oggetto di tipo GameModel in modo tale che
	 * possa aggiornare le sue informazioni
	 * 
	 * @return il GameModel che è stato ricevuto dal server
	 */
	private GameModel waitForServer()
	{
		if (objReceivedGame != null && objReceivedGame instanceof GameModel)
		{
			System.out.println(((GameModel) objReceivedGame).getPlayers().get(0).getHandCards().getNumberOfCards());

			return ((GameModel) objReceivedGame);
		}

		return null;
	}

	/**
	 * Gestisce la connessione tra Client e Server
	 */
	private void startClient()
	{
		try
		{
			InetAddress ip = InetAddress.getByName(IP_ADDRESS);

			clientSocket = new Socket(ip, port);

			System.out.println("[CLIENT] - Trying to connect to server");

			// STREAMS
			InputStream inputStream = clientSocket.getInputStream();
			OutputStream outputStream = clientSocket.getOutputStream();

			// OBJ INPUT STREAMS
			objInputStream = new ObjectInputStream(inputStream);
			objOutputStream = new ObjectOutputStream(outputStream);

			isConnected = true;

			if (isConnected)
			{
				System.out.println("[CLIENT] - CONNECTED TO SERVER: " + isConnected);

				try
				{
					System.out.println("[CLIENT] - Trying to send data");
					objOutputStream.writeObject(playerNameClient);
				}
				catch (IOException e)
				{
					System.out.println("Error while getting init details");
					e.printStackTrace();
				}
			}
		}
		catch (UnknownHostException e)
		{
			System.err.println("IP address of the host could not be determined : " + e);
		}
		catch (IOException e)
		{
			System.err.println("Error in creating socket: " + e);
		}
	}

	/**
	 * Ascolta il server per eventuali oggetti ricevuti. In caso positivo, notifica
	 * i Thread che sono in attesa
	 */
	private void listenToServer()
	{
		while (isConnected)
		{
			Object objReceived = null;

			try
			{
				objReceived = objInputStream.readObject();

				if (objReceived != null && objReceived instanceof String && ((String) objReceived).length() > 0)
				{
					System.out.println("[CLIENT] - Message received from server: " + objReceived);

					System.out.println("Server name: " + playerNameServer);
					view.addChatMessage((String) objReceived, playerNameServer);
				}
				if (objReceived != null && objReceived instanceof GameModel)
				{
					synchronized (syncObjectModel)
					{
						syncObjectModel.notify();
					}

					objReceivedGame = objReceived;
				}
			}
			catch (EOFException e)
			{
				JOptionPane.showMessageDialog(null, "Server disconnected");
				System.exit(0);
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "Server disconnected");
				System.exit(0);
			}
			catch (ClassNotFoundException e)
			{
				JOptionPane.showMessageDialog(null, "Server disconnected");
				System.exit(0);
			}
		}
	}

	/**
	 * Controlla se l'utente vuole mandare messaggi in chat al server
	 */
	private void listenForNewMessagesToSend()
	{
		while (isConnected)
		{
			synchronized (syncObjectChat)
			{
				try
				{
					syncObjectChat.wait();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			sendToServer(view.getMessage());
		}
	}

	/**
	 * Manda dati al server
	 * 
	 * @param l'oggetto da mandare al server
	 */
	private void sendToServer(Object objToSend)
	{
		try
		{
			objOutputStream.reset();
			objOutputStream.writeObject(objToSend);
			objOutputStream.flush();
		}
		catch (IOException e)
		{
			System.out.println("Error while sending - Couldn't send object to client");
			e.printStackTrace();
		}
	}

	/**
	 * Ritorna se il client è connesso al server
	 * 
	 * @return valore booleano per verificare se il client e ancora connesso al
	 *         server
	 */
	public boolean isConnected()
	{
		return isConnected;
	}

	/**
	 * Returns the name of the server, received from the server
	 * 
	 * @return the String name of the client
	 */
	public String getServerName()
	{
		return playerNameServer;
	}
}
