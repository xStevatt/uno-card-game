package unibs.pajc.uno.model.player;

import unibs.pajc.uno.model.card.Card;

public class Player
{
	private String namePlayer;

	private HandCards handCards;

	public Player(String namePlayer, Card[] cards)
	{
		this.namePlayer = namePlayer;
		this.handCards = new HandCards(cards);
	}

	public String getNamePlayer()
	{
		return namePlayer;
	}

	public void setNamePlayer(String namePlayer)
	{
		this.namePlayer = namePlayer;
	}

	public HandCards getHandCards()
	{
		return handCards;
	}

	public void setHandCards(HandCards handCards)
	{
		this.handCards = handCards;
	}
}
