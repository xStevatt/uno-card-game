package unibs.pajc.uno.controller.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.view.TableView;

/**
 * Metodo per la gestione di molteplici client connessi al server. Eredita dalla
 * classe @class Threa
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
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

	/**
	 * 
	 * Ciascun client è rappresentato in modo univoco da un certo clientIndex. Nel
	 * caso in cui si voglia mandare un dato ad un solo client, ad esempio, è
	 * sufficiente richiedere l'elemento che nella lista dei Thread sia a quel certo
	 * index
	 *
	 * @param view            la view del gioco
	 * @param clientSocket    il socket del client
	 * @param clientIndex     l'indice univoco del client
	 * @param syncObjectModel l'oggetto necessario alla sincronizzazione delle
	 *                        azioni del giocatore
	 * @param syncObjectChat  l'oggetto necessario alla sincronizzazione delle
	 *                        azioni della chat di gioco
	 */
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

	/**
	 * Rimane in ascolto per eventuali dati ricevuti dallo stream del client
	 */
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

	/**
	 * Metodo che permette l'inviio di oggetto al client
	 * 
	 * @param objToSend l'oggetto da mandare al client
	 */
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