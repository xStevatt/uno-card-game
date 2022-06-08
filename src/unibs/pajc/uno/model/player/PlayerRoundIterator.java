package unibs.pajc.uno.model.player;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerRoundIterator implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ArrayList<Player> players;
	private int current = 0;
	private int previous = 0;
	private Direction direction = Direction.CLOCKWISE;

	public PlayerRoundIterator(ArrayList<Player> players)
	{
		this.players = players;
	}

	public PlayerRoundIterator(ArrayList<Player> players, int current)
	{
		this.players = players;
		this.current = current;
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
	 * @return
	 */
	public int getIndexCurrentPlayer()
	{
		return players.get(current).getIndex();
	}

	/**
	 * 
	 * @return
	 */
	public int getIndexNextPlayer()
	{
		return getNextIndex();
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

	public int getPreviousPlayerIndex()
	{
		return previous;
	}

	/**
	 * 
	 * @return
	 */
	public int getNextIndex()
	{
		previous = current;
		int increment = direction == Direction.CLOCKWISE ? 1 : -1;
		return (players.size() + current + increment) % players.size();
	}

	/**
	 * 
	 */
	public void setCurrentPlayer(int turn)
	{
		this.current = turn;
	}
}
