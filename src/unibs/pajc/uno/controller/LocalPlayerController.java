package unibs.pajc.uno.controller;

import java.util.Random;

import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.TableView;

public class LocalPlayerController
{
	private TableView gameView;
	private GameModel model;

	public LocalPlayerController(String playerOneName, String playerTwoName)
	{
		// INITS MODEL
		model = new GameModel();
		initModel(playerOneName, playerTwoName);

		// INITS VIEW
		gameView = new TableView(playerOneName, playerTwoName, model.getUsedCards().getLastCardUsed());
		initView();

		// START RUNNING GAME
		// runGame();
	}

	public void runGame()
	{
		int turn = new Random().nextInt(1);

		int i = 0;

		while (i < 3)
		{
			if (turn == 0)
			{
				gameView.disableViewPlayer(1);
			}
			else
			{
				gameView.disableViewPlayer(0);
			}
			i++;
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
		gameView.loadCards(model.getPlayers()[0].getHandCards(), 0);
		gameView.loadCards(model.getPlayers()[0].getHandCards(), 1);
	}
}
