package unibs.pajc.uno.controller;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.CardBackView;
import unibs.pajc.uno.view.CardView;
import unibs.pajc.uno.view.DialogWrongInput;
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
				int turn = 0;

				while (!model.isGameOver())
				{
					if (turn == 0)
					{
						gameView.enableViewPlayer(1, false);

						while (CardView.cardSelected == null && CardBackView.isCardDrawnFromDeck == false)
						{
							if (CardView.cardSelected != null)
							{
								System.out.println("HERE");
								if (model.isPlacedCardValid(CardView.cardSelected))
								{
									System.out.println("HERE");
								}
								else
								{
									new DialogWrongInput().setVisible(true);
								}
							}
							else if (CardBackView.isCardDrawnFromDeck == true)
							{

							}
						}
					}
					if (turn == 1)
					{

					}

					CardView.cardSelected = null; // RESETS CARD SELECTED
					CardBackView.isCardDrawnFromDeck = false; // RESETS IF THE CARD WAS DRAWN FROM DECK
					updateView(model.getPlayers().get(0), model.getPlayers().get(1)); // UP. VIEW AFTER MODEL EVAL
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
