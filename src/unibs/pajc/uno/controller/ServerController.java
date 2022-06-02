package unibs.pajc.uno.controller;

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

	public ServerController(String IP_ADDRESS, int PORT, String playerNameServer)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.PORT = PORT;

		this.playerNameServer = playerNameServer;
		System.out.println(playerNameServer);

		if (startServer())
		{
			view = new TableView(playerNameServer, playerNameClient, true);
			view.setVisible(true);
			view.setResizable(false);
		}

		new Thread(() -> {
			listenToClient();
		}).start();
	}

	public void startView()
	{
		view = new TableView(null, null, true);
		view.setVisible(true);
	}

	public boolean startServer()
	{
		boolean isClientConnected = false;

		try (ServerSocket serverSocket = new ServerSocket(PORT))
		{
			System.out.print("[SERVER] - Trying to launch server");

			System.out.println("HERE");

			serverSocket.setSoTimeout(100000);
			client = serverSocket.accept();
			objOutputStream = new ObjectOutputStream(client.getOutputStream());
			objInputStream = new ObjectInputStream(client.getInputStream());

			isClientConnected = true;
			System.out.println("[SERVER] - " + isConnected);

			if (isClientConnected)
			{
				try
				{
					objOutputStream.writeObject(playerNameServer);
					playerNameClient = (String) objInputStream.readObject();
				}
				catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
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

		return isConnected;
	}

	public void listenToClient()
	{
		while (isConnected)
		{
			System.out.println("HERE");
			try
			{
				Object objReceived;

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
