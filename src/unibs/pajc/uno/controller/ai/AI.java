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
	private GameModel gameModel;
	private ArrayList<Card> cardList;

	public AI(GameModel gameModel)
	{
		this.gameModel = gameModel;
	}

	private Card hasSpecialWildCard(ArrayList<Card> cardsList)
	{
		cardsList.stream().filter(e -> e.isCardSpecialWild());

		CardColor cardColor = determineMostPresentColor(cardsList);

		if (!cardList.isEmpty())
			return cardList.get((int) (Math.random() * cardList.size()));

		return null;
	}

	private CardColor determineMostPresentColor(ArrayList<Card> cardsList)
	{
		HashMap<CardColor, Integer> hashColorMap = new HashMap<CardColor, Integer>();

		for (Card card : cardList)
		{
			hashColorMap.put(card.getCardColor(), hashColorMap.get(card.getCardColor()) + 1);
		}

		for (Map.Entry entry : hashColorMap.entrySet())
		{
			System.out.println(entry.getKey() + " " + entry.getValue());
		}

		return CardColor.RED;
	}

	public Card determineNexMossa()
	{
		Player currentPlayer = gameModel.getCurrentPlayer();
		ArrayList<Card> playerCardList = currentPlayer.getHandCards().getCardList();

		// CHECKS IF NEXT ADVERSARY HAS MORE CARDS THAN CURRENT PLAYER
		boolean hasPlayerMoreCards = gameModel.getCurrentPlayer().getHandCards().getCardList().size() < gameModel
				.getPlayers().get(gameModel.getNextPlayerIndex()).getHandCards().getCardList().size();

		// FILTERS OUT ONLY VALID CARDS
		playerCardList.stream().filter(e -> gameModel.isPlacedCardValid(e));
		System.out.println(playerCardList.size() + " valid cards found");

		if (playerCardList.size() != 0)
		{
			if (!hasPlayerMoreCards && hasSpecialWildCard(cardList) != null)
			{
				System.out.println("Player ahs special card list");
			}

			else if (!hasPlayerMoreCards && hasSpecialWildCard(cardList) != null)
			{
				System.out.println("Player has not special cards list");
			}
		}

		// RETURNS A NEW CARD
		return drawCard();
	}

	public Card drawCard()
	{
		return null;
	}
}
