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
		for (Card card : cardsList)
		{
			if (card.isCardSpecialWild())
				return card;
		}

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
		int maxOccurrences = 0;

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
				if (entry.getValue() >= maxOccurrences)
				{
					maxColor = entry.getKey();
				}
			}
		}

		return maxColor != null ? maxColor : CardColor.RED;
	}

	public void determineNexMossa(GameModel model)
	{
		this.model = model;

		Player currentPlayer = model.getCurrentPlayer();
		ArrayList<Card> playerCardList = currentPlayer.getHandCards().getCardList();
		ArrayList<Card> validCards = new ArrayList<>();

		// CHECKS IF NEXT ADVERSARY HAS MORE CARDS THAN CURRENT PLAYER
		boolean hasPlayerMoreCards = model.getCurrentPlayer().getHandCards().getCardList().size() < model.getPlayers()
				.get(model.getNextPlayerIndex()).getHandCards().getCardList().size();

		// FILTERS OUT ONLY VALID CARDS

		System.out.print("VALID CARDS -> < ");
		playerCardList.stream().filter(e -> model.isPlacedCardValid(e))
				.forEach(e -> System.out.print(e.getCardColor() + ", " + e.getCardType()));
		System.out.println(" >");

		for (Card card : playerCardList)
		{
			if (model.isPlacedCardValid(card))
			{
				validCards.add(card);
			}
		}

		if (validCards.size() != 0)
		{
			if (hasSpecialWildCard(playerCardList) != null)
			{
				Card card = hasSpecialWildCard(model.getPlayers().get(1).getHandCards().getCardList());
				System.out.println("Best move -> < " + card.getCardColor() + " " + card.getCardType() + " >");
				model.evalMossa(card);
				this.model.setCurrentCardColor(determineMostPresentColor(playerCardList));
			}
			else
			{
				Card card = validCards.get((int) (Math.random() * validCards.size()));
				System.out.println("Best move -> < " + card.getCardColor() + " " + card.getCardType() + " >");
				model.evalMossa(card);
			}
		}
		else
		{

			drawCard();
		}

	}

	private void drawCard()
	{
		System.out.println("AI - Drawing a card");
		model.getPlayers().get(model.getCurrentPlayerIndex()).addCard(model.getCardFromDeck());
		model.nextTurn();
	}

	public GameModel getModel()
	{
		return model;
	}
}
