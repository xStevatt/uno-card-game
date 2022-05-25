package unibs.pajc.uno.model;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.Deck;
import unibs.pajc.uno.model.player.Player;

public class GameModel
{
	private Deck cardsDeck;
	private Player players[];

	/**
	 * Constructor that allows to set a custom number of players
	 * 
	 * @param numberOfPlayers the number of players set by the user
	 */
	public GameModel(int numberOfPlayers)
	{
		players = new Player[numberOfPlayers];
		initGameElements();
	}

	/**
	 * Constructor to automatically set the default number of players
	 */
	public GameModel()
	{
		players = new Player[GameRules.DEFAULT_NUMBER_OF_PLAYERS];
		initGameElements();
	}

	/**
	 * Method that initializes all the items necessary: deck, starting hand cards
	 */
	public void initGameElements()
	{
		cardsDeck = new Deck();
		cardsDeck.initCardDeck();
	}

	/**
	 * Method returns a card from the deck. The card is drawn from the deck. After
	 * the card is drawn, it's removed from the deck
	 * 
	 * @return the card that was drawn by the deck
	 */
	public Card drawCardFromDeck()
	{
		return cardsDeck.getRandomCard();
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
	 * 
	 * @param player is the identifier of the player
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
