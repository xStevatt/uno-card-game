package unibs.pajc.uno.controller.ai;

import java.util.ArrayList;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.player.Player;

public class AI
{
	private GameModel gameModel;
	private ArrayList<Card> cardList;

	public AI(GameModel gameModel)
	{
		this.gameModel = gameModel;
	}

	public Card determineNexMossa()
	{
		Player currentPlayer = gameModel.getCurrentPlayer();
		ArrayList<Card> playerCardList = currentPlayer.getHandCards().getCardList();

		// CHECKS IF NEXT ADVERSARY HAS MORE CARDS THAN CURRENT PLAYER
		boolean hasPlayerMoreCards = gameModel.getCurrentPlayer().getHandCards().getCardList().size() > gameModel
				.getPlayers().get(gameModel.getNextPlayerIndex()).getHandCards().getCardList().size();

		// FILTERS OUT ONLY VALID CARDS
		playerCardList.stream().filter(e -> gameModel.isPlacedCardValid(e));
		System.out.println(playerCardList.size() + " valid cards found");

		if (playerCardList.size() != 0)
		{

		}

		// RETURNS A NEW CARD
		return drawCard();
	}

	public Card drawCard()
	{
		return null;
	}
}
