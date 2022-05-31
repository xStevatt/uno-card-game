package unibs.pajc.uno.test.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.card.NumberCard;
import unibs.pajc.uno.model.player.Player;

public class GameModelTest
{
	@Test
	public void createNewPlayers()
	{
		GameModel gameModel = new GameModel();

		Player player1 = new Player("Player 1", gameModel.generateStartingCards(), 0);
		Player player2 = new Player("Player 2", gameModel.generateStartingCards(), 1);

	}

	@Test
	public void isCardValidSameNumberButDifferentColor()
	{
		Card cardUsed = new NumberCard(CardColor.RED, 9);
		Card cardSelected = new NumberCard(CardColor.BLUE, 9);

		assertEquals(gameModel.isPlacedCardValid(cardSelected, cardUsed), true);
	}

	@Test
	public void isCardValidSameColorButDifferentNumber()
	{
		Card cardUsed = new NumberCard(CardColor.RED, 9);
		Card cardSelected = new NumberCard(CardColor.RED, 8);

		assertEquals(gameModel.isPlacedCardValid(cardSelected, cardUsed), true);
	}

	@Test
	public void isCardValidSameColorSameNumber()
	{
		Card cardUsed = new NumberCard(CardColor.RED, 9);
		Card cardSelected = new NumberCard(CardColor.RED, 9);

		assertEquals(gameModel.isPlacedCardValid(cardSelected, cardUsed), true);
	}

	@Test
	public void isCardValidDifferentColorAndDifferentNumber()
	{
		Card cardUsed = new NumberCard(CardColor.RED, 9);
		Card cardSelected = new NumberCard(CardColor.BLUE, 8);

		assertEquals(gameModel.isPlacedCardValid(cardSelected, cardUsed), false);
	}

	@Test
	public void isGameOverWithStillCards()
	{
		assertEquals(gameModel.isGameOver(), false);
	}

	/*
	 * @Test public void doesRemovingActuallyWork() {
	 * player1.getHandCards().removeAllCards();
	 * assertEquals(player1.getHandCards().getNumberOfCards(), 0); }
	 * 
	 * @Test public void isGameOverWithoutCards() {
	 * player1.getHandCards().removeAllCards(); assertEquals(gameModel.isGameOver(),
	 * true); }
	 */
}
