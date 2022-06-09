package unibs.pajc.uno.controller.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.view.TableView;

public class NetClient
{
	private final String IP_ADDRESS;
	private final int port;
	private boolean isConnected = false;

	private volatile ObjectInputStream objInputStream;
	private volatile ObjectOutputStream objOutputStream;
	private volatile Object objReceivedGame = null;

	private String playerNameClient;
	private String playerNameServer;

	private TableView view;
	private GameModel model;

	private Packet packetReceived = null;
	private int currentIteration = 0;

	public NetClient(String IP_ADDRESS, int port, String playerNameClient)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.port = port;
		this.playerNameClient = playerNameClient;

		startClient();
		view = new TableView(null, null, false);
		view.setTitle(playerNameClient);

		startClient();
	}

	private void startClient()
	{
		try
		{
			Socket clientSocket = new Socket(IP_ADDRESS, port);

			System.out.println("[CLIENT] - Trying to connect to server");

			objInputStream = new ObjectInputStream(clientSocket.getInputStream());
			objOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

			isConnected = true;
			System.out.println("[CLIENT] - CONNECTED TO SERVER: " + isConnected);

			if (isConnected)
			{
				try
				{
					// SENDS CLIENT NAME TO SERVER
					objOutputStream.writeObject(playerNameClient);
					this.model = (GameModel) objInputStream.readObject();

					if (model != null)
					{
						System.out.println("[CLIENT] - Model set");
					}
				}
				catch (IOException e)
				{
					System.out.println("Error while getting init details");
					e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
}
