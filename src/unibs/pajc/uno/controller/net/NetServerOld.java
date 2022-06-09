package unibs.pajc.uno.controller.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.view.TableView;

public class NetServerOld
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

	public NetServerOld(String IP_ADDRESS, int PORT, String playerNameServer)
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

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	public Packet waitForClient()
	{
		while (objReceivedGame == null)
		{
			if (objReceivedGame != null && objReceivedGame instanceof Packet)
			{
				return ((Packet) objReceivedGame);
			}
		}

		return null;
	}

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
				}
				if (objReceived != null && objReceived instanceof Packet)
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
		}
	}

	public void listenForNewMessagesToSend()
	{
		while (true)
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

	public void sendToClient(Object objToSend)
	{
		try
		{
			if (objToSend instanceof Packet)
			{
				System.out.println("[SERVER] - sending packet");
			}

			objOutputStream.writeObject(objToSend);

			if (objToSend instanceof String)
			{
				System.out.println("Message sent: " + (String) objToSend);
			}

			objOutputStream.flush();
		}
		catch (IOException e)
		{
			System.out.println("Error while sending - Couldn't send object to client");
			e.printStackTrace();
		}
	}
}