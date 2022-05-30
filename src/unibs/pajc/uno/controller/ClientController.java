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

	private ExecutorService executor;

	private TableView view;
	private GameModel model;

	public ClientController(String IP, int port, String playerName)
	{

	}

	private void initializeGame()
	{
		connectToServer();

		executor = Executors.newFixedThreadPool(2);
		executor.execute(this::listenToServer);
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

			objInputStream = new ObjectInputStream(clientSocket.getInputStream());
			objOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

			isConnected = true;
			initializeGame();
		}
		catch (UnknownHostException e)
		{
			System.err.println("IP address of the host could not be determined : " + e.toString());
		}
		catch (IOException e)
		{
			System.err.println("Error in creating socket: " + e.toString());
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
