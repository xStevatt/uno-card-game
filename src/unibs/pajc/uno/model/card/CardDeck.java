package unibs.pajc.uno.model.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import unibs.pajc.uno.model.GameRules;

/**
 * 
 * Classe che definisce il mazzo di carte del gioco
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
public class CardDeck implements Serializable
{
	private ArrayList<Card> cardDeck;

	public CardDeck()
	{
		cardDeck = new ArrayList<>(GameRules.DECK_CARD_NUMBER);
		initCardDeck();
	}

	public void initCardDeck()
	{
		initActionCards();
		initNumberCards();
		initWildCards();

		shuffleCards();
	}

	/**
	 * Inits number cards in deck
	 */
	public void initNumberCards()
	{
		for (CardColor color : CardColor.values())
		{
			for (int i = 0; i <= 9; i++)
			{
				cardDeck.add(new NumberCard(color, i));
			}
		}
	}

	/**
	 * Inits action cards in deck
	 */
	public void initActionCards()
	{
		for (CardColor color : CardColor.values())
		{
			for (var i = 0; i < GameRules.NUMBER_OF_ACTIONS_CARDS_PER_COLOR; i++)
			{
				cardDeck.add(new ActionCard(CardType.SKIP, color));
				cardDeck.add(new ActionCard(CardType.REVERSE, color));
				cardDeck.add(new ActionCard(CardType.WILD_DRAW_TWO, color));
			}
		}
	}

	/**
	 * Inits wild cards in deck
	 */
	public void initWildCards()
	{
		for (int i = 0; i < GameRules.NUMBER_OF_WILD_CARDS; i++)
		{
			cardDeck.add(new WildCard(CardType.WILD_COLOR));
			cardDeck.add(new WildCard(CardType.WILD_DRAW_FOUR));
		}
	}

	/**
	 * Retuns a random card from the deck. If the deck doesn't have any more cards,
	 * the deck is resetted
	 * 
	 * @return a random card from the deck
	 */
	public Card getRandomCard()
	{
		Card cardSelected = null;

		if (cardDeck.size() > 0)
		{
			int randomIndex = new Random().nextInt(cardDeck.size());

			cardSelected = cardDeck.get(new Random().nextInt(cardDeck.size()));

			cardDeck.remove(randomIndex);
		}
		else
		{
			initCardDeck();
			shuffleCards();

			int randomIndex = new Random().nextInt(cardDeck.size());

			cardSelected = cardDeck.get(new Random().nextInt(cardDeck.size()));

			cardDeck.remove(randomIndex);
		}

		return cardSelected;
	}

	/**
	 * Shuffle cards in the deck
	 */
	public void shuffleCards()
	{
		Collections.shuffle(cardDeck);
	}

	/**
	 * Returns the number of cards in the deck
	 * 
	 * @return the number of cards in the deck
	 */
	public int numberOfCards()
	{
		return cardDeck.size();
	}
}
