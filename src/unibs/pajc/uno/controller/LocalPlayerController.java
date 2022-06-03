
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
				while (!model.isGameOver())
				{
					// SETTING TURN LABEL
					gameView.setTurn(model.getCurrentPlayer().getNamePlayer());

					if (model.hasPlayerOneCard(model.getPlayers().get(model.getPreviousPlayerIndex())))
					{
						gameView.setSayUnoButtonVisibile(true, model.getPreviousPlayerIndex());
					}

					if (model.getCurrentPlayer().getIndex() == 0)
					{
						gameView.enableViewPlayer(0, true);
						gameView.enableViewPlayer(1, false);

						// LOOP WAITING FOR A CARD TO BE SELECTED
						while (CardView.isCardSelected == false && CardBackView.isCardDrawnFromDeck == false)
						{
							try
							{
								Thread.sleep(1);
							}
							catch (InterruptedException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							if (CardView.isCardSelected == true)
							{
								if (model.isPlacedCardValid(CardView.cardSelected))
								{
									if (model.hasPlayerOneCard(model.getPlayers().get(model.getCurrentPlayerIndex()))
											&& gameView.isUnoButtonPressed() == false)
									{
										model.playerDidNotSayUno(model.getCurrentPlayerIndex());
									}

									gameView.setLastUsedCard(CardView.cardSelected);
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

						if (model.hasPlayerOneCard(model.getPlayers().get(model.getPreviousPlayerIndex())))
						{
							gameView.setSayUnoButtonVisibile(
									model.hasPlayerOneCard(model.getPlayers().get(model.getPreviousPlayerIndex())),
									model.getPreviousPlayerIndex());
						}

						while (CardView.isCardSelected == false && CardBackView.isCardDrawnFromDeck == false)
						{
							// THREAD SLEEP
							try
							{
								Thread.sleep(1);
							}
							catch (InterruptedException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							if (CardView.isCardSelected == true)
							{
								if (model.isPlacedCardValid(CardView.cardSelected))
								{
									gameView.setLastUsedCard(CardView.cardSelected);
									boolean newColorSelection = model.evalMossa(CardView.cardSelected);

									if (model.hasPlayerOneCard(model.getPlayers().get(model.getCurrentPlayerIndex()))
											&& gameView.isUnoButtonPressed() == false)
									{
										model.playerDidNotSayUno(model.getCurrentPlayerIndex());
									}

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

					// DISABLING BUTTONS
					gameView.setSayUnoButtonVisibile(false, model.getPreviousPlayerIndex());
					gameView.setSayUnoButtonVisibile(false, model.getCurrentPlayerIndex());

					// RESETTING FLAGS
					CardView.isCardSelected = false;
					CardBackView.isCardDrawnFromDeck = false;

					// RESETS GAME VIEW
					gameView.enableViewPlayer(0, false);
					gameView.enableViewPlayer(1, false);

					// UPDATES VIEW
					updateView(model.getPlayers().get(0), model.getPlayers().get(1));
				}

				JOptionPane.showMessageDialog(null, model.getWinnerPlayer().getNamePlayer()
						+ " vincitore! Congratulazioni! Non hai vinto assolutamente nulla, se non un briciolo di misera gloria!");

				gameView.dispose();
			}
		}).start();
	}
}