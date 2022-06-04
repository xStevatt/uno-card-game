package unibs.pajc.uno.controller.net;

import java.io.EOFException;
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

	private TableView view;
	private GameModel model;

	private String playerNameServer;
	private String playerNameClient;

	private Thread serverThread;

	public NetServer(String IP_ADDRESS, int PORT, String playerNameServer)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.PORT = PORT;

		this.playerNameServer = playerNameServer;
		System.out.println(playerNameServer);

		startServer();
		listenToClient();

		try
		{
			Thread.sleep(NetUtils.DEFAULT_SERVER_TIME_OUT);
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}

		System.out.println("Is server connected?" + isConnected);

		if (isConnected)
		{
			view = new TableView(playerNameServer, playerNameClient, NetUtils.ONLINE_GAME);
			view.setVisible(true);
			view.setResizable(false);
		}

	}

	public void startView()
	{
		view = new TableView(null, null, true);
		view.setVisible(true);
	}

	public void startServer()
	{
		try
		{
			serverSocket = new ServerSocket(PORT);
			System.out.print("[SERVER] - Trying to launch server");

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
					objOutputStream.writeObject(playerNameServer);
					playerNameClient = (String) objInputStream.readObject();

					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					while (playerNameClient.equals(""))
					{
						System.out.print("HERE ->>> ");
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
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						Object objReceived = objInputStream.readObject();

						if (objReceived instanceof String && ((String) objReceived).length() > 0)
						{
							System.out.println("Message received from client: " + ((String) objReceived));
						}
					}
					catch (EOFException e)
					{

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
		}).start();
	}

	public void sendToClient(Object objToSend)
	{
		try
		{
			objOutputStream.writeObject(objToSend);
		}
		catch (IOException e)
		{
			System.out.println("Error while sending - Couldn't send object to client");
			e.printStackTrace();
		}
	}
}
