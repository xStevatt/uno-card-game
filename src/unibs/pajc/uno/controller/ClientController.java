package unibs.pajc.uno.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.view.TableView;

public class ClientController
{
	private Socket clientSocket;
	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;

	private final String IP_ADDRESS;
	private final int port;
	private final String playerName;

	private ExecutorService executor;

	private TableView view;
	private GameModel model;

	public ClientController(String IP_ADDRESS, int port, String playerName)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.port = port;
		this.playerName = playerName;

		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(this::connectToServer);
	}

	public void initView()
	{

	}

	public void initModel()
	{

	}

	/**
	 * 
	 * @return true if client is successfully connected to the server
	 */
	private boolean connectToServer()
	{
		boolean isConnected = false;

		try
		{
			clientSocket = new Socket("127.0.0.1", ServerController.PORT_NUMBER);

			System.out.println("[CLIENT] - Trying to connect to server");

			objInputStream = new ObjectInputStream(clientSocket.getInputStream());
			objOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

			isConnected = true;
			System.out.println("[CLIENT] - " + isConnected);

			if (isConnected)
			{
				initView();

				String initPlayer = "";

				try
				{
					while (initPlayer.equals(""))
					{
						initPlayer = (String) objInputStream.readObject();
					}
				}
				catch (Exception e)
				{
					System.out.print("Error while getting init details");
				}

				System.out.print("Server name: " + initPlayer);
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

		return isConnected;
	}

	private void listenToServer()
	{
		while (!clientSocket.isClosed())
		{
			try
			{
				if (objInputStream.readObject() != null && (objInputStream.readObject() instanceof String))
				{
					System.out.println("Ciao");
				}
			}
			catch (ClassNotFoundException | IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
