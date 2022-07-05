package unibs.pajc.uno.test.model;

import java.util.ArrayList;

import org.junit.Test;

import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.model.player.PlayerRoundIterator;

public class RoundPlayerIterator
{
	@Test
	public void testRoundPlayers()
	{
		Player playerOne = new Player("One", 0);
		Player playerTwo = new Player("Two", 1);
		Player playerThree = new Player("Three", 2);

		ArrayList<Player> players = new ArrayList<Player>();
		players.add(playerOne);
		players.add(playerTwo);
		players.add(playerThree);
		PlayerRoundIterator iterator = new PlayerRoundIterator(players);

		for (int i = 0; i < 20; i++)
		{
			System.out.println(iterator.next().getIndex());
		}
	}
}
