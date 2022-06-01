package unibs.pajc.uno.controller;

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

public class ServerController
{
	public static final int PORT_NUMBER = 1234;

	private Socket client;
	private String IP_ADDRESS;
	private int PORT;

	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;

	private TableView view;
	private GameModel model;

	private String playerName;

	/**
	 * 
	 * Constructor containing IP address and port inserted by the user
	 * 
	 * @param view
	 * @param model
	 * @param IP_ADDRESS
	 * @param PORT
	 */
	public ServerController(String IP_ADDRESS, int PORT, String playerName)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.PORT = PORT;

		this.playerName = playerName;
		System.out.println(playerName);

		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(this::startServer);
		executor.execute(this::listenToClient);
	}

	public boolean startServer()
	{
		boolean isConnected = false;

		try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER))
		{
			System.out.print("[SERVER] - Trying to launch server");
			serverSocket.setSoTimeout(100000);

			client = serverSocket.accept();
			objOutputStream = new ObjectOutputStream(client.getOutputStream());
			objInputStream = new ObjectInputStream(client.getInputStream());

			isConnected = true;
			System.out.println("[CLIENT] - " + isConnected);

			if (isConnected)
			{
				objOutputStream.writeObject(playerName);
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
		while (true)
		{
			try
			{
				objInputStream.readObject();
			}
			catch (IOException e)
			{
				System.out.println("Errors in listening to client");
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void sendToClient()
	{

	}
}
