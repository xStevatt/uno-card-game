package unibs.pajc.uno.controller.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.CardBackView;
import unibs.pajc.uno.view.CardView;
import unibs.pajc.uno.view.DialogSelectNewColor;
import unibs.pajc.uno.view.TableView;

public class NetClient
{
	private Socket clientSocket;
	private final String IP_ADDRESS;
	private final int port;
	private boolean isConnected = false;

	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;
	private volatile Object objReceivedGame = null;

	private volatile String playerNameClient;
	private volatile String playerNameServer;

	private TableView view;
	private GameModel model;

	public NetClient(String IP_ADDRESS, int port, String playerName)
	{
		this.IP_ADDRESS = IP_ADDRESS;
		this.port = port;
		this.playerNameClient = playerName;

		startClient();
		System.out.println("[CLIENT] - starting");
		view = new TableView(null, null, false);
		// view.setVisible(true);

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		executor.execute(this::listenForNewMessagesToSend);
		executor.execute(this::listenToServer);
		executor.execute(this::runGameLogic);
	}

	public void initView()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				view.setVisible(true);
				view.setResizable(false);

				view.setMatchLabel(model.getPlayers().get(1).getNamePlayer(),
						model.getPlayers().get(0).getNamePlayer());

				view.setTitle(model.getPlayers().get(0).getNamePlayer());
			}
		});
	}

	/**
	 * 
	 */
	public void runGameLogic()
	{
		// WAITS FOR SERVER TO SEND MODEL
		while (objReceivedGame == null)
		{
			System.out.println("[CLIENT] - waiting for server");
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			System.out.println(" 1[CLIENT] - waiting for server");
		}

		this.model = ((GameModel) objReceivedGame);
		objReceivedGame = null;

		initView();

		this.playerNameClient = model.getPlayers().get(1).getNamePlayer();
		this.playerNameServer = model.getPlayers().get(0).getNamePlayer();
		System.out.println("Server name in running: " + model.getPlayers().get(0).getNamePlayer());

		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}

		while (!model.isGameOver())
		{
			updateView(model.getPlayers().get(1), model.getPlayers().get(0));

			if (model.getCurrentPlayerIndex() == 0)
			{
				model = waitForServer();
				objReceivedGame = null;

				updateView(model.getPlayers().get(1), model.getPlayers().get(0));
			}
			if (model.getCurrentPlayerIndex() == 1)
			{
				checkPlayerSaidUno();
				view.setTurn(model.getCurrentPlayer().getNamePlayer());

				while (CardView.isCardSelected == false && CardBackView.isCardDrawnFromDeck == false)
				{
					turnGame();
					updateView(model.getPlayers().get(1), model.getPlayers().get(0));

					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}

			}
		}
	}

	/**
	 * 
	 */
	public void turnGame()
	{
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (CardView.isCardSelected == true)
		{
			System.out.println("[CLIENT] - card selected");

			if (model.hasPlayerOneCard() && !view.isUnoButtonPressed())
			{
				JOptionPane.showMessageDialog(view, "You didn't say UNO! Two more cards for you.");
				model.playerDidNotSayUno(model.getCurrentPlayerIndex());
			}

			if (model.isPlacedCardValid(CardView.cardSelected))
			{
				view.changeDroppedCardView(CardView.cardSelected, model.getCurrentCardColor());
				boolean newColorSelection = model.evalMossa(CardView.cardSelected);

				if (newColorSelection)
				{
					DialogSelectNewColor dialogColor = new DialogSelectNewColor();
					CardColor cardColor = dialogColor.show();
					model.setCurrentCardColor(cardColor);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Please select a valid card!", "Error", JOptionPane.ERROR_MESSAGE);
				CardView.isCardSelected = false;
			}

			CardView.isCardSelected = false;
		}
		if (CardBackView.isCardDrawnFromDeck == true && model.getCurrentPlayer().getHandCards().getNumberOfCards() < 30)
		{
			System.out.println("[CLIENT] - Card drawn");

			if (model.hasPlayerOneCard() && view.isUnoButtonPressed())
			{
				JOptionPane.showMessageDialog(view, "You didn't say UNO! Two more cards for you.");
				model.playerDidNotSayUno(model.getCurrentPlayerIndex());
			}

			model.getCurrentPlayer().addCard(model.getCardFromDeck());
			model.nextTurn();
		}
		else if (CardBackView.isCardDrawnFromDeck == true
				&& model.getCurrentPlayer().getHandCards().getNumberOfCards() == 30)
		{
			JOptionPane.showMessageDialog(view, "Hai già troppe carte!");
		}

		// RESETTING FLAGS
		CardView.isCardSelected = false;
		CardBackView.isCardDrawnFromDeck = false;
	}

	/**
	 * 
	 * @return
	 */
	public GameModel waitForServer()
	{
		while (objReceivedGame == null)
		{
			if (objReceivedGame != null && objReceivedGame instanceof GameModel)
			{
				return ((GameModel) objReceivedGame);
			}
		}

		return null;
	}

	/**
	 * 
	 * @param server
	 * @param client
	 */
	public void updateView(Player server, Player client)
	{
		for (int i = 0; i < server.getHandCards().getNumberOfCards(); i++)
		{
			System.out.println(server.getHandCards().getCard(i).getCardType() + " - "
					+ server.getHandCards().getCard(i).getCardColor());
		}
		System.out.println("---");
		for (int i = 0; i < client.getHandCards().getNumberOfCards(); i++)
		{
			System.out.println(client.getHandCards().getCard(i).getCardType() + " - "
					+ client.getHandCards().getCard(i).getCardColor());
		}

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				view.setTurn(model.getCurrentPlayer().getNamePlayer());

				view.enableViewPlayer(model.getCurrentPlayerIndex(), true);
				view.enableViewPlayer(model.getNextPlayerIndex(), false);

				view.loadCards(server.getHandCards(), 0);

				view.loadCards(client.getHandCards(), 1);
				view.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());
			}
		});
	}

	/**
	 * 
	 */
	public void checkPlayerSaidUno()
	{
		if (model.hasPlayerOneCard(model.getCurrentPlayer()))
		{
			view.setSayUnoButtonVisibile(true, model.getCurrentPlayerIndex());
		}
	}

	/**
	 * 
	 * @return true if client is successfully connected to the server
	 */
	private void startClient()
	{
		try
		{
			clientSocket = new Socket(IP_ADDRESS, port);

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
				}
				catch (IOException e)
				{
					System.out.println("Error while getting init details");
					e.printStackTrace();
				}
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
	}

	/**
	 * Makes the server listen to the client
	 */
	private synchronized void listenToServer()
	{
		while (true)
		{
			Object objReceived = null;

			try
			{
				objReceived = objInputStream.readObject();

				if (objReceived != null && objReceived instanceof String && ((String) objReceived).length() > 0)
				{
					System.out.println("[CLIENT] - Message received from server: " + ((String) objReceived));

					System.out.println("Server name: " + playerNameServer);
					view.addChatMessage((String) objReceived, playerNameServer);

					Thread.sleep(1000);
				}
				if (objReceived != null && objReceived instanceof GameModel)
				{
					System.out.println("[CLIENT] - New game model received from server. Adversary: "
							+ ((GameModel) objReceived).getPlayers().get(0).getNamePlayer());

					objReceivedGame = objReceived;
				}
			}
			catch (EOFException e)
			{
				System.out.println("EOFException");
			}
			catch (IOException e)
			{
				System.out.println("Errors in listening to the server");
				System.exit(0);

			}
			catch (ClassNotFoundException e)
			{
				System.out.print("Class not found");
				System.exit(0);
			}
			catch (InterruptedException e)
			{
				System.out.println("Interrupted execution");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Listens for new messages to send
	 */
	public void listenForNewMessagesToSend()
	{
		while (true)
		{
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (TableView.message.equals("") == false)
			{
				sendToServer(TableView.message);

				// COOLDOWN - DEFAULT 700ms
				try
				{
					Thread.sleep(700);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				// RESETS LAST MESSAGE SENT
				TableView.message = "";
			}
		}
	}

	/**
	 * Sends data to the server.
	 * 
	 * @param objToSend Object that has to be sent to the server
	 */
	private void sendToServer(Object objToSend)
	{
		try
		{
			objOutputStream.writeObject(objToSend);

			if (objToSend instanceof String)
			{
				System.out.println("[CLIENT] - Message sent: " + (String) objToSend);
			}
		}
		catch (IOException e)
		{
			System.out.println("Error while sending - Couldn't send object to client");
			e.printStackTrace();
		}
	}

	/**
	 * Returns if the client is connected to the server
	 * 
	 * @return
	 */
	public boolean isConnected()
	{
		return isConnected;
	}

	/**
	 * Returns the name of the server, received from the server
	 * 
	 * @return the String name of the client
	 */
	public String getServerName()
	{
		return playerNameServer;
	}
}
