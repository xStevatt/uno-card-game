package unibs.pajc.uno.controller;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.TableView;

public class LocalPlayerController
{
	private TableView gameView;
	private GameModel model;

	public LocalPlayerController(String playerOneName, String playerTwoName)
	{
		model = new GameModel();

		Player playerOne = new Player(playerOneName, model.generateStartingCards(), 0);
		Player playerTwo = new Player(playerTwoName, model.generateStartingCards(), 1);

		model.initPlayer(playerOne);
		model.initPlayer(playerTwo);

		// Takes a random car out of the deck and puts it in the used card deck
		Card randomCard = model.getCardsDeck().getRandomCard();

		gameView = new TableView(playerOneName, playerTwoName, randomCard);
		gameView.setVisible(true);
		updateView(playerOne, playerTwo);

		runGame();
	}

	public void runGame()
	{

	}

	public void updateView(Player playerOne, Player playerTwo)
	{
		gameView.loadCards(playerOne.getHandCards());
	}
}
