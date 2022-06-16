package unibs.pajc.uno.controller.ai;

import java.util.HashMap;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.player.HandCards;

public class AI
{
	private HashMap<Card, Integer> hashMapCards;

	public AI()
	{
		hashMapCards = new HashMap<>();
	}

	public Card findNextMossa(HandCards handCards, Card cardPlaced)
	{
		HandCards possibleSelections = (HandCards) handCards.getCardList().stream().filter(e -> e.isCardSpecialWild());

		return null;
	}
}
