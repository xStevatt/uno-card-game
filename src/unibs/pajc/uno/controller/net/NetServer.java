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
import unibs.pajc.uno.view.CardBackView;
import unibs.pajc.uno.view.CardView;
import unibs.pajc.uno.view.DialogSelectNewColor;
import unibs.pajc.uno.view.TableView;

public class NetServer
{
	private Socket client;
	private ServerSocket serverSocket;
	private String IP_ADDRESS;
	private int PORT;
	private boolean isConnected = false;

	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;
	private volatile Object objReceivedGame = null;

	private TableView view;
	private volatile GameModel model;

	private String playerNameServer = null;
	private String playerNameClient = null;

	public NetServer(String IP_ADDRESS, int PORT, String playerNameServer)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.PORT = PORT;

		this.playerNameServer = playerNameServer;
		System.out.println(playerNameServer);

		startServer();
		view = new TableView(playerNameServer, playerNameClient, false);
		initView();

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		executor.execute(this::listenForNewMessagesToSend);
		executor.execute(this::listenToClient);
		executor.execute(this::runGameLogic);
	}

	/**
	 * 
	 */
	public void initView()
	{
		view.setVisible(true);
		view.setResizable(false);
		view.setTitle(playerNameServer);
	}

	/**
	 * 
	 * @param server
	 * @param client
	 */
	public void updateView(Player server, Player client, int playingPlayer)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				view.repaint();
				logCurrentCards(server, client);

				view.setTurn(model.getCurrentPlayer().getNamePlayer());

				view.loadCards(server.getHandCards(), 0);
				view.addCardsToViewBack(client.getHandCards().getNumberOfCards());

				view.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());

				changeTurnView(playingPlayer);
				view.repaint();
			}
		});
	}

	/**
	 * 
	 * @param playingPlayer
	 */
	public void changeTurnView(int playingPlayer)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				if (playingPlayer == 0)
				{
					view.enableViewPlayer(0, true);
				}
				if (playingPlayer == 1)
				{
					view.enableViewPlayer(0, false);
				}
			}
		});
	}

	/**
	 * 
	 */
	public void runGameLogic()
	{
		model = new GameModel();

		Player server = new Player(playerNameServer, model.generateStartingCards(), 0);
		Player client = new Player(playerNameClient, model.generateStartingCards(), 1);

		model.initPlayers(new ArrayList<Player>(Arrays.asList(new Player[] { server, client })));

		// SENDING MODEL TO CLIENT
		sendToClient(model);

		while (!model.isGameOver())
		{
			updateView(model.getPlayers().get(0), model.getPlayers().get(1), 0);

			if (model.getCurrentPlayerIndex() == 0)
			{
				checkPlayerSaidUno();

				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				changeTurnView(model.getCurrentPlayerIndex());

				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				while (CardView.isCardSelected == false && CardBackView.isCardDrawnFromDeck == false
						&& model.getCurrentPlayerIndex() == 0)
				{
					// System.out.println("Working");
					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					turnGame();

					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				updateView(model.getPlayers().get(0), model.getPlayers().get(1), 0);
				// --------- SENDS MATCH MODEL TO SERVER ----------
				System.out.println("[SERVER] - Sending model to client. Number of cards server: "
						+ model.getPlayers().get(0).getHandCards().getNumberOfCards());

				sendToClient(model);

				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (model.getCurrentPlayerIndex() == 1)
			{
				changeTurnView(model.getCurrentPlayerIndex());

				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				this.model = waitForClient();

				updateView(model.getPlayers().get(0), model.getPlayers().get(1), model.getCurrentPlayerIndex());
			}

			model.nextTurn();
		}

		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		CardView.isCardSelected = false;
		CardBackView.isCardDrawnFromDeck = false;
	}

	public void turnGame()
	{
		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{

			e.printStackTrace();
		}

		if (CardView.isCardSelected == true)
		{
			manageCardSelected();
		}
		if (CardBackView.isCardDrawnFromDeck == true && model.getCurrentPlayer().getHandCards().getNumberOfCards() < 30)
		{
			manageCardDrawn();
		}
		else if (CardBackView.isCardDrawnFromDeck == true
				&& model.getCurrentPlayer().getHandCards().getNumberOfCards() >= 30)
		{
			JOptionPane.showMessageDialog(view, "Hai già troppe carte!");
		}

		// RESETTING FLAGS
		CardView.isCardSelected = false;
		CardBackView.isCardDrawnFromDeck = false;
	}

	/**
	 * 
	 */
	public void manageCardDrawn()
	{
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("[SERVER] - Card drawn");

		if (model.hasPlayerOneCard() && view.isUnoButtonPressed())
		{
			JOptionPane.showMessageDialog(view, "You didn't say UNO! Two more cards for you.");
			model.playerDidNotSayUno(model.getCurrentPlayerIndex());
		}

		model.getCurrentPlayer().addCard(model.getCardFromDeck());
		updateView(model.getPlayers().get(0), model.getPlayers().get(1), 0);
		model.nextTurn();
	}

	/**
	 * 
	 */
	public void manageCardSelected()
	{
		System.out.println("[SERVER] - card selected");

		if (model.hasPlayerOneCard() && !view.isUnoButtonPressed())
		{
			JOptionPane.showMessageDialog(view, "You didn't say UNO! Two more cards for you.");
			model.playerDidNotSayUno(model.getCurrentPlayerIndex());
		}

		if (model.isPlacedCardValid(CardView.cardSelected))
		{
			view.changeDroppedCardView(CardView.cardSelected, model.getCurrentCardColor());
			boolean newColorSelection = model.evalMossa(CardView.cardSelected);
			updateView(model.getPlayers().get(0), model.getPlayers().get(1), 0);

			if (newColorSelection)
			{
				DialogSelectNewColor dialogColor = new DialogSelectNewColor();
				CardColor cardColor = dialogColor.show();
				model.setCurrentCardColor(cardColor);
			}

			updateView(model.getPlayers().get(0), model.getPlayers().get(1), 0);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please select a valid card!", "Error", JOptionPane.ERROR_MESSAGE);
			CardView.isCardSelected = false;
		}

		CardView.isCardSelected = false;
	}

	public GameModel waitForClient()
	{
		while (objReceivedGame == null)
		{
			if (objReceivedGame != null && objReceivedGame instanceof GameModel)
			{
				return ((GameModel) objReceivedGame);
			}
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

	public void logCurrentCards(Player server, Player client)
	{
		for (int i = 0; i < server.getHandCards().getNumberOfCards(); i++)
		{
			System.out.println(server.getHandCards().getCard(i).getCardType() + " - "
					+ server.getHandCards().getCard(i).getCardColor());
		}
		System.out.println("---");
		for (int i = 0; i < client.getHandCards().getNumberOfCards(); i++)
		{
			System.out.println(client.getHandCards().getCard(i).getCardType() + " - "
					+ client.getHandCards().getCard(i).getCardColor());
		}
	}

	/**
	 * Initializes the server
	 */
	public void startServer()
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
					// SENDS SERVER NAME TO CLIENT (not necessary)
					// objOutputStream.writeObject(playerNameServer);

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
	public void listenToClient()
	{
		while (true)
		{
			Object objReceived = null;

			try
			{
				objReceived = objInputStream.readObject();

				if (objReceived != null && objReceived instanceof String && ((String) objReceived).length() > 0)
				{
					System.out.println("[SERVER] - Message received from client: " + ((String) objReceived));

					view.addChatMessage((String) objReceived, playerNameClient);

					Thread.sleep(1000);
				}
				if (objReceived != null && objReceived instanceof GameModel)
				{
					objReceivedGame = objReceived;
					System.out.println("[SERVER] - Game model received");
				}
			}
			catch (EOFException e)
			{
				System.out.println("Error while connected!");
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
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Listens for possible new messages to send to the client
	 */
	public void listenForNewMessagesToSend()
	{
		while (true)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
			}
			if (TableView.message.equals("") == false)
			{
				sendToClient(TableView.message);

				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				TableView.message = "";
			}
		}
	}

	/**
	 * Sends an object to the client
	 * 
	 * @param objToSend the object to send to the client
	 */
	public synchronized void sendToClient(Object objToSend)
	{
		if (objToSend instanceof GameModel)
		{
			System.out.println(((GameModel) objToSend).getPlayers().get(0).getNamePlayer() + " . "
					+ ((GameModel) objToSend).getPlayers().get(0).getHandCards().getNumberOfCards());
		}

		try
		{
			objOutputStream.writeObject(objToSend);

			if (objToSend instanceof String)
			{
				System.out.println("Message sent: " + (String) objToSend);
			}
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