package unibs.pajc.uno.controller.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ClientHandler implements Runnable
{
	private Socket clientSocket;
	private ObjectOutputStream objOutputStream;
	private ObjectInputStream objInputStream;

	private Object syncObjectModel;
	private String syncObjectChat;
	private Packet packetReceived;

	public ClientHandler(Socket clientSocket, Object syncObjectModel)
	{
		this.clientSocket = clientSocket;
		this.syncObjectModel = syncObjectModel;

		try
		{
			objOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			objInputStream = new ObjectInputStream(clientSocket.getInputStream());
		}
		catch (IOException e)
		{
			System.out.println("Errors while connecting");
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		listenToClient();
	}

	private void listenToClient()
	{
		while (true)
		{
			Object objReceived = null;

			try
			{
				objReceived = objInputStream.readObject();

				if (objReceived != null && objReceived instanceof String && ((String) objReceived).length() > 0)
				{
					System.out.println("[SERVER] - Message received from client: " + ((String) objReceived));

					syncObjectChat = (String) objReceived;

					synchronized (syncObjectChat)
					{
						syncObjectChat.notify();
					}
				}
				if (objReceived != null && objReceived instanceof Packet)
				{
					synchronized (syncObjectModel)
					{
						syncObjectModel.notify();
					}

					packetReceived = (Packet) objReceived;
					System.out.println("New packet received from -> " + packetReceived.getClientSending());
				}
			}
			catch (EOFException e)
			{
				JOptionPane.showMessageDialog(null, "Network error!");
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
	}
}
