package unibs.pajc.uno.controller.net;

import java.io.Serializable;
import java.util.ArrayList;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.card.CardDeck;
import unibs.pajc.uno.model.player.Player;

public class Packet implements Serializable
{
	private ArrayList<Player> players;
	private CardDeck deck;
	private Card cardPlaced;
	private CardColor currentCardColor;
	private int currentTurn;

	private int iterationCounter = 0;

	public Packet(ArrayList<Player> players, Card cardPlaced, CardColor currentCardColor, CardDeck deck,
			int currentTurn, int iterationCounter)
	{
		this.players = players;
		this.cardPlaced = cardPlaced;
		this.currentCardColor = currentCardColor;
		this.currentTurn = currentTurn;
		this.deck = deck;
		this.iterationCounter = 0;
	}

	public void incrementCounter()
	{
		iterationCounter++;
	}

	public int getIterationCounter()
	{
		return iterationCounter;
	}

	public void setIterationCounter(int iterationCounter)
	{
		this.iterationCounter = iterationCounter;
	}

	public CardDeck getDeck()
	{
		return deck;
	}

	public void setDeck(CardDeck deck)
	{
		this.deck = deck;
	}

	public ArrayList<Player> getPlayers()
	{
		return players;
	}

	public void setPlayers(ArrayList<Player> players)
	{
		this.players = players;
	}

	public Card getCardPlaced()
	{
		return cardPlaced;
	}

	public void setCardPlaced(Card cardPlaced)
	{
		this.cardPlaced = cardPlaced;
	}

	public CardColor getCurrentCardColor()
	{
		return currentCardColor;
	}

	public void setCurrentCardColor(CardColor currentCardColor)
	{
		this.currentCardColor = currentCardColor;
	}

	public int getCurrentTurn()
	{
		return currentTurn;
	}

	public void setCurrentTurn(int currentTurn)
	{
		this.currentTurn = currentTurn;
	}
}
