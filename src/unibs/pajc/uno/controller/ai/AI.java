package unibs.pajc.uno.controller.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.player.Player;

public class AI
{
	private GameModel model;

	public AI(GameModel gameModel)
	{
		this.model = gameModel;
	}

	private Card hasSpecialWildCard(ArrayList<Card> cardsList)
	{
		cardsList.stream().filter(e -> e.isCardSpecialWild());

		if (!cardsList.isEmpty())
			return cardsList.get((int) (Math.random() * cardsList.size()));

		return null;
	}

	private CardColor determineMostPresentColor(ArrayList<Card> cardsList)
	{
		HashMap<CardColor, Integer> hashColorMap = new HashMap<CardColor, Integer>();

		for (Card card : cardsList)
		{
			if (hashColorMap.containsKey(card.getCardColor()))
			{
				hashColorMap.put(card.getCardColor(), 0);
			}
			else
			{
				hashColorMap.put(card.getCardColor(), hashColorMap.get(card.getCardColor()) + 1);
			}
		}

		for (Map.Entry entry : hashColorMap.entrySet())
		{
			System.out.println(entry.getKey() + " " + entry.getValue());
		}

		return CardColor.RED;
	}

	public Card determineNexMossa()
	{
		Player currentPlayer = model.getCurrentPlayer();
		ArrayList<Card> playerCardList = currentPlayer.getHandCards().getCardList();

		// CHECKS IF NEXT ADVERSARY HAS MORE CARDS THAN CURRENT PLAYER
		boolean hasPlayerMoreCards = model.getCurrentPlayer().getHandCards().getCardList().size() < model.getPlayers()
				.get(model.getNextPlayerIndex()).getHandCards().getCardList().size();

		// FILTERS OUT ONLY VALID CARDS
		playerCardList.stream().filter(e -> model.isPlacedCardValid(e));
		System.out.println(playerCardList.size() + " valid cards found");

		if (playerCardList.size() != 0)
		{
			if (hasSpecialWildCard(playerCardList) != null)
			{
				System.out.println("Player has special card list");
				this.model.setCurrentCardColor(determineMostPresentColor(playerCardList));
			}

			else if (!hasPlayerMoreCards && hasSpecialWildCard(playerCardList) != null)
			{
				System.out.println("Player has not special cards list");
			}
		}

		// RETURNS A NEW CARD
		return drawCard();
	}

	private Card drawCard()
	{
		return null;
	}

	public GameModel getModel()
	{
		return model;
	}
}
