package unibs.pajc.uno.controller.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.view.TableView;

public class NetClient
{
	private Socket clientSocket;
	private final String IP_ADDRESS;
	private final int port;
	private boolean isConnected = false;

	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;
	private Object objReceivedGame = null;

	private final String playerNameClient;
	private String playerNameServer;

	private TableView view;
	private GameModel model;

	private Thread clientThread;

	private int indexCurrentPlayer = 1;

	public NetClient(String IP_ADDRESS, int port, String playerName)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.port = port;
		this.playerNameClient = playerName;

		connectToServer();

		try
		{
			Thread.sleep(NetUtils.DEFAULT_SERVER_TIME_OUT);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		executor.execute(this::listenForNewMessagesToSend);
		executor.execute(this::listenToServer);
	}

	public void startView()
	{
		view = new TableView(playerNameClient, playerNameServer, false);
		view.setVisible(true);
	}

	/**
	 * 
	 * @return true if client is successfully connected to the server
	 */
	private void connectToServer()
	{
		clientThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					clientSocket = new Socket(IP_ADDRESS, port);

					System.out.println("[CLIENT] - Trying to connect to server");

					objInputStream = new ObjectInputStream(clientSocket.getInputStream());
					objOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

					isConnected = true;
					System.out.println("[CLIENT] - CONNECTED TO SERVER: " + isConnected);

					if (isConnected)
					{
						try
						{
							objOutputStream.writeObject(playerNameClient);
							playerNameServer = (String) objInputStream.readObject();

							while (playerNameServer.equals(""))
							{
								playerNameServer = (String) objInputStream.readObject();
							}
						}
						catch (Exception e)
						{
							System.out.println("Error while getting init details");
							e.printStackTrace();
						}
					}
					// initializeGame();
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
		});

		clientThread.start();
		startView();

		/*
		 * try { new Thread(new Runnable() {
		 * 
		 * @Override public void run() {
		 * 
		 * sendToServer("Ciao dal client");
		 * 
		 * } }).join(); } catch (InterruptedException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
	}

	/**
	 * Makes the server listen to the client
	 */
	private void listenToServer()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				Object objReceived;

				while (true)
				{
					objReceived = null;

					try
					{
						objReceived = objInputStream.readObject();

						if (objReceived != null && objReceived instanceof String && ((String) objReceived).length() > 0)
						{
							System.out.println("[CLIENT] - Message received from server: " + ((String) objReceived));

							view.addChatMessage((String) objReceived, playerNameServer);

							Thread.sleep(1000);
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
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
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
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (TableView.message.equals("") == false)
			{
				sendToServer(TableView.message);

				try
				{
					Thread.sleep(3000);
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
	 * Sends data to the server.
	 * 
	 * @param objToSend Object that has to be sent to the server
	 */
	private void sendToServer(Object objToSend)
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
