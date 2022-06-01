
package unibs.pajc.uno.controller;

import javax.swing.JOptionPane;

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
		gameView = new TableView(playerOneName, playerTwoName, true);
		initView();

		// START RUNNING GAME
		runGame();
	}

	public void initModel(String playerOneName, String playerTwoName)
	{
		Player playerOne = new Player(playerOneName, model.generateStartingCards(), 0);
		Player playerTwo = new Player(playerTwoName, model.generateStartingCards(), 1);

		model.initPlayer(playerOne);
		model.initPlayer(playerTwo);
	}

	public void initView()
	{
		gameView.setVisible(true);
		gameView.setResizable(false);
		gameView.loadCards(model.getPlayers().get(0).getHandCards(), 0);
		gameView.loadCards(model.getPlayers().get(1).getHandCards(), 1);
		gameView.changeDroppedCardView(model.getCardFromDeck());

		gameView.setLocationRelativeTo(null);
	}

	public void updateView(Player playerOne, Player playerTwo)
	{
		gameView.loadCards(playerOne.getHandCards(), 0);
		gameView.loadCards(playerTwo.getHandCards(), 1);
		System.out.println("Last card used: " + model.getLastCardUsed());
		gameView.changeDroppedCardView(model.getLastCardUsed());
	}

	public void runGame()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				int turn = 0;

				while (model.isGameOver() == false)
				{
					if (turn == 0)
					{
						gameView.enableViewPlayer(1, false);

						while (CardView.isCardSelected == false && CardBackView.isCardDrawnFromDeck == false)
						{
							try
							{
								Thread.sleep(500);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}

							if (CardView.isCardSelected == true)
							{
								if (model.isPlacedCardValid(CardView.cardSelected))
								{
									model.evalMossa(CardView.cardSelected, turn);
									gameView.changeDroppedCardView(CardView.cardSelected);
								}
								else
								{
									JOptionPane.showMessageDialog(null, "Please select a valid card!", "Error",
											JOptionPane.ERROR_MESSAGE);
								}
							}
							if (CardBackView.isCardDrawnFromDeck == true)
							{
								model.getPlayers().get(turn).addCard(model.getCardFromDeck());
							}
						}
					}
					if (turn == 1)
					{
						gameView.enableViewPlayer(0, false);
						gameView.enableViewPlayer(1, true);
					}

					CardView.isCardSelected = false;
					CardBackView.isCardDrawnFromDeck = false;

					gameView.enableViewPlayer(0, false);
					gameView.enableViewPlayer(1, false);

					updateView(model.getPlayers().get(0), model.getPlayers().get(1)); // UP. VIEW AFTER MODEL
				}

				JOptionPane.showMessageDialog(null, model.getWinnerPlayer().getNamePlayer() + "vincitore");
			}
		}).start();
	}
}