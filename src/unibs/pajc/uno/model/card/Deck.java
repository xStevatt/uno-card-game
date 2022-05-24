package unibs.pajc.uno.model.card;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Deck of cards class, a deck consists of 108 cards
 * 
 * @author Stefano Valloncini, Yuhang Ye, Luigi Amarante
 *
 */
public class Deck
{
	private static final int NUMBER_OF_ACTIONS_CARDS_PER_COLOR = 2;
	private static final int NUMBER_OF_WILD_CARDS = 4;
	private static final int DECK_CARD_NUMBER = 108;

	private ArrayList<Card> cardDeck;

	public Deck()
	{
		cardDeck = new ArrayList<>(DECK_CARD_NUMBER);
	}

	public void initCardDec()
	{
		initActionCards();
		initCardDec();
		initNumberCards();
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
		for (CardColor cardColor : CardColor.values())
		{
			for (var i = 0; i < NUMBER_OF_ACTIONS_CARDS_PER_COLOR; i++)
			{
				cardDeck.add(new ActionCard(CardType.SKIP, cardColor));
				cardDeck.add(new ActionCard(CardType.SKIP, cardColor));
				cardDeck.add(new ActionCard(CardType.SKIP, cardColor));
			}
		}
	}

	public void initWildCards()
	{
		for (int i = 0; i < NUMBER_OF_WILD_CARDS; i++)
		{
			cardDeck.add(new WildCard(CardType.WILD_COLOR));
			cardDeck.add(new WildCard(CardType.WILD_DRAW_FOUR));
		}
	}

	public void shuffleCards()
	{
		Collections.shuffle(cardDeck);
	}
}
