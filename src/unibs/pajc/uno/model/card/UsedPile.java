package unibs.pajc.uno.model.card;

import java.io.Serializable;
import java.util.ArrayList;

public class UsedPile implements Serializable
{
	private ArrayList<Card> usedCards;

	public UsedPile(Card startingCard)
	{
		this.usedCards = new ArrayList<>();
		usedCards.add(startingCard);
	}

	public void addCard(Card card)
	{
		usedCards.add(card);
	}

	public Card getLastCardUsed()
	{
		return usedCards.get(usedCards.size() - 1);
	}
}
