package unibs.pajc.uno.controller;

import unibs.pajc.uno.controller.ai.AI;
import unibs.pajc.uno.model.GameModel;
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
	}
}
