package unibs.pajc.uno.model;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardDeck;
import unibs.pajc.uno.model.card.NumberCard;
import unibs.pajc.uno.model.card.UsedPile;
import unibs.pajc.uno.model.card.WildCard;
import unibs.pajc.uno.model.player.Player;

public class GameModel
{
	private CardDeck cardsDeck;
	private UsedPile usedCards;
	private Player players[];
	private int maxNumberOfPlayers;
	private int numberOfPlayers = 0;
	private boolean gameOver = false;

	/**
	 * Constructor that allows to set a custom number of players
	 * 
	 * @param numberOfPlayers the number of players set by the user
	 */
	public GameModel(int maxNumberOfPlayers)
	{
		this.maxNumberOfPlayers = maxNumberOfPlayers;
		players = new Player[maxNumberOfPlayers];
		initGameElements();
	}

	/**
	 * Constructor to automatically set the default number of players
	 */
	public GameModel()
	{
		this.maxNumberOfPlayers = GameRules.DEFAULT_NUMBER_OF_PLAYERS;
		players = new Player[GameRules.DEFAULT_NUMBER_OF_PLAYERS];
		initGameElements();
	}

	/**
	 * Method that initializes all the items necessary: deck, discarded cards deck,
	 * and starting hand cards. Instead of using a specific class for the discarded
	 * cards, it would've been enough to used a variable Card to store just the last
	 * card used
	 */
	public void initGameElements()
	{
		cardsDeck = new CardDeck();
		cardsDeck.initCardDeck();

		usedCards = new UsedPile(cardsDeck.getRandomCard());
	}

	public void initPlayer(String name, Card[] cards)
	{
		if (numberOfPlayers < maxNumberOfPlayers - 1)
		{
			players[numberOfPlayers] = new Player(name, cards, numberOfPlayers);
			numberOfPlayers++;
			System.out.println("Player added");
		}
	}

	public void initPlayer(Player player)
	{
		if (numberOfPlayers < maxNumberOfPlayers - 1)
		{
			players[numberOfPlayers] = player;
			numberOfPlayers++;
			System.out.print("Player added: " + player.getNamePlayer() + " , index: " + numberOfPlayers);
		}
	}

	/**
	 * 
	 */
	public void determineEffectsOfCard(Card card, int index)
	{
		switch (card.getCardType())
		{
		case NUMBER:
			break;
		case WILD_COLOR:

			break;
		case WILD_DRAW_FOUR:
			break;
		case WILD_DRAW_TWO:
			break;
		case SKIP:
			break;
		case REVERSE:
			break;
		}
	}

	/**
	 * Method that checks if the card that the user wants to place is valid. A card
	 * that needs to be placed must be the same color of the last card that was
	 * discarded in the used cards pile.
	 * 
	 * @return
	 */
	public boolean isPlacedCardValid(Card card)
	{
		boolean isCardValid = true;

		if (card instanceof WildCard)
		{
			return isCardValid;
		}
		else
		{
			if ((usedCards.getLastCardUsed().getCardColor() == card.getCardColor()))
			{
				return isCardValid;
			}
			else if ((card instanceof NumberCard && usedCards.getLastCardUsed() instanceof NumberCard)
					&& ((NumberCard) card).getValue() == ((NumberCard) usedCards.getLastCardUsed()).getValue())
			{
				return isCardValid;
			}
		}

		return !isCardValid;
	}

	/**
	 * Checks if the game is over. The game is over if a player has 0 cards.
	 * 
	 * @return the function returns a player if a player has zero cards, otherwise
	 *         it returns null
	 */
	public Player getWinnerPlayer()
	{
		Player playerWinner = null;

		for (Player player : players)
		{
			if (player.getHandCards().getNumberOfCards() == 0)
			{
				playerWinner = player;
			}
		}

		return playerWinner;
	}

	public boolean isGameOver()
	{
		for (int i = 0; i < numberOfPlayers; i++)
		{
			if (players[i].getHandCards().getNumberOfCards() == 0)
			{
				this.gameOver = true;
			}
		}

		return this.gameOver;
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
	 * Generates a starting set of card and returns it. Number of cards is not
	 * passed as an argument and is set automatically to default, constant is used
	 * directly
	 * 
	 * @param player is the identifier of the player
	 */
	public Card[] generateStartingCards()
	{
		Card[] card = new Card[GameRules.DEFAULT_NUMBER_OF_CARDS];

		for (int i = 0; i < GameRules.DEFAULT_NUMBER_OF_CARDS; i++)
		{
			card[i] = cardsDeck.getRandomCard();
		}

		return card;
	}

	/**
	 * Generates a starting set of cards and returns it. Number of cards is passed
	 * as an argument.
	 * 
	 * @param startingCards is the number of starting cards that we want
	 * @param player        is the index of the player the new set of cards is set
	 *                      for
	 */
	public void generateStartingCards(int startingCards)
	{
		Card[] card = new Card[startingCards];

		for (int i = 0; i < startingCards; i++)
		{
			card[i] = cardsDeck.getRandomCard();
		}
	}

	// GETTERS AND SETTERS

	public CardDeck getCardsDeck()
	{
		return cardsDeck;
	}

	public void setCardsDeck(CardDeck cardsDeck)
	{
		this.cardsDeck = cardsDeck;
	}

	public UsedPile getUsedCards()
	{
		return usedCards;
	}

	public void setUsedCards(UsedPile usedCards)
	{
		this.usedCards = usedCards;
	}

	public Player[] getPlayers()
	{
		return players;
	}

	public void setPlayers(Player[] players)
	{
		this.players = players;
	}

	public int getMaxNumberOfPlayers()
	{
		return maxNumberOfPlayers;
	}

	public void setMaxNumberOfPlayers(int maxNumberOfPlayers)
	{
		this.maxNumberOfPlayers = maxNumberOfPlayers;
	}

	public int getNumberOfPlayers()
	{
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers)
	{
		this.numberOfPlayers = numberOfPlayers;
	}

	public void setGameOver(boolean gameOver)
	{
		this.gameOver = gameOver;
	}
}
