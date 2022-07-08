package unibs.pajc.uno.controller;

import java.util.concurrent.ExecutorService;

import javax.swing.JOptionPane;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.view.DialogSelectNewColor;
import unibs.pajc.uno.view.PlayerDetailsInfoOffline;
import unibs.pajc.uno.view.TableView;
import unibs.pajc.uno.view.events.CardDrawnEvent;
import unibs.pajc.uno.view.events.CardSelectedEvent;

/**
 * Classe astratta per la gesione del
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
public abstract class PlayerController
{
	// GAME VIEW AND MODEL
	protected GameModel model;
	protected TableView view;

	// EXECUTOR SERVICE
	protected ExecutorService executor;

	// OBJECT TO NOTIFY THREADS WAITING
	protected Object notifyObj = new Object();

	// CARD SELECTED BY PLAYER
	protected Card cardSelected = null;

	// MOUSE LISTENERS
	protected CardSelectedEvent mouseListener;
	protected CardDrawnEvent mouseListenerDrawnCard;

	/**
	 * 
	 */
	protected abstract void initView();

	/**
	 * 
	 */
	protected abstract void updateView();

	/**
	 * 
	 */
	protected abstract void runGame();

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
	 * 
	 */
	protected void playerDrawCard()
	{
		model.getCurrentPlayer().addCard(model.getCardFromDeck());
		model.nextTurn();
	}

	/**
	 * 
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
	 * 
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

			new LocalPlayerController(model.getPlayers().get(0).getNamePlayer(),
					model.getPlayers().get(1).getNamePlayer());
			break;
		case 1:
			view.dispose();
			System.exit(0);
			break;
		case 2:
			view.dispose();
			new PlayerDetailsInfoOffline().setVisible(true);
			break;
		}
	}
}
