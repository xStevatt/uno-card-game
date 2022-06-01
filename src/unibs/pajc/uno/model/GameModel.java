package unibs.pajc.uno.model;

import java.util.ArrayList;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.card.CardDeck;
import unibs.pajc.uno.model.card.CardType;
import unibs.pajc.uno.model.card.NumberCard;
import unibs.pajc.uno.model.card.UsedPile;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.model.player.PlayerRoundIterator;

public class GameModel
{
	private CardDeck cardsDeck;
	private UsedPile usedCards;

	private ArrayList<Player> players;
	private int numberOfPlayers;
	private int maxNumberOfPlayers;
	private PlayerRoundIterator turnIterator;

	private CardColor currentCardColor;

	/**
	 * 
	 * @param players
	 */
	public GameModel(ArrayList<Player> players)
	{
		this.players = players;
		turnIterator = new PlayerRoundIterator(players);

		initGameElements();
	}

	/**
	 * Constructor that allows to set a custom number of players
	 * 
	 * @param numberOfPlayers the number of players set by the user
	 */
	public GameModel(int maxNumberOfPlayers)
	{
		this.maxNumberOfPlayers = maxNumberOfPlayers;
		players = new ArrayList<Player>(maxNumberOfPlayers);
		PlayerRoundIterator roundIterator = new PlayerRoundIterator(players);

		initGameElements();
	}

	/**
	 * Constructor to automatically set the default number of players
	 */
	public GameModel()
	{
		this(GameRules.DEFAULT_NUMBER_OF_PLAYERS);
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
		usedCards = new UsedPile(cardsDeck.getRandomCard());
	}

	/**
	 * Initializes the player without passing a Player object directly and creating
	 * it from name and cards
	 * 
	 * @param name  name of the cards
	 * @param cards the initial cards of the player
	 */
	public void initPlayer(String name, Card[] cards)
	{
		players.add(new Player(name, cards, numberOfPlayers));
		numberOfPlayers++;
	}

	/**
	 * 
	 * @param player
	 */
	public void initPlayer(Player player)
	{
		players.add(player);
		numberOfPlayers++;
	}

	/**
	 * 
	 * @param player
	 */
	public void initPlayer(ArrayList<Player> playersToAdd)
	{
		players.addAll(playersToAdd);
		numberOfPlayers += playersToAdd.size();
	}

	public Player getCurrentPlayer()
	{
		return turnIterator.getCurrentPlayer();
	}

	/**
	 * 
	 * @return
	 */
	public Card getCardFromDeck()
	{
		return cardsDeck.getRandomCard();
	}

	/**
	 * Boolean variable assignments were left for clarity
	 * 
	 * @param card
	 * @param index - if index is 1, then a new color must be chosen
	 */
	public boolean evalMossa(Card card, int index)
	{
		players.get(index).removeCard(card);
		usedCards.addCard(card);

		boolean newColorNeedsSelection = false;

		switch (card.getCardType())
		{
		case NUMBER:

			// NOTHING NEEDS TO BE DONE WHEN CARD PLACED IS A NUMBER
			newColorNeedsSelection = false;

			break;

		case WILD_COLOR:

			newColorNeedsSelection = true;

			break;

		case WILD_DRAW_FOUR:

			for (int i = 0; i < 4; i++)
			{
				turnIterator.next().addCard(getCardFromDeck());
			}

			newColorNeedsSelection = true;

			break;
		case WILD_DRAW_TWO:

			for (int i = 0; i < 2; i++)
			{
				turnIterator.next().addCard(getCardFromDeck());
			}

			newColorNeedsSelection = false;

			break;

		case SKIP:

			newColorNeedsSelection = false;

			break;

		case REVERSE:

			newColorNeedsSelection = false;

			break;
		}

		return newColorNeedsSelection;
	}

	/**
	 * Method that checks if the card that the user wants to place is valid. A card
	 * that needs to be placed must be the same color of the last card that was
	 * discarded in the used cards pile.
	 * 
	 * @return
	 */
	public boolean isPlacedCardValid(Card cardSelected, Card cardUsed)
	{
		boolean isCardValid = false;

		if (cardSelected.isCardSpecialWild() || cardUsed.isCardSpecialWild())
		{
			if (cardSelected.getCardColor() == currentCardColor)
				isCardValid = true;
		}

		if (cardSelected.getCardColor() == cardUsed.getCardColor())
		{
			isCardValid = true;
		}

		if (cardSelected.getCardType() == cardUsed.getCardType() && cardSelected.getCardType() != CardType.NUMBER)
		{
			isCardValid = true;
		}

		if (cardUsed.getCardType() == cardSelected.getCardType() && cardUsed.getCardType() == CardType.NUMBER
				&& ((NumberCard) cardSelected).getValue() == ((NumberCard) cardUsed).getValue())
		{
			isCardValid = true;
		}

		return isCardValid;
	}

	/**
	 * 
	 * @param cardSelected
	 * @return
	 */
	public boolean isPlacedCardValid(Card cardSelected)
	{
		return isPlacedCardValid(cardSelected, usedCards.getLastCardUsed());
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

		// Checks if there's actually a winning player
		for (Player player : players)
		{
			if (player.getHandCards().getNumberOfCards() == 0)
			{
				playerWinner = player;
			}
		}

		return playerWinner;
	}

	/**
	 * Returns if the game is over. If the game is over (a player has zero cards
	 * left), then true is returned, false otherwise.
	 * 
	 * @return true if game is over, false otherwise
	 */
	public boolean isGameOver()
	{
		boolean isGameOver = false;

		for (Player player : players)
		{
			isGameOver = player.getHandCards().getNumberOfCards() == 0;
		}

		return isGameOver;
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

	public Card getLastCardUsed()
	{
		return usedCards.getLastCardUsed();
	}

	public PlayerRoundIterator getTurnIterator()
	{
		return turnIterator;
	}

	public void setTurnIterator(PlayerRoundIterator turnIterator)
	{
		this.turnIterator = turnIterator;
	}

	public CardColor getCurrentCardColor()
	{
		return currentCardColor;
	}

	public void setCurrentCardColor(CardColor currentCardColor)
	{
		this.currentCardColor = currentCardColor;
	}

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

	public ArrayList<Player> getPlayers()
	{
		return players;
	}

	public void setPlayers(ArrayList<Player> players)
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
}
