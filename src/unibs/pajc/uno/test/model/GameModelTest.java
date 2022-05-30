package unibs.pajc.uno.test.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.player.Player;

public class GameModelTest
{
	GameModel gameModel = new GameModel();
	Player player1 = new Player("Player 1", gameModel.generateStartingCards(), 0);
	Player player2 = new Player("Player 2", gameModel.generateStartingCards(), 1);

	@Test
	public void isGameOverWithStillCards()
	{
		assertEquals(gameModel.isGameOver(), false);
	}

	@Test
	public void doesRemovingActuallyWork()
	{
		player1.getHandCards().removeAllCards();
		assertEquals(player1.getHandCards().getNumberOfCards(), 0);
	}

	@Test
	public void isGameOverWithoutCards()
	{
		player1.getHandCards().removeAllCards();
		assertEquals(gameModel.isGameOver(), true);
	}
}
