package unibs.pajc.uno.controller;

import java.util.Random;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.CardBackView;
import unibs.pajc.uno.view.CardView;
import unibs.pajc.uno.view.TableView;

public class LocalPlayerController
{
	private TableView gameView;
	private GameModel model;

	public LocalPlayerController(String playerOneName, String playerTwoName)
	{
		// INITS MODEL
		model = new GameModel();
		initModel(playerOneName, playerTwoName);

		// INITS VIEW
		gameView = new TableView(playerOneName, playerTwoName, model.getUsedCards().getLastCardUsed(), true);
		initView();

		// START RUNNING GAME
		runGame();
	}

	public void runGame()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				int turn = new Random().nextInt(1);

				while (!model.isGameOver())
				{
					if (turn == 0)
					{
						gameView.enableViewPlayer(1, false);

						for (int i = 0; i < gameView.getAllCards(turn).size(); i++)
						{
							CardView cardView = gameView.getAllCards(turn).get(i);
							gameView.getAllCards(turn).get(i).addMouseListener(gameView.getAllCards(turn).get(i));
						}

						while (CardView.cardSelected == null && CardBackView.isCardDrawnFromDeck == false)
						{
							if (CardView.cardSelected != null && CardBackView.isCardDrawnFromDeck == false)
							{
								System.out.print("Sadasdasd");
								System.out.println(CardView.cardSelected.getCardColor() + " "
										+ CardView.cardSelected.getCardType());
							}
							if (CardView.cardSelected == null && CardBackView.isCardDrawnFromDeck == true)
							{
								System.out.println("ciao");
							}
						}
					}
					else
					{
						gameView.enableViewPlayer(0, false);

						for (int i = 0; i < gameView.getAllCards(turn).size(); i++)
						{
							gameView.getAllCards(turn).get(i).addMouseListener(gameView.getAllCards(turn).get(i));
						}
					}

					if (turn == 0)
						turn = 1;
					else
						turn = 0;

					gameView.enableViewPlayer(0, true);
					gameView.enableViewPlayer(1, true);

					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					updateView(model.getPlayers().get(0), model.getPlayers().get(1));
				}
			}
		}).start();
	}

	public void initModel(String playerOneName, String playerTwoName)
	{
		Player playerOne = new Player(playerOneName, model.generateStartingCards(), 0);
		Player playerTwo = new Player(playerTwoName, model.generateStartingCards(), 1);

		model.initPlayer(playerOne);
		model.initPlayer(playerTwo);
	}

	public void updateView(Player playerOne, Player playerTwo)
	{
		gameView.loadCards(playerOne.getHandCards(), 0);
		gameView.loadCards(playerTwo.getHandCards(), 1);
	}

	public void initView()
	{
		gameView.setVisible(true);
		gameView.setResizable(false);
		gameView.loadCards(model.getPlayers().get(0).getHandCards(), 0);
		gameView.loadCards(model.getPlayers().get(1).getHandCards(), 1);
	}
}
