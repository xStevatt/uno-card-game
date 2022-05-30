package unibs.pajc.uno.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import unibs.pajc.uno.controller.net.NetUtils;
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

	/**
	 * 
	 * Constructor containing IP address and port inserted by the user
	 * 
	 * @param view
	 * @param model
	 * @param IP_ADDRESS
	 * @param PORT
	 */
	public ServerController(String IP_ADDRESS, int PORT)
	{
		this.model = model;
		this.view = view;

		this.IP_ADDRESS = IP_ADDRESS;
		this.PORT = PORT;
	}

	/**
	 * 
	 * Sets default IP address and port
	 * 
	 * @param view
	 * @param model
	 */
	public ServerController(TableView view, GameModel model)
	{
		this.view = view;
		this.model = model;

		this.IP_ADDRESS = NetUtils.DEFAULT_IP_ADDRESS;
		this.PORT = NetUtils.DEFAULT_PORT;
	}

	public void initGame()
	{
		view = new TableView("", "", null, false);
		view.setVisible(true);

		while (!model.isGameOver())
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
