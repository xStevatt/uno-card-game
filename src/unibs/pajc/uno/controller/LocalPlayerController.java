
package unibs.pajc.uno.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
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
import unibs.pajc.uno.view.PlayerDetailsInfoOffline;
import unibs.pajc.uno.view.TableView;

public class LocalPlayerController
{
	private TableView gameView;
	private GameModel model;
	private ExecutorService executor;

	public LocalPlayerController(String playerOneName, String playerTwoName)
	{
		// INITS MODEL
		model = new GameModel();
		initModel(playerOneName, playerTwoName);

		// INITS VIEW
		gameView = new TableView(playerOneName, playerTwoName, true);
		initView();

		// START RUNNING GAME
		executor = Executors.newCachedThreadPool();
		executor.execute(this::runGame);
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
		// SHOWS GUI
		gameView.setVisible(true);
		gameView.setResizable(false);
		gameView.setLocationRelativeTo(null);

		// LOADS GAME CARDS
		gameView.loadCards(model.getPlayers().get(0).getHandCards(), 0);
		gameView.loadCards(model.getPlayers().get(1).getHandCards(), 1);

		// LOADS LAST CARD DROPPED
		gameView.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());
	}

	public void updateView(Player playerOne, Player playerTwo)
	{
		gameView.loadCards(playerOne.getHandCards(), 0);
		gameView.loadCards(playerTwo.getHandCards(), 1);

		ArrayList<CardView> panelPlayerOne = gameView.getAllCards(0,
				model.getPlayers().get(0).getHandCards().getNumberOfCards());
		ArrayList<CardView> panelPlayerTwo = gameView.getAllCards(1,
				model.getPlayers().get(1).getHandCards().getNumberOfCards());

		panelPlayerOne.forEach(e -> e.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseReleased(MouseEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if ((CardView) e.getSource() != null)
				{
					System.out.println(((CardView) e.getSource()).getCard().getCardColor());
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				// TODO Auto-generated method stub

			}
		}));

		gameView.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());
		gameView.repaint();
	}

	public void runGame()
	{
		while (!model.isGameOver())
		{
			// SETTING TURN LABEL
			gameView.setTurn(model.getCurrentPlayer().getNamePlayer());
			updateView(model.getPlayers().get(0), model.getPlayers().get(1));

			if (model.getCurrentPlayer().getIndex() == 0)
			{
				checkPlayerSaidUno();
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
						if (model.hasPlayerOneCard() && !gameView.isUnoButtonPressed())
						{
							JOptionPane.showMessageDialog(gameView, "You didn't say UNO! Two more cards for you.");
							model.playerDidNotSayUno(model.getCurrentPlayerIndex());
						}

						if (model.isPlacedCardValid(CardView.cardSelected))
						{
							gameView.changeDroppedCardView(CardView.cardSelected, model.getCurrentCardColor());
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
					if (CardBackView.isCardDrawnFromDeck == true
							&& model.getCurrentPlayer().getHandCards().getNumberOfCards() < 30)
					{
						if (model.hasPlayerOneCard() && !gameView.isUnoButtonPressed())
						{
							JOptionPane.showMessageDialog(gameView, "You didn't say UNO! Two more cards for you.");
							model.playerDidNotSayUno(model.getCurrentPlayerIndex());
						}

						model.getCurrentPlayer().addCard(model.getCardFromDeck());
						model.nextTurn();
					}
					else if (CardBackView.isCardDrawnFromDeck == true
							&& model.getCurrentPlayer().getHandCards().getNumberOfCards() >= 30)
					{
						JOptionPane.showMessageDialog(gameView, "Hai già troppe carte!");
					}
				}
			}
			if (model.getCurrentPlayer().getIndex() == 1)
			{
				checkPlayerSaidUno();
				gameView.enableViewPlayer(0, false);
				gameView.enableViewPlayer(1, true);

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
						if (model.hasPlayerOneCard() && !gameView.isUnoButtonPressed())
						{
							JOptionPane.showMessageDialog(gameView, "You didn't say UNO! Two more cards for you.");

							model.playerDidNotSayUno(model.getCurrentPlayerIndex());
						}

						if (model.isPlacedCardValid(CardView.cardSelected))
						{
							if (model.hasPlayerOneCard() && gameView.isUnoButtonPressed())
							{
								gameView.changeDroppedCardView(CardView.cardSelected, model.getCurrentCardColor());

								boolean newColorSelection = model.evalMossa(CardView.cardSelected);

								if (newColorSelection)
								{
									DialogSelectNewColor dialogColor = new DialogSelectNewColor();
									CardColor cardColor = dialogColor.show();
									model.setCurrentCardColor(cardColor);
								}

								gameView.setUnoButtonPressed(false);
							}
							else if (!model.hasPlayerOneCard())
							{
								gameView.changeDroppedCardView(CardView.cardSelected, model.getCurrentCardColor());

								boolean newColorSelection = model.evalMossa(CardView.cardSelected);

								if (newColorSelection)
								{
									DialogSelectNewColor dialogColor = new DialogSelectNewColor();
									CardColor cardColor = dialogColor.show();
									model.setCurrentCardColor(cardColor);
								}
								gameView.setUnoButtonPressed(false);
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Please select a valid card!", "Error",
									JOptionPane.ERROR_MESSAGE);
							CardView.isCardSelected = false;
						}
					}
					if (CardBackView.isCardDrawnFromDeck == true
							&& model.getCurrentPlayer().getHandCards().getNumberOfCards() < 30)
					{
						if (model.hasPlayerOneCard() && !gameView.isUnoButtonPressed())
						{
							JOptionPane.showMessageDialog(gameView, "You didn't say UNO! Two more cards for you.");
							model.playerDidNotSayUno(model.getCurrentPlayerIndex());
						}
						if (model.hasPlayerOneCard() && gameView.isUnoButtonPressed())
						{
							model.getCurrentPlayer().addCard(model.getCardFromDeck());
							model.nextTurn();
							gameView.setUnoButtonPressed(false);
						}
						model.getCurrentPlayer().addCard(model.getCardFromDeck());
						model.nextTurn();
						gameView.setUnoButtonPressed(false);
					}
					else if (CardBackView.isCardDrawnFromDeck == true
							&& model.getCurrentPlayer().getHandCards().getNumberOfCards() >= 30)
					{
						JOptionPane.showMessageDialog(gameView, "Hai già troppe carte!");
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

		int selection = JOptionPane.showOptionDialog(gameView, "Do you want to rematch?", "Select an option.",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
				new String[] { "Yes", "No", "Yes, but with new players" }, "No");

		switch (selection)
		{
		case 0:
			gameView.dispose();

			new LocalPlayerController(model.getPlayers().get(0).getNamePlayer(),
					model.getPlayers().get(1).getNamePlayer());
			break;
		case 1:
			gameView.dispose();
			System.exit(0);
			break;
		case 2:
			gameView.dispose();
			new PlayerDetailsInfoOffline().setVisible(true);
			break;
		}
	}

	/**
	 * 
	 */
	public void checkPlayerSaidUno()
	{
		if (model.hasPlayerOneCard(model.getCurrentPlayer()))
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					gameView.setSayUnoButtonVisibile(true, model.getCurrentPlayerIndex());
					gameView.repaint();
				}
			});
		}
	}
}