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

public class ServerController
{
	private Socket client;
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

	public ServerController(String IP_ADDRESS, int PORT, String playerNameServer)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.PORT = PORT;

		this.playerNameServer = playerNameServer;
		System.out.println(playerNameServer);

		startServer();

		try
		{
			Thread.sleep(10000);
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}

		System.out.println(isConnected);
		if (isConnected)
		{
			view = new TableView(playerNameServer, playerNameClient, NetUtils.ONLINE_GAME);
			view.setVisible(true);
			view.setResizable(false);
		}

		try
		{
			new Thread(() -> {
				listenToClient();
			}).join();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startView()
	{
		view = new TableView(null, null, true);
		view.setVisible(true);
	}

	public void startServer()
	{
		serverThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try (ServerSocket serverSocket = new ServerSocket(PORT))
				{
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
		});

		serverThread.start();
	}

	public void listenToClient()
	{
		while (isConnected)
		{
			try
			{
				Object objReceived;

				System.out.println("LMAO");

				objReceived = objInputStream.readObject();

				if (objReceived instanceof String && ((String) objReceived).length() > 0)
				{
					System.out.println("Message received from client: " + ((String) objReceived));
				}
			}
			catch (EOFException e)
			{
				System.out.println("Client disconnected");
				isConnected = false;
				System.exit(0);
			}
			catch (IOException e)
			{
				System.out.println("Errors in listening to client");
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void sendToClient(Object objToSend)
	{
		try
		{
			objOutputStream.writeObject(objToSend);
		}
		catch (IOException e)
		{
			System.out.println("Couldn't send to client");
			e.printStackTrace();
		}
	}
}
