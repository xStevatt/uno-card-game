package unibs.pajc.uno.test.view;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.model.player.PlayerRoundIterator;
import unibs.pajc.uno.view.TableView;

public class TestTableView
{
	@Test
	public void testTableView()
	{

		// PROVA A CREARE UNA PARTITA CON PIU GIOCATORI

		TableView table = new TableView("PlayerOne", "PlayerTwo", true);
		table.setVisible(true);

		GameModel model = new GameModel();

		Player playerOne = new Player("Player One", model.generateStartingCards(), 0);
		Player playerTwo = new Player("Player Two", model.generateStartingCards(), 1);
		Player playerThree = new Player("Player Three", model.generateStartingCards(), 2);

		ArrayList<Player> players = new ArrayList<Player>(
				Arrays.asList(new Player[] { playerOne, playerTwo, playerThree }));

		model.initPlayers(players);

		PlayerRoundIterator iterator = new PlayerRoundIterator(players);

		for (int i = 0; i < 10; i++)
		{
			if (iterator.getIndexCurrentPlayer() == 0)
				table.loadCards(model.getPlayers().get(0).getHandCards(), 0);
			else
				table.loadCards(model.getPlayers().get(iterator.getIndexCurrentPlayer()).getHandCards(), 1);

			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			iterator.next();
		}

		try
		{
			Thread.sleep(10000);
		}
		catch (Exception e)
		{
			System.out.println("Error while loading cards");
		}
	}
}
