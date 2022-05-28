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
		initModel(playerOneName, playerTwoName);

		Card randomCard = model.getCardsDeck().getRandomCard();
		gameView = new TableView(playerOneName, playerTwoName, randomCard);
		initView();

		// runGame();
	}

	public void runGame()
	{
		while (!model.isGameOver())
		{

		}
	}

	public void updateView(Player playerOne, Player playerTwo)
	{
		gameView.loadCards(playerOne.getHandCards(), 0);
		gameView.loadCards(playerTwo.getHandCards(), 1);
	}

	public void initModel(String playerOneName, String playerTwoName)
	{
		Player playerOne = new Player(playerOneName, model.generateStartingCards(), 0);
		Player playerTwo = new Player(playerTwoName, model.generateStartingCards(), 1);

		model.initPlayer(playerOne);
		model.initPlayer(playerTwo);
	}

	public void initView()
	{
		gameView.setVisible(true);
		gameView.setResizable(false);
	}
}
