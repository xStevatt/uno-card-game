package unibs.pajc.uno.controller;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.CardView;
import unibs.pajc.uno.view.DialogSelectNewColor;
import unibs.pajc.uno.view.PlayerDetailsInfoOffline;
import unibs.pajc.uno.view.TableView;
import unibs.pajc.uno.view.events.CardDrawnEvent;
import unibs.pajc.uno.view.events.CardSelectedEvent;

public class LocalPlayerController
{
	private TableView gameView;
	private GameModel model;
	private ExecutorService executor;

	private Object notifyObj = new Object();

	private Card cardSelected = null;

	private CardSelectedEvent mouseListener;
	private CardDrawnEvent mouseListenerDrawnCard;

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

		// CREATES ACTION LISTER TO DRAW A CARD
		mouseListenerDrawnCard = new CardDrawnEvent(notifyObj);
		gameView.getCardDeckView().addMouseListener(mouseListenerDrawnCard);

		updateView();
	}

	/**
	 * Updates game view, loading all the new data available and setting up
	 */
	public synchronized void updateView()
	{
		SwingUtilities.invokeLater(() -> {

			// SETS TURN AND LOADS CARDS
			gameView.setTurn(model.getCurrentPlayer().getNamePlayer());
			gameView.loadCards(model.getPlayers().get(0).getHandCards(), 0);
			gameView.loadCards(model.getPlayers().get(1).getHandCards(), 1);
			gameView.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());

			// DISABLE BUTTONS
			gameView.setSayUnoButtonVisibile(false, model.getPreviousPlayerIndex());
			gameView.setSayUnoButtonVisibile(false, model.getCurrentPlayerIndex());

			// GETS ALL CARDS VIEWS FROM GAMEVIEW
			ArrayList<CardView> panelPlayerOneCards = gameView.getAllCards(0,
					model.getPlayers().get(0).getHandCards().getNumberOfCards());
			ArrayList<CardView> panelPlayerTwoCards = gameView.getAllCards(1,
					model.getPlayers().get(1).getHandCards().getNumberOfCards());

			// ADD MOUSE LISTENERS TO CARDS
			mouseListener = new CardSelectedEvent(notifyObj);
			panelPlayerOneCards.forEach(e -> e.addMouseListener(mouseListener));
			panelPlayerTwoCards.forEach(e -> e.addMouseListener(mouseListener));

			// CHECKS THAT PLAYER HAS SAID UNO
			if (model.hasPlayerOneCard(model.getCurrentPlayer()))
			{
				gameView.setSayUnoButtonVisibile(true, model.getCurrentPlayerIndex());
				gameView.repaint();
			}

			// ENABLES / DISABLE VIEW FOR PLAYERS
			gameView.enableViewPlayer(model.getCurrentPlayerIndex(), true);
			gameView.enableViewPlayer(model.getNextPlayerIndex(), false);

			// CHECKS IF UNO BUTTON SHOULD BE ENABLED
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

			// REPAINTS VIEW
			gameView.repaint();
		});
	}

	public void runGame()
	{
		while (!model.isGameOver())
		{
			synchronized (notifyObj)
			{
				try
				{
					notifyObj.wait();

					checkPlayerUno();

					if (mouseListenerDrawnCard.isCardDrawn()
							&& model.getCurrentPlayer().getHandCards().getNumberOfCards() < 30)
					{
						if (model.getCurrentPlayer().getHandCards().getNumberOfCards() <= 30)
						{
							playerDrawCard();
						}
					}
					if (mouseListener.getCardSelected() != null)
					{
						playerSelectedCard();
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			updateView();

			mouseListener.setCardSelectedNull();
			mouseListenerDrawnCard.setCardDrawn(false);
		}

		gameOver();
	}

	public void playerDrawCard()
	{
		// CHECK IF PLAYER SAID UNO
		model.getCurrentPlayer().addCard(model.getCardFromDeck());
		model.nextTurn();
	}

	public void playerSelectedCard()
	{
		// CHECK IF PLAYER SAID UNO

		cardSelected = mouseListener.getCardSelected();

		if (model.isPlacedCardValid(cardSelected))
		{
			gameView.changeDroppedCardView(cardSelected, model.getCurrentCardColor());

			// CHECKS IF A NEW COLOR SELECTION IS NEEDED
			boolean newColorSelection = model.evalMossa(cardSelected);

			if (newColorSelection)
			{
				boolean colorSelectionValid = false;

				while (colorSelectionValid == false)
				{
					try
					{
						DialogSelectNewColor dialogColor = new DialogSelectNewColor(gameView);
						CardColor cardColor = dialogColor.show();
						model.setCurrentCardColor(cardColor);
						colorSelectionValid = true;
					}
					catch (NullPointerException e)
					{
						colorSelectionValid = false;
					}
				}
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please select a valid card!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 
	 */
	public void checkPlayerUno()
	{
		// PLAYER DID NOT SAY UNO
		if (model.hasPlayerOneCard(model.getPreviousPlayer()) && !gameView.isUnoButtonPressed())
		{
			JOptionPane.showMessageDialog(gameView, "You didn't say UNO! Two more cards for you.");
			model.playerDidNotSayUno(model.getPreviousPlayerIndex());
		}
		// PLAYER SAID UNO
		if (model.hasPlayerOneCard(model.getPreviousPlayer()) && gameView.isUnoButtonPressed())
		{
			gameView.setUnoButtonPressed(false);
		}
	}

	/**
	 * 
	 */
	public void checkEnableUnoButton()
	{

	}

	public void gameOver()
	{
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
}