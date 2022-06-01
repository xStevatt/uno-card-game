package unibs.pajc.uno.model.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import unibs.pajc.uno.model.GameRules;

/**
 * Deck of cards class, a deck consists of 108 cards
 * 
 * @author Stefano Valloncini, Yuhang Ye, Luigi Amarante
 *
 */
public class CardDeck
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
	}

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

	public void initWildCards()
	{
		for (int i = 0; i < GameRules.NUMBER_OF_WILD_CARDS; i++)
		{
			cardDeck.add(new WildCard(CardType.WILD_COLOR));
			cardDeck.add(new WildCard(CardType.WILD_DRAW_FOUR));
		}
	}

	public Card getRandomCard()
	{
		int randomIndex = new Random().nextInt(cardDeck.size());

		Card card = cardDeck.get(new Random().nextInt(cardDeck.size()));

		cardDeck.remove(randomIndex);

		return card;
	}

	public void shuffleCards()
	{
		Collections.shuffle(cardDeck);
	}
}
