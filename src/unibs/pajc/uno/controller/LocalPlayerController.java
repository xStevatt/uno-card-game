package unibs.pajc.uno.controller;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.CardView;
import unibs.pajc.uno.view.TableView;
import unibs.pajc.uno.view.events.CardDrawnEvent;
import unibs.pajc.uno.view.events.CardSelectedEvent;

/**
 * Classe di gestione del controller per il gioco in multiplayer locale
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
public class LocalPlayerController extends PlayerController
{
	public LocalPlayerController(String playerOneName, String playerTwoName)
	{
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
	 * Metodo per inizializzare gli elementi del model necessari per iniziare il
	 * gioco in multiplayer in locale sullo stesso computer
	 * 
	 * @param playerOneName
	 * @param playerTwoName
	 */
	protected void initModel(String playerOneName, String playerTwoName)
	{
		Player playerOne = new Player(playerOneName, model.generateStartingCards(), 0);
		Player playerTwo = new Player(playerTwoName, model.generateStartingCards(), 1);

		model.initPlayer(playerOne);
		model.initPlayer(playerTwo);
	}

	@Override
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
	 * Aggionra la view con tutti gli elementi aggiornati
	 */
	@Override
	protected void updateView()
	{
		SwingUtilities.invokeLater(() -> {

			// SETS TURN AND LOADS CARDS
			view.setTurn(model.getCurrentPlayer().getNamePlayer());
			view.loadCards(model.getPlayers().get(0).getHandCards(), 0);
			view.loadCards(model.getPlayers().get(1).getHandCards(), 1);
			view.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());

			// DISABLE BUTTONS
			view.setSayUnoButtonVisibile(false, model.getPreviousPlayerIndex());
			view.setSayUnoButtonVisibile(false, model.getCurrentPlayerIndex());

			// GETS ALL CARDS VIEWS FROM GAMEVIEW
			ArrayList<CardView> panelPlayerOneCards = view.getAllCards(0,
					model.getPlayers().get(0).getHandCards().getNumberOfCards());
			ArrayList<CardView> panelPlayerTwoCards = view.getAllCards(1,
					model.getPlayers().get(1).getHandCards().getNumberOfCards());

			// ADD MOUSE LISTENERS TO CARDS
			mouseListener = new CardSelectedEvent(notifyObj);
			panelPlayerOneCards.forEach(e -> e.addMouseListener(mouseListener));
			panelPlayerTwoCards.forEach(e -> e.addMouseListener(mouseListener));

			// ENABLES / DISABLE VIEW FOR PLAYERS
			view.enableViewPlayer(model.getCurrentPlayerIndex(), true);
			view.enableViewPlayer(model.getNextPlayerIndex(), false);

			// CHECKS IF UNO BUTTON SHOULD BE ENABLED
			view.setSayUnoButtonVisibile(model.hasPlayerOneCard(model.getCurrentPlayer()),
					model.getCurrentPlayerIndex());

			// REPAINTS VIEW
			view.repaint();
		});
	}

	@Override
	protected void runGame()
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
						else
						{
							JOptionPane.showMessageDialog(null, "Non puoi pescare ulteriori carte!");
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
}