
package unibs.pajc.uno.controller;

import javax.swing.JOptionPane;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.CardBackView;
import unibs.pajc.uno.view.CardView;
import unibs.pajc.uno.view.DialogSelectNewColor;
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

		// LOADS GAME CARDS
		gameView.loadCards(model.getPlayers().get(0).getHandCards(), 0);
		gameView.loadCards(model.getPlayers().get(1).getHandCards(), 1);

		// LOADS LAST CARD DROPPED
		gameView.changeDroppedCardView(model.getLastCardUsed());

		gameView.setLocationRelativeTo(null);
	}

	public void updateView(Player playerOne, Player playerTwo)
	{
		gameView.loadCards(playerOne.getHandCards(), 0);
		gameView.loadCards(playerTwo.getHandCards(), 1);

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
					if (model.getCurrentPlayer().getIndex() == 0)
					{
						gameView.enableViewPlayer(0, true);
						gameView.enableViewPlayer(1, false);

						while (CardView.isCardSelected == false && CardBackView.isCardDrawnFromDeck == false)
						{
							try
							{
								Thread.sleep(1);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}

							if (CardView.isCardSelected == true)
							{
								if (model.isPlacedCardValid(CardView.cardSelected))
								{
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
									JOptionPane.showMessageDialog(null, "Please select a valid card!", "Error",
											JOptionPane.ERROR_MESSAGE);
									CardView.isCardSelected = false;
								}
							}
							if (CardBackView.isCardDrawnFromDeck == true)
							{
								model.getCurrentPlayer().addCard(model.getCardFromDeck());
								model.nextTurn();
							}
						}
					}
					if (model.getCurrentPlayer().getIndex() == 1)
					{
						gameView.enableViewPlayer(0, false);
						gameView.enableViewPlayer(1, true);

						while (CardView.isCardSelected == false && CardBackView.isCardDrawnFromDeck == false)
						{
							try
							{
								Thread.sleep(1);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
							if (CardView.isCardSelected == true)
							{
								if (model.isPlacedCardValid(CardView.cardSelected))
								{
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
									JOptionPane.showMessageDialog(null, "Please select a valid card!", "Error",
											JOptionPane.ERROR_MESSAGE);
									CardView.isCardSelected = false;
								}
							}
							if (CardBackView.isCardDrawnFromDeck == true)
							{
								model.getCurrentPlayer().addCard(model.getCardFromDeck());
								model.nextTurn();
							}
						}
					}

					// CHECKS NUMBER CARDS
					if (model.getCurrentPlayer().getHandCards().getNumberOfCards() == 1)
					{
						gameView.setSayUnoButtonVisibile(true, model.getCurrentPlayer().getIndex());
					}

					// RESETS FLAGS
					CardView.isCardSelected = false;
					CardBackView.isCardDrawnFromDeck = false;

					// RESETS GAME VIEW
					gameView.enableViewPlayer(0, false);
					gameView.enableViewPlayer(1, false);

					// UPDATES VIEW
					updateView(model.getPlayers().get(0), model.getPlayers().get(1));
				}

				JOptionPane.showMessageDialog(null, model.getWinnerPlayer().getNamePlayer() + "vincitore");
			}
		}).start();
	}
}