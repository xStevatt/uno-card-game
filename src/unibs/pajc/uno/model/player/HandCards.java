package unibs.pajc.uno.model.player;

import java.util.ArrayList;
import java.util.Arrays;

import unibs.pajc.uno.model.card.Card;

public class HandCards
{
	private ArrayList<Card> cardList;

	public HandCards(Card[] cards)
	{
		this.cardList = new ArrayList<Card>(Arrays.asList(cards));
	}

	public Card getCard(int index)
	{
		return cardList.get(index);
	}

	public int getNumberOfCards()
	{
		return cardList.size();
	}

	public ArrayList<Card> getCardList()
	{
		return this.cardList;
	}
}
