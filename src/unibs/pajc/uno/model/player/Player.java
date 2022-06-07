package unibs.pajc.uno.model.player;

import java.io.Serializable;

import unibs.pajc.uno.model.card.Card;

public class Player implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String namePlayer;
	private final int index;
	private HandCards handCards;

	public Player(String namePlayer, Card[] cards, int index)
	{
		this.namePlayer = namePlayer;
		this.handCards = new HandCards(cards);
		this.index = index;
	}

	public boolean removeCard(Card card)
	{
		return handCards.getCardList().remove(card);
	}

	public void addCard(Card card)
	{
		handCards.addCard(card);
	}

	public String getNamePlayer()
	{
		return namePlayer;
	}

	public HandCards getHandCards()
	{
		return handCards;
	}

	public void setHandCards(HandCards handCards)
	{
		this.handCards = handCards;
	}

	public void removeAllCards()
	{
		handCards.removeAllCards();
	}

	public int getIndex()
	{
		return index;
	}
}
