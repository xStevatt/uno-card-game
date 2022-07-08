package unibs.pajc.uno.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.player.Player;

/**
 * Classe di gestione del controller per il gioco in multiplayer locale
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
public class AI
{
	private GameModel model;

	public AI(GameModel gameModel)
	{
		this.model = gameModel;
	}

	/**
	 * Metodo che controlla se sono presenti delle carte speciali all'interno della
	 * lista di carte del giocatore
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
	 * Metodo che ritorna il colore che è presenta per il numero maggiore di volte
	 * 
	 * @param la lista di carte che devono essere analizzate
	 * @return il colore che è presente per il numero maggiore di volte
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

	/**
	 * Metodo che stampa a schermo tutte le carte valide del giocatore
	 * 
	 * @param model
	 */
	public void printValidCards(GameModel model)
	{
		// FILTERS OUT ONLY VALID CARDS

		Player currentPlayer = model.getCurrentPlayer();
		ArrayList<Card> playerCardList = currentPlayer.getHandCards().getCardList();
		ArrayList<Card> validCards = new ArrayList<>();

		System.out.print("VALID CARDS -> < ");
		playerCardList.stream().filter(e -> model.isPlacedCardValid(e))
				.forEach(e -> System.out.print(e.getCardColor() + ", " + e.getCardType()));
		System.out.println(" >");
	}

	/**
	 * Metodo che determina la prossima mossa che deve prendere il computer
	 * 
	 * @param model
	 */
	public void determineNexMossa(GameModel model)
	{
		this.model = model;

		Player currentPlayer = model.getCurrentPlayer();
		ArrayList<Card> playerCardList = currentPlayer.getHandCards().getCardList();
		ArrayList<Card> validCards = new ArrayList<>();

		// CHECKS IF NEXT ADVERSARY HAS MORE CARDS THAN CURRENT PLAYER
		boolean hasPlayerMoreCards = model.getCurrentPlayer().getHandCards().getCardList().size() >= model.getPlayers()
				.get(model.getNextPlayerIndex()).getHandCards().getCardList().size();

		printValidCards(model);

		for (Card card : playerCardList)
		{
			if (model.isPlacedCardValid(card))
			{
				validCards.add(card);
			}
		}

		if (validCards.size() != 0)
		{
			if (hasSpecialWildCard(playerCardList) != null && hasPlayerMoreCards)
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

	/**
	 * Metodo che permette all'AI di pescare una carta dal mezzo di carte
	 */
	private void drawCard()
	{
		System.out.println("AI - Drawing a card");
		model.getPlayers().get(model.getCurrentPlayerIndex()).addCard(model.getCardFromDeck());
		model.nextTurn();
	}

	/**
	 * Ritorna il model del gioco modificato dall'AI.
	 * 
	 * @return
	 */
	public GameModel getModel()
	{
		return model;
	}
}
