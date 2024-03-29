package unibs.pajc.uno.controller.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.CardView;
import unibs.pajc.uno.view.DialogSelectNewColor;
import unibs.pajc.uno.view.TableView;
import unibs.pajc.uno.view.events.CardDrawnEvent;
import unibs.pajc.uno.view.events.CardSelectedEvent;

/**
 * Classe per la gestione di un client
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
public class NetServer
{
	// MODEL AND VIEW
	private GameModel model;
	private final TableView view;

	// NETWORK ATTRIBUTES
	private Socket client;
	private ServerSocket serverSocket;
	private final int PORT;
	private boolean isConnected = false;
	private Object syncServerConnection = new Object();

	// OUTPUT / INPUT STREAMS
	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;
	private Object objReceivedGame = null;

	private String playerNameServer = null;
	private String playerNameClient = null;

	private CardSelectedEvent mouseListener;
	private final CardDrawnEvent mouseListenerDrawnCard;

	// MULTIPLE CLIENTS
	private final int playerIndex = 0;

	private final Object syncCardSelected = new Object();
	private final Object syncObjectModel = new Object();
	private final Object syncObjectChat = new Object();

	public NetServer(int PORT, String playerNameServer, int numberOfPlayers)
	{
		this.PORT = PORT;
		this.playerNameServer = playerNameServer;

		startServer();

		view = new TableView(playerNameServer, playerNameClient, false, syncObjectChat);
		view.setVisible(true);
		view.setResizable(false);
		view.setTitle(playerNameServer);

		mouseListenerDrawnCard = new CardDrawnEvent(syncCardSelected);
		view.getCardDeckView().addMouseListener(mouseListenerDrawnCard);

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		executor.execute(this::runGameLogic);
		executor.execute(this::listenForNewMessagesToSend);
		executor.execute(this::listenToClient);
	}

	/**
	 * 
	 * @param server
	 * @param client
	 */
	public void updateView()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				Player player = model.getPlayers().get(playerIndex);

				view.setMiddleCardClickable(model.getCurrentPlayerIndex() == 0);

				// SETS UNIO BUTTON
				view.setSayUnoButtonVisibile(model.hasPlayerOneCard() && model.getCurrentPlayerIndex() == 0, 0);
				view.setUnoButtonPressed(false);

				// SETS LABELS
				view.setPanelTitles(player.getNamePlayer(), model.getPlayers().get(1).getNamePlayer());
				view.setTurn(model.getCurrentPlayer().getNamePlayer());

				// LOADS CARDS (Player and adversary)
				view.loadCards(player.getHandCards(), 0);
				view.loadCardsAdversary(model.getPlayers().get(1).getHandCards().getNumberOfCards());

				// CHANGES THE LAST CARD USED
				view.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());

				// ADDS ACTION LISTENERS
				ArrayList<CardView> panelPlayerOneCards = view.getAllCards(0, player.getHandCards().getNumberOfCards());

				mouseListener = new CardSelectedEvent(syncCardSelected);
				panelPlayerOneCards.forEach(e -> e.addMouseListener(mouseListener));

				// ENABLES / DISABLES CARDS
				view.enableViewPlayer(0, model.getCurrentPlayerIndex() == 0);

				view.repaint();
			}
		});
	}

	/**
	 * 
	 */
	private void runGameLogic()
	{
		model = new GameModel();

		Player server = new Player(playerNameServer, model.generateStartingCards(), 0);
		Player client = new Player(playerNameClient, model.generateStartingCards(), 1);

		model.initPlayers(new ArrayList<Player>(Arrays.asList(server, client)));

		// SENDING MODEL TO CLIENT
		sendToClient(model);
		updateView();

		while (!model.isGameOver())
		{
			updateView();

			if (model.getCurrentPlayerIndex() == 0)
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

				sendToClient(model);
			}
			else if (model.getCurrentPlayerIndex() != 0)
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

				model = updatedModel;
				updatedModel = null;
				objReceivedGame = null;
			}
		}

		if (model.getWinnerPlayer().getIndex() == 0)
		{
			JOptionPane.showMessageDialog(null, model.getWinnerPlayer().getNamePlayer()
					+ " vincitore! Congratulazioni! Non hai vinto assolutamente nulla, se non un briciolo di misera gloria!");
		}
		else
		{
			JOptionPane.showMessageDialog(null,
					model.getWinnerPlayer().getNamePlayer() + " hai perso! L'importante è partecipare?");
		}

		try
		{
			serverSocket.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(0);
	}

	private void turnGame()
	{
		if (mouseListener.getCardSelected() != null)
		{
			System.out.println("[SERVER] - Card selected");
			manageCardSelected();
		}
		if (mouseListenerDrawnCard.isCardDrawn() && model.getCurrentPlayer().getHandCards().getNumberOfCards() < 30)
		{
			System.out.println("[SERVER] - Card card drawn");
			manageCardDrawn();
		}
		else if (mouseListenerDrawnCard.isCardDrawn()
				&& model.getCurrentPlayer().getHandCards().getNumberOfCards() >= 30)
		{
			JOptionPane.showMessageDialog(view, "Hai già troppe carte!");
		}
	}

	private void manageCardDrawn()
	{
		model.getCurrentPlayer().addCard(model.getCardFromDeck());
		mouseListenerDrawnCard.setCardDrawn(false);
		model.nextTurn();
	}

	/**
	 * 
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

		mouseListener.setCardSelectedNull();
	}

	private void playerSaidUno()
	{
		if (model.hasPlayerOneCard(model.getCurrentPlayer()) && view.isUnoButtonPressed() == false)
		{
			JOptionPane.showMessageDialog(view, "You didn't say UNO! Two more cards for you.");
			model.playerDidNotSayUno(model.getCurrentPlayerIndex());
		}
	}

	/**
	 * Initializes the server
	 */
	private void startServer()
	{
		try
		{
			serverSocket = new ServerSocket(PORT);
			System.out.println("[SERVER] - Trying to launch server");

			serverSocket.setSoTimeout(NetUtils.DEFAULT_SERVER_TIME_OUT);

			client = serverSocket.accept();
			objOutputStream = new ObjectOutputStream(client.getOutputStream());
			objInputStream = new ObjectInputStream(client.getInputStream());

			isConnected = true;

			System.out.println("[SERVER] - CONNECTED TO CLIENT: " + isConnected);

			if (isConnected)
			{
				try
				{
					// TRIES TO READ CLIENT NAME
					playerNameClient = (String) objInputStream.readObject();

					// WAITS FOR A VALID CLIENT NAME
					while (playerNameClient.equals(""))
					{
						playerNameClient = (String) objInputStream.readObject();
					}
				}
				catch (NullPointerException e)
				{
					System.out.println("[SERVER] - No player init message received");
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (SocketTimeoutException e)
		{
			System.err.println("Communication time-out: " + e);
			System.exit(0);
		}
		catch (IOException e)
		{
			System.err.println("Some communication error happened: " + e);
			System.exit(0);
		}
	}

	/**
	 * Method that starts listening to the client
	 */
	private void listenToClient()
	{
		while (true)
		{
			Object objReceived = null;

			try
			{
				objReceived = objInputStream.readObject();

				if (objReceived != null && objReceived instanceof String && ((String) objReceived).length() > 0)
				{
					System.out.println("[SERVER] - Message received from client: " + objReceived);

					view.addChatMessage((String) objReceived, playerNameClient);
				}
				if (objReceived != null && objReceived instanceof GameModel)
				{
					synchronized (syncObjectModel)
					{
						syncObjectModel.notify();
					}

					objReceivedGame = objReceived;
					System.out.println("[SERVER] - Game model received");
				}
			}
			catch (EOFException e)
			{
				JOptionPane.showMessageDialog(view, "Network error!");
				System.exit(0);
			}
			catch (IOException e)
			{
				System.out.println("Errors in listening to the client");
				System.exit(0);
			}
			catch (ClassNotFoundException e)
			{
				System.out.print("Class not found");
				System.exit(0);
			}
		}
	}

	/**
	 * Listens for possible new messages to send to the client
	 */
	private void listenForNewMessagesToSend()
	{
		while (true)
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

				sendToClient(view.getMessage());
			}
		}
	}

	/**
	 * Sends an object to the client
	 * 
	 * @param objToSend the object to send to the client
	 */
	private synchronized void sendToClient(Object objToSend)
	{
		try
		{
			objOutputStream.reset();
			objOutputStream.writeObject(objToSend);

			if (objToSend instanceof String)
			{
				System.out.println("Message sent: " + objToSend);
			}

			objOutputStream.flush();
		}
		catch (IOException e)
		{
			System.out.println("Error while sending - Couldn't send object to client");
			e.printStackTrace();
		}
	}

	/**
	 * Returns if the server connected to the server
	 * 
	 * @return if the server is connected
	 */
	public boolean isConnected()
	{
		return isConnected;
	}

	/**
	 * Returns the name of the client, received from the client
	 * 
	 * @return the String name of the client
	 */
	public String getClientName()
	{
		return playerNameClient;
	}

	/**
	 * Returns the model
	 * 
	 * @return game's model
	 */
	public GameModel getModel()
	{
		return model;
	}

	/**
	 * Sets the model of the game
	 * 
	 * @param model the game
	 */
	public void setModel(GameModel model)
	{
		this.model = model;
	}
}