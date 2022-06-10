package unibs.pajc.uno.controller.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.CardBackView;
import unibs.pajc.uno.view.CardView;
import unibs.pajc.uno.view.DialogSelectNewColor;
import unibs.pajc.uno.view.TableView;

public class NetClientOld
{
	private final String IP_ADDRESS;
	private final int port;
	private boolean isConnected = false;

	private volatile ObjectInputStream objInputStream;
	private volatile ObjectOutputStream objOutputStream;
	private volatile Object objReceivedGame = null;

	private String playerNameClient;
	private String playerNameServer;

	private TableView view;
	private GameModel model;

	private Object syncObject = new Object();

	public NetClientOld(String IP_ADDRESS, int port, String playerName)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.port = port;
		this.playerNameClient = playerName;

		startClient();
		System.out.println("[CLIENT] - starting");
		view = new TableView(null, null, false);
		view.setTitle(playerNameClient);

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		executor.execute(this::listenForNewMessagesToSend);
		executor.execute(this::listenToServer);
		executor.execute(this::runGameLogic);
	}

	public void initView()
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
				view.repaint();
			}
		});
	}

	public void updateView(Player server, Player client)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				view.setTurn(model.getCurrentPlayer().getNamePlayer());

				view.loadCards(client.getHandCards(), 0);
				view.addCardsToViewBack(server.getHandCards().getNumberOfCards());
				view.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());

				view.repaint();
			}
		});
	}

	public void changeTurnView(int playingPlayer)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				if (playingPlayer == 0)
				{
					view.enableViewPlayer(0, false);
				}
				if (playingPlayer == 1)
				{
					view.enableViewPlayer(0, true);
				}
			}
		});
	}

	public void runGameLogic()
	{
		synchronized (syncObject)
		{
			try
			{
				syncObject.wait();
				System.out.println("Notified");
			}
			catch (Exception e)
			{

			}
		}

		while (Objects.isNull(this.model))
		{
			try
			{
				this.model = waitForServer();
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			System.out.println("[CLIENT] - waiting for server");
		}

		// UNA VOLTA CHE RICEVE IL MODEL INIZIALIZZA LA GRAFICA
		initView();
		this.playerNameServer = model.getPlayers().get(0).getNamePlayer();
		this.playerNameClient = model.getPlayers().get(1).getNamePlayer();

		updateView(model.getPlayers().get(0), model.getPlayers().get(1));
		changeTurnView(0);

		while (!model.isGameOver())
		{
			if (model.getCurrentPlayerIndex() == 0)
			{
				GameModel updatedModel = null;
				changeTurnView(0);

				synchronized (syncObject)
				{
					try
					{
						syncObject.wait();
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

				updateView(model.getPlayers().get(0), model.getPlayers().get(1));
			}
			else if (model.getCurrentPlayerIndex() == 1)
			{
				changeTurnView(1);
				checkPlayerSaidUno();

				while (CardView.isCardSelected == false && CardBackView.isCardDrawnFromDeck == false
						&& model.getCurrentPlayerIndex() == 1)
				{
					turnGame();
				}

				updateView(model.getPlayers().get(0), model.getPlayers().get(1));

				System.out.println("Turn: " + model.getCurrentPlayerIndex());

				sendToServer(model);
			}
		}
	}

	public void turnGame()
	{
		if (CardView.isCardSelected == true)
		{
			System.out.println("[CLIENT] - Card selected");
			manageCardSelected();
		}
		if (CardBackView.isCardDrawnFromDeck == true && model.getCurrentPlayer().getHandCards().getNumberOfCards() < 30)
		{
			System.out.println("[CLIENT] - Card drawn");
			manageCardDrawn();
		}
		else if (CardBackView.isCardDrawnFromDeck == true
				&& model.getCurrentPlayer().getHandCards().getNumberOfCards() == 30)
		{
			JOptionPane.showMessageDialog(view, "Hai già troppe carte!");
		}

		CardView.isCardSelected = false;
		CardBackView.isCardDrawnFromDeck = false;
	}

	private void manageCardDrawn()
	{
		if (model.hasPlayerOneCard() && view.isUnoButtonPressed())
		{
			JOptionPane.showMessageDialog(view, "You didn't say UNO! Two more cards for you.");
			model.playerDidNotSayUno(model.getCurrentPlayerIndex());
		}

		model.getCurrentPlayer().addCard(model.getCardFromDeck());
		model.nextTurn();
	}

	private void manageCardSelected()
	{
		if (model.hasPlayerOneCard() && !view.isUnoButtonPressed())
		{
			JOptionPane.showMessageDialog(view, "You didn't say UNO! Two more cards for you.");
			model.playerDidNotSayUno(model.getCurrentPlayerIndex());
		}

		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		if (model.isPlacedCardValid(CardView.cardSelected))
		{
			view.changeDroppedCardView(CardView.cardSelected, model.getCurrentCardColor());
			boolean newColorSelection = model.evalMossa(CardView.cardSelected);

			if (newColorSelection)
			{
				DialogSelectNewColor dialogColor = new DialogSelectNewColor();
				CardColor cardColor = dialogColor.show();
				model.setCurrentCardColor(cardColor);
			}

			updateView(model.getPlayers().get(0), model.getPlayers().get(1));
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please select a valid card!", "Error", JOptionPane.ERROR_MESSAGE);
			CardView.isCardSelected = false;
		}

		CardView.isCardSelected = false;
	}

	public GameModel waitForServer()
	{
		if (objReceivedGame != null && objReceivedGame instanceof GameModel)
		{
			System.out.println(((GameModel) objReceivedGame).getPlayers().get(0).getHandCards().getNumberOfCards());

			return ((GameModel) objReceivedGame);
		}

		return null;
	}

	public void checkPlayerSaidUno()
	{
		if (model.hasPlayerOneCard(model.getCurrentPlayer()))
		{
			view.setSayUnoButtonVisibile(true, model.getCurrentPlayerIndex());
		}
	}

	private void startClient()
	{
		try
		{
			Socket clientSocket = new Socket(IP_ADDRESS, port);

			System.out.println("[CLIENT] - Trying to connect to server");

			objInputStream = new ObjectInputStream(clientSocket.getInputStream());
			objOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

			isConnected = true;
			System.out.println("[CLIENT] - CONNECTED TO SERVER: " + isConnected);

			if (isConnected)
			{
				try
				{
					// SENDS CLIENT NAME TO SERVER
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
			System.err.println("IP address of the host could not be determined : " + e.toString());
			System.exit(0);
		}
		catch (IOException e)
		{
			System.err.println("Error in creating socket: " + e.toString());
			System.exit(0);
		}
	}

	public void listenToServerForModel()
	{
		while (true)
		{
			Object objReceivedModel = null;

			try
			{
				objReceivedModel = objInputStream.readObject();

				if (objReceivedModel != null && objReceivedModel instanceof String)
				{
					System.out.println("[CLIENT] - Message received from server: " + ((String) objReceivedModel));

					System.out.println("Server name: " + playerNameServer);
					view.addChatMessage((String) objReceivedModel, playerNameServer);

					Thread.sleep(1000);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Makes the server listen to the client
	 */
	private void listenToServer()
	{
		while (true)
		{
			Object objReceived = null;

			try
			{
				objReceived = objInputStream.readObject();

				if (objReceived != null && objReceived instanceof String && ((String) objReceived).length() > 0)
				{
					System.out.println("[CLIENT] - Message received from server: " + ((String) objReceived));

					System.out.println("Server name: " + playerNameServer);
					view.addChatMessage((String) objReceived, playerNameServer);

					Thread.sleep(1000);
				}
				if (objReceived != null && objReceived instanceof GameModel)
				{
					synchronized (syncObject)
					{
						syncObject.notify();
					}

					objReceivedGame = objReceived;
				}
			}
			catch (EOFException e)
			{
				JOptionPane.showMessageDialog(null, "Something went wrong");
				System.exit(0);
			}
			catch (IOException e)
			{
				System.out.println("Errors in listening to the server");
				System.exit(0);

			}
			catch (ClassNotFoundException e)
			{
				System.out.print("Class not found");
				System.exit(0);
			}
			catch (InterruptedException e)
			{
				System.out.println("Interrupted execution");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Listens for new messages to send
	 */
	public void listenForNewMessagesToSend()
	{
		while (true)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (TableView.message.equals("") == false)
			{
				sendToServer(TableView.message);

				// COOLDOWN - DEFAULT 700ms
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				// RESETS LAST MESSAGE SENT
				TableView.message = "";
			}
		}
	}

	/**
	 * Sends data to the server.
	 * 
	 * @param objToSend Object that has to be sent to the server
	 */
	private void sendToServer(Object objToSend)
	{
		try
		{
			objOutputStream.reset();
			objOutputStream.writeObject(objToSend);

			if (objToSend instanceof String)
			{
				System.out.println("[CLIENT] - Message sent: " + (String) objToSend);
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
	 * Returns if the client is connected to the server
	 * 
	 * @return
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
