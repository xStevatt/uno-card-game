package unibs.pajc.uno.controller.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.view.GameView;

public class ServerController
{
	public static final int PORT_NUMBER = 1234;

	private Socket client;
	private String IP_ADDRESS;
	private int PORT;

	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;

	private GameView view;
	private GameModel model;

	public ServerController(GameView view, GameModel model, String IP_ADDRESS, int PORT)
	{
		this.model = model;
		this.view = view;
		this.IP_ADDRESS = IP_ADDRESS;
		this.PORT = PORT;
	}

	public void initGame()
	{
		view = new GameView();
		view.setVisible(true);

		boolean isGameOver = false;

		while (!isGameOver)
		{

		}
	}

	public boolean startServer()
	{
		boolean isConnected = false;

		try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER))
		{
			serverSocket.setSoTimeout(10000);

			client = serverSocket.accept();
			objOutputStream = new ObjectOutputStream(client.getOutputStream());
			objInputStream = new ObjectInputStream(client.getInputStream());

			isConnected = true;
		}
		catch (SocketTimeoutException e)
		{
			System.err.println("Communication time-out: " + e);
		}
		catch (IOException e)
		{
			System.err.println("Some communication error happened: " + e);

		}

		return isConnected;
	}

	public void listenToClient()
	{

	}

	public void sendToClient()
	{

	}
}
