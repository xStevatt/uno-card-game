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
	private Object objReceivedGame = null;

	private TableView view;
	private GameModel model;

	private String playerNameServer = null;
	private String playerNameClient = null;

	private int indexCurrentPlayer = 0;

	public NetServer(String IP_ADDRESS, int PORT, String playerNameServer)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.PORT = PORT;

		this.playerNameServer = playerNameServer;
		System.out.println(playerNameServer);

		startServer();
		view = new TableView(playerNameServer, playerNameClient, false);
		view.setVisible(true);

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		executor.execute(this::listenForNewMessagesToSend);
		executor.execute(this::listenToClient);
		executor.execute(this::runGameLogic);
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

		while (!model.isGameOver())
		{
			updateView(server, client);

			if (model.getCurrentPlayerIndex() == 0)
			{
				checkPlayerSaidUno();
				view.setTurn(model.getCurrentPlayer().getNamePlayer());

				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				System.out.println("HERE");

				while (CardView.isCardSelected == false && CardBackView.isCardDrawnFromDeck == false)
				{
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
					view.repaint();
				}

				// SENDS MATCH MODEL TO SERVER
				// sendToClient(model);
			}
			if (model.getCurrentPlayerIndex() == 1)
			{
				view.setTurn(model.getCurrentPlayer().getNamePlayer());

				this.model = waitForUser();
			}

			try
			{
				Thread.sleep(5000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			model.nextTurn();
		}
	}

	public void turnGame()
	{
		if (CardView.isCardSelected == true)
		{
			if (model.hasPlayerOneCard() && !view.isUnoButtonPressed())
			{
				JOptionPane.showMessageDialog(view, "You didn't say UNO! Two more cards for you.");
				model.playerDidNotSayUno(model.getCurrentPlayerIndex());
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
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Please select a valid card!", "Error", JOptionPane.ERROR_MESSAGE);
				CardView.isCardSelected = false;
			}

			CardView.isCardSelected = false;
		}
		if (CardBackView.isCardDrawnFromDeck == true && model.getCurrentPlayer().getHandCards().getNumberOfCards() < 30)
		{
			if (model.hasPlayerOneCard() && view.isUnoButtonPressed())
			{
				JOptionPane.showMessageDialog(view, "You didn't say UNO! Two more cards for you.");
				model.playerDidNotSayUno(model.getCurrentPlayerIndex());
			}

			model.getCurrentPlayer().addCard(model.getCardFromDeck());
			model.nextTurn();
		}
		else if (CardBackView.isCardDrawnFromDeck == true
				&& model.getCurrentPlayer().getHandCards().getNumberOfCards() == 30)
		{
			JOptionPane.showMessageDialog(view, "Hai già troppe carte!");
		}
	}

	public GameModel waitForUser()
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

	/**
	 * 
	 * @param server
	 * @param client
	 */
	public void updateView(Player server, Player client)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				view.setTurn(model.getCurrentPlayer().getNamePlayer());

				view.enableViewPlayer(model.getCurrentPlayerIndex(), true);
				view.enableViewPlayer(model.getNextPlayerIndex(), false);

				view.loadCards(server.getHandCards(), 0);
				view.loadCards(client.getHandCards(), 1);
				view.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());
			}
		});
	}

	public void checkPlayerSaidUno()
	{
		if (model.hasPlayerOneCard(model.getCurrentPlayer()))
		{
			view.setSayUnoButtonVisibile(true, model.getCurrentPlayerIndex());
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
					// SENDS SERVER NAME TO CLIENT
					objOutputStream.writeObject(playerNameServer);

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
				// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
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
	public void sendToClient(Object objToSend)
	{
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
