package unibs.pajc.uno.controller;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import unibs.pajc.uno.controller.ai.AI;
import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.CardView;
import unibs.pajc.uno.view.DialogSelectNewColor;
import unibs.pajc.uno.view.PlayerDetailsInfoSinglePlayer;
import unibs.pajc.uno.view.TableView;
import unibs.pajc.uno.view.events.CardDrawnEvent;
import unibs.pajc.uno.view.events.CardSelectedEvent;

/**
 * Classe di gestione del controller per il gioco in single player con
 * l'intelligenza artificiale
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
public class AIPlayerController extends PlayerController
{
	private AI ai;
	private String playerOneName;
	private String playerTwoName;

	public AIPlayerController(String playerOneName, String playerTwoName)
	{
		this.playerOneName = playerOneName;
		this.playerTwoName = playerTwoName;

		// INITS MODEL
		model = new GameModel();
		initModel(playerOneName, playerTwoName);

		// INITS VIEW
		view = new TableView(playerOneName, playerTwoName, true);
		initView();

		// START RUNNING GAME
		executor = Executors.newCachedThreadPool();
		executor.execute(this::runGame);
	}

	/**
	 * Inizializza la view, creando l'interfaccia del tavolo e impostando tutte le
	 * variabili necessarie a visualizzare correttamente la view
	 */
	protected void initView()
	{
		// SHOWS GUI
		view.setVisible(true);
		view.setResizable(false);
		view.setLocationRelativeTo(null);

		// CREATES ACTION LISTER TO DRAW A CARD
		mouseListenerDrawnCard = new CardDrawnEvent(notifyObj);
		view.getCardDeckView().addMouseListener(mouseListenerDrawnCard);

		updateView();
	}

	/**
	 * Inizializza il model del gioco
	 * 
	 * @param namePlayerOne nome del primo player
	 * @param namePlayerTwo nome del secondo player
	 */
	protected void initModel(String namePlayerOne, String namePlayerTwo)
	{
		Player playerOne = new Player(namePlayerOne, model.generateStartingCards(), 0);
		Player playerTwo = new Player(namePlayerTwo, model.generateStartingCards(), 1);

		model.initPlayer(playerOne);
		model.initPlayer(playerTwo);

		ai = new AI(model);
	}

	/*
	 * Aggiorna la view con gli ultimi componenti grafici
	 */
	protected void updateView()
	{
		// SETS TURN AND LOADS CARDS
		view.setTurn(model.getCurrentPlayer().getNamePlayer());
		view.loadCards(model.getPlayers().get(0).getHandCards(), 0);
		view.loadCardsAdversary(model.getPlayers().get(1).getHandCards().getNumberOfCards());

		view.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());

		// DISABLE BUTTONS
		view.setSayUnoButtonVisibile(false, model.getCurrentPlayerIndex());

		// GETS ALL CARDS VIEWS FROM GAMEVIEW
		ArrayList<CardView> panelPlayerOneCards = view.getAllCards(0,
				model.getPlayers().get(0).getHandCards().getNumberOfCards());

		// ADD MOUSE LISTENERS TO CARDS
		mouseListener = new CardSelectedEvent(notifyObj);
		panelPlayerOneCards.forEach(e -> e.addMouseListener(mouseListener));

		// ENABLES / DISABLE VIEW FOR PLAYERS
		view.enableViewPlayer(0, model.getCurrentPlayerIndex() == 0);

		// CHECKS IF UNO BUTTON SHOULD BE ENABLED
		view.setSayUnoButtonVisibile(
				model.hasPlayerOneCard(model.getCurrentPlayer()) && model.getCurrentPlayerIndex() == 0,
				model.getCurrentPlayerIndex());

		// REPAINTS VIEW
		view.repaint();
	}

	/**
	 * 
	 */
	protected void runGame()
	{
		while (!model.isGameOver())
		{
			if (model.getCurrentPlayerIndex() == 0)
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
			}
			else
			{
				ai.determineNexMossa(model);
				this.model = ai.getModel();
				view.setUnoButtonPressed(true);

				view.repaint();
				try
				{
					Thread.sleep(2000);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			updateView();

			mouseListener.setCardSelectedNull();
			mouseListenerDrawnCard.setCardDrawn(false);
		}

		gameOver();
	}

	/**
	 * 
	 */
	protected void playerDrawCard()
	{
		// CHECK IF PLAYER SAID UNO
		model.getCurrentPlayer().addCard(model.getCardFromDeck());
		model.nextTurn();
	}

	/**
	 * 
	 */
	protected void playerSelectedCard()
	{
		// CHECK IF PLAYER SAID UNO

		cardSelected = mouseListener.getCardSelected();

		if (model.isPlacedCardValid(cardSelected))
		{
			view.changeDroppedCardView(cardSelected, model.getCurrentCardColor());

			// CHECKS IF A NEW COLOR SELECTION IS NEEDED
			boolean newColorSelection = model.evalMossa(cardSelected);

			if (newColorSelection)
			{
				boolean colorSelectionValid = false;

				while (colorSelectionValid == false)
				{
					try
					{
						DialogSelectNewColor dialogColor = new DialogSelectNewColor(view);
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
	 * Controlla che un giocatore abbia detto uno. Se il giocatore non ha detto uno,
	 * allora vengono aggiunte due carte al gicoatore che non ha detto uno.
	 */
	protected void checkPlayerUno()
	{
		// PLAYER DID NOT SAY UNO
		if (model.hasPlayerOneCard(model.getPreviousPlayer()) && !view.isUnoButtonPressed())
		{
			JOptionPane.showMessageDialog(view, "You didn't say UNO! Two more cards for you.");
			model.playerDidNotSayUno(model.getPreviousPlayerIndex());
		}
		// PLAYER SAID UNO
		if (model.hasPlayerOneCard(model.getPreviousPlayer()) && view.isUnoButtonPressed())
		{
			view.setUnoButtonPressed(false);
		}
	}

	/**
	 * Mostra i risultati di fine gioco. Viene visualizzato un pannello con il nome
	 * del giocatore vincitore.
	 */
	protected void gameOver()
	{
		JOptionPane.showMessageDialog(null, model.getWinnerPlayer().getNamePlayer()
				+ " vincitore! Congratulazioni! Non hai vinto assolutamente nulla, se non un briciolo di misera gloria!");

		int selection = JOptionPane.showOptionDialog(view, "Do you want to rematch?", "Select an option.",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
				new String[] { "Yes", "No", "Yes, but with new players" }, "No");

		switch (selection)
		{
		case 0:
			view.dispose();
			new AIPlayerController(playerOneName, playerTwoName);
			break;
		case 1:
			view.dispose();
			System.exit(0);
			break;
		case 2:
			view.dispose();
			new PlayerDetailsInfoSinglePlayer().setVisible(true);
			break;
		}
	}
}
