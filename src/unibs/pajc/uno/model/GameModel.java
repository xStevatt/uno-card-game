package unibs.pajc.uno.model;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.Deck;
import unibs.pajc.uno.model.player.Player;

public class GameModel
{
	private Deck cardsDeck;
	private Player players[];

	public GameModel(int numberOfPlayers)
	{
		players = new Player[numberOfPlayers];
	}

	/**
	 * Generates a starting set of cards and returns it. Number of cards is passed
	 * as an argument.
	 * 
	 * @param startingCards is the number of starting cards that we want
	 * @param player        is the index of the player the new set of cards is set
	 *                      for
	 */
	public void generateStartingCards(int startingCards, int player)
	{
		Card[] card = new Card[startingCards];

		for (int i = 0; i < startingCards; i++)
		{
			card[i] = cardsDeck.getRandomCard();
		}
	}

	/**
	 * Generates a starting set of card and returns it Number of cards is not passed
	 * as an argument and is set automatically to default
	 */
	public void generateStartingCards(int player)
	{
		Card[] card = new Card[GameRules.DEFAULT_NUMBER_OF_CARDS];

		for (int i = 0; i < GameRules.DEFAULT_NUMBER_OF_CARDS; i++)
		{
			card[i] = cardsDeck.getRandomCard();
		}
	}
}
