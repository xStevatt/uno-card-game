package unibs.pajc.uno.controller;

import unibs.pajc.uno.controller.ai.AI;
import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.TableView;

public class AIPlayerController
{
	private TableView view;
	private GameModel model;
	private AI ai;

	public AIPlayerController()
	{
		view = new TableView("", "", true);
		initView();
	}

	public void initView()
	{
		view.setVisible(true);
		view.setResizable(false);
		view.setTitle("Single player game");
	}

	public void initModel()
	{
		Player playerOne = new Player("Human", model.generateStartingCards(), 0);
		Player playerTwo = new Player("Robot", model.generateStartingCards(), 1);

		model.initPlayer(playerOne);
		model.initPlayer(playerTwo);
	}

	public void updateView()
	{

	}
}
