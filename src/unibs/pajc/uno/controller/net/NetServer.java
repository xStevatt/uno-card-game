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

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.player.Player;
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
	private Object objReceivedGame;

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

	public void updateView(Player server, Player client)
	{
		view.setTurn(model.getCurrentPlayer().getNamePlayer());

		view.enableViewPlayer(model.getCurrentPlayerIndex(), true);
		view.enableViewPlayer(model.getNextPlayerIndex(), false);

		view.loadCards(server.getHandCards(), 0);
		view.loadCards(client.getHandCards(), 1);
		view.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());
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
	 * Listens for possible new messages to send
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
}
