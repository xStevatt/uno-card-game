package unibs.pajc.uno.model;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.Deck;
import unibs.pajc.uno.model.player.Player;

public class GameModel
{
	private Deck cardsDeck;
	private Player player1;
	private Player player2;

	/**
	 * Generates a starting set of cards and returns it. Number of cards is passed
	 * as an argument.
	 * 
	 * @param startingCards
	 */
	public void generateStartingCards(int startingCards)
	{
		Card[] card = new Card[startingCards];

		for (int i = 0; i < startingCards; i++)
		{

		}
	}

	/**
	 * Generates a starting set of card and returns it Number of cards is not passed
	 * as an argument and is set automatically to default
	 */
	public void generateStartingCards()
	{

	}
}
