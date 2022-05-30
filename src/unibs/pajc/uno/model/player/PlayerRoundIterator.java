package unibs.pajc.uno.model.player;

import java.util.ArrayList;

public class PlayerRoundIterator
{
	private final ArrayList<Player> players;
	private int current = 0;
	private Direction direction = Direction.CLOCKWISE;

	public PlayerRoundIterator(ArrayList<Player> players)
	{
		this.players = players;
	}

	/**
	 * 
	 * @return
	 */
	public Player getCurrentPlayer()
	{
		return players.get(current);
	}

	/**
	 * 
	 */
	public void reverseDirection()
	{
		direction = Direction.COUNTER_CLOCK_WISE;
	}

	/**
	 * 
	 * @return
	 */
	public Player next()
	{
		current = getNextIndex();
		return getCurrentPlayer();
	}

	/**
	 * 
	 * @return
	 */
	private int getNextIndex()
	{
		int increment = direction == Direction.CLOCKWISE ? 1 : -1;
		return (players.size() + current + increment) % players.size();
	}
}
