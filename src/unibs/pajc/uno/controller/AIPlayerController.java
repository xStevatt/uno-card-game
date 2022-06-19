package unibs.pajc.uno.controller;

import java.util.concurrent.ExecutorService;

import unibs.pajc.uno.controller.ai.AI;
import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.TableView;
import unibs.pajc.uno.view.events.CardDrawnEvent;
import unibs.pajc.uno.view.events.CardSelectedEvent;

public class AIPlayerController
{
	private TableView view;
	private GameModel model;
	private AI ai;

	private ExecutorService executor;

	private Object notifyObj = new Object();

	private Card cardSelected = null;

	private CardSelectedEvent mouseListener;
	private CardDrawnEvent mouseListenerDrawnCard;

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
		view.setVisible(true);
		view.setResizable(false);
		view.setLocationRelativeTo(null);

		// CREATES ACTION LISTER TO DRAW A CARD
		mouseListenerDrawnCard = new CardDrawnEvent(notifyObj);
		view.getCardDeckView().addMouseListener(mouseListenerDrawnCard);

		updateView();
	}
}
