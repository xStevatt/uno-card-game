package unibs.pajc.uno.model.card;

import java.util.ArrayList;

public class UsedPile
{
	private ArrayList<Card> usedCards;

	public UsedPile()
	{
		this.usedCards = new ArrayList<>();
	}

	public void addCard(Card card)
	{
		usedCards.add(card);
	}

	public Card getLastCardUsed()
	{
		return usedCards.get(usedCards.size());
	}
}
