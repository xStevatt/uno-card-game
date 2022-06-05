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

	private final String playerNameClient;
	private String playerNameServer;

	private Object objReceived = null;

	private TableView view;
	private GameModel model;

	private Thread clientThread;

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

		ExecutorService executor = Executors.newFixedThreadPool(2);

		executor.execute(() -> {
			listenForNewMessagesToSend();
		});
		executor.execute(() -> {
			listenToServer();
		});
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
		Thread listeningThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (isConnected)
				{
					try
					{
						Thread.sleep(1000);

						objReceived = objInputStream.readObject();

						System.out.println("-> here");
						if (objReceived != null && objReceived instanceof String && ((String) objReceived).length() > 0)
						{
							System.out.println("[CLIENT] - Message received from server: " + ((String) objReceived));
						}
					}
					catch (EOFException e)
					{
						System.out.println("HERE");
					}
					catch (ClassNotFoundException e)
					{
						System.out.println("HERE");
					}
					catch (IOException e)
					{
						System.out.println("Ciao 3");
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String message = view.getMessage();

					if (view.getMessage().length() > 0)
					{
						try
						{
							// SLEEPS FOR 1000ms
							Thread.sleep(1000);
						}
						catch (InterruptedException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						try
						{
							objOutputStream.writeObject(message);
							view.addChatMessage(message, playerNameClient);
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		});

		listeningThread.start();
	}

	/**
	 * Listens for new messages to send
	 */
	public void listenForNewMessagesToSend()
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
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (!TableView.message.equals(""))
					{
						sendToServer(TableView.message);
						TableView.message = "";
					}
				}
			}
		}).start();
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
