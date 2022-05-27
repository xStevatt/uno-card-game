package unibs.pajc.uno.controller;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.TableView;

public class LocalPlayerController
{
	private TableView gameView;
	private GameModel model;

	public LocalPlayerController(String playerOneName, String playerTwoName)
	{
		model = new GameModel();
		gameView = new TableView(playerOneName, playerTwoName);
		gameView.setVisible(true);

		Player playerOne = new Player(playerOneName, model.generateStartingCards(), 0);
		Player playerTwo = new Player(playerTwoName, model.generateStartingCards(), 1);

		model.initPlayer(playerOne);
		model.initPlayer(playerTwo);

		initBeginningStateView(playerOne, playerTwo);

		runGame();
	}

	public void runGame()
	{

	}

	public void initBeginningStateModel(Player playerOneName, Player playerTwoName)
	{

	}

	public void initBeginningStateView(Player playerOne, Player playerTwo)
	{
		System.out.println(playerOne.getHandCards().getNumberOfCards());

		gameView.addCardToView();

		for (int i = 0; i < 10; i++)
		{
			gameView.loadCards(playerOne.getHandCards());
		}
	}
}
