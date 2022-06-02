package unibs.pajc.uno.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.view.TableView;

public class ClientController
{
	private Socket clientSocket;
	private final String IP_ADDRESS;
	private final int port;
	private boolean isConnected = false;

	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;

	private final String playerNameClient;
	private String playerNameServer;

	private TableView view;
	private GameModel model;

	private Thread clientThread;

	public ClientController(String IP_ADDRESS, int port, String playerName)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.port = port;
		this.playerNameClient = playerName;

		connectToServer();

		System.out.print("Here: " + isConnected);
		if (isConnected)
		{
			System.out.println("HERE - CDASDASDASD");
			view = new TableView(playerNameServer, playerNameClient, false);
			view.setVisible(true);
			view.setResizable(false);
		}
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

						listenToServer();
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
	}

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
						Object objReceived = objInputStream.readObject();

						if (objReceived instanceof String && ((String) objReceived).length() > 0)
						{
							System.out.println("Message received from server: " + ((String) objReceived));
						}
					}
					catch (IOException e)
					{
						System.out.println("Errors in listening to server");
					}
					catch (ClassNotFoundException e)
					{
						e.printStackTrace();
					}
				}
			}
		});

		listeningThread.start();
	}

	private void sendToServer(Object objToSend)
	{
		try
		{
			objOutputStream.writeObject(objToSend);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
