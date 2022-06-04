package unibs.pajc.uno.controller.ai;

import java.util.HashMap;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.card.NumberCard;
import unibs.pajc.uno.model.player.HandCards;

public class AI
{
	private HashMap<CardColor, Integer> hashMap;

	public AI()
	{

	}

	public Card evalMossa(HandCards handCards, Card cardPlaced)
	{
		return new NumberCard(CardColor.RED, 0);
	}
}
