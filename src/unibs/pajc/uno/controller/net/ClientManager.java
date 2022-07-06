package unibs.pajc.uno.controller.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.view.TableView;

public class ClientManager extends Thread
{
	private TableView view;

	private Socket client;
	private String playerNameClient;
	private int clientIndex;

	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;

	private final Object syncObjectModel;
	private final Object syncObjectChat;

	private Object objReceivedGame = null;

	private boolean isConnected = false;

	public ClientManager(TableView view, Socket clientSocket, int clientIndex, Object syncObjectModel,
			Object syncObjectChat)
	{
		this.client = clientSocket;
		this.clientIndex = clientIndex;

		this.syncObjectModel = syncObjectModel;
		this.syncObjectChat = syncObjectChat;
	}

	@Override
	public void run()
	{
		try
		{
			objOutputStream = new ObjectOutputStream(client.getOutputStream());
			objInputStream = new ObjectInputStream(client.getInputStream());
			isConnected = true;
			System.out.println("[SERVER] - CONNECTED TO CLIENT: " + isConnected);
		}
		catch (IOException e)
		{
			isConnected = false;
		}

		if (isConnected)
		{
			try
			{
				// TRIES TO READ CLIENT NAME
				playerNameClient = (String) objInputStream.readObject();

				// WAITS FOR A VALID CLIENT NAME
				while (playerNameClient.equals(""))
				{
					playerNameClient = (String) objInputStream.readObject();
				}
			}
			catch (IOException e)
			{
				System.out.println("[SERVER] - No player init message received");
			}
			catch (NullPointerException e)
			{
				System.out.println("[SERVER] - No player init message received");
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void listenToClient()
	{
		Object objReceived = null;

		try
		{
			objReceived = objInputStream.readObject();

			if (objReceived != null && objReceived instanceof String && ((String) objReceived).length() > 0)
			{
				System.out.println("[SERVER] - Message received from client: " + objReceived);

				view.addChatMessage((String) objReceived, playerNameClient);
			}
			if (objReceived != null && objReceived instanceof GameModel)
			{
				synchronized (syncObjectModel)
				{
					syncObjectModel.notify();
				}

				objReceivedGame = objReceived;
				System.out.println("[SERVER] - Game model received");
			}
		}
		catch (EOFException e)
		{
			JOptionPane.showMessageDialog(view, "Network error!");
			System.exit(0);
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
	}

	public void sendToClient(Object objToSend)
	{
		try
		{
			objOutputStream.reset();
			objOutputStream.writeObject(objToSend);

			if (objToSend instanceof String)
			{
				System.out.println("Message sent: " + objToSend);
			}

			objOutputStream.flush();
		}
		catch (IOException e)
		{
			System.out.println("Error while sending - Couldn't send object to client");
			e.printStackTrace();
		}
	}
}