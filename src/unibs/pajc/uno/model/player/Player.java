package unibs.pajc.uno.model.player;

import unibs.pajc.uno.model.card.Card;

public class Player
{
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

	public void addCard()
	{

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

	public int getIndex()
	{
		return index;
	}
}
