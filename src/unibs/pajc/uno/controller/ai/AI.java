package unibs.pajc.uno.controller.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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

	/**
	 * Method checks if player has any wild cards
	 * 
	 * @param cardsList the cardsList to check
	 * @return method returns true if player has any special wild cards
	 */
	private Card hasSpecialWildCard(ArrayList<Card> cardsList)
	{
		cardsList.stream().filter(e -> e.isCardSpecialWild());

		if (!cardsList.isEmpty())
			return cardsList.get((int) (Math.random() * cardsList.size()));

		return null;
	}

	/**
	 * Method
	 * 
	 * @param cardsList
	 * @return
	 */
	private CardColor determineMostPresentColor(ArrayList<Card> cardsList)
	{
		HashMap<CardColor, Integer> hashColorMap = new HashMap<CardColor, Integer>();
		CardColor maxColor = null;
		int maxOccurencies = 0;

		for (Card card : cardsList)
		{
			if (hashColorMap.containsKey(card.getCardColor()))
			{
				hashColorMap.put(card.getCardColor(), hashColorMap.get(card.getCardColor()) + 1);
			}
			else
			{
				hashColorMap.put(card.getCardColor(), 0);
			}
		}

		if (!hashColorMap.isEmpty())
		{
			for (Entry<CardColor, Integer> entry : hashColorMap.entrySet())
			{
				if (entry.getValue() >= maxOccurencies)
				{
					maxColor = entry.getKey();
				}
			}
		}

		return maxColor != null ? maxColor : CardColor.RED;
	}

	public Card determineNexMossa()
	{
		Player currentPlayer = model.getCurrentPlayer();
		ArrayList<Card> playerCardList = currentPlayer.getHandCards().getCardList();

		// CHECKS IF NEXT ADVERSARY HAS MORE CARDS THAN CURRENT PLAYER
		boolean hasPlayerMoreCards = model.getCurrentPlayer().getHandCards().getCardList().size() < model.getPlayers()
				.get(model.getNextPlayerIndex()).getHandCards().getCardList().size();

		// FILTERS OUT ONLY VALID CARDS

		playerCardList.stream().filter(e -> model.isPlacedCardValid(e))
				.forEach(e -> System.out.println(e.getCardColor() + ", " + e.getCardType()));

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
		System.out.println("AI - Drawing a card");
		return null;
	}

	public GameModel getModel()
	{
		return model;
	}
}
