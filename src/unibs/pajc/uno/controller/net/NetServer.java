package unibs.pajc.uno.controller.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import unibs.pajc.uno.model.GameModel;
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

	private Packet packetReceived = null;
	private int currentIteration = 0;

	public NetServer(String IP_ADDRESS, int PORT, String playerNameServer)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.PORT = PORT;

		this.playerNameServer = playerNameServer;
		System.out.println(playerNameServer);

		startServer();
		view = new TableView(playerNameServer, playerNameClient, false);
		view.setVisible(true);
		view.setResizable(false);
		view.setTitle(playerNameServer);
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
					// TRIES TO READ CLIENT NAME
					playerNameClient = (String) objInputStream.readObject();

					// WAITS FOR A VALID CLIENT NAME
					while (playerNameClient.equals(""))
					{
						playerNameClient = (String) objInputStream.readObject();
						this.model = new GameModel();
						objOutputStream.writeObject(model);
						System.out.println("[SERVER] - Name Received from client: " + playerNameClient);
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
}