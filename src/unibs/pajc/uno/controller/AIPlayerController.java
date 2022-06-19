package unibs.pajc.uno.controller;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import unibs.pajc.uno.controller.ai.AI;
import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.player.Player;
import unibs.pajc.uno.view.CardView;
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

	public AIPlayerController(String nameHuman1)
	{
		// INITS MODEL
		model = new GameModel();
		initModel(nameHuman1, "Robot");

		// INITS VIEW
		view = new TableView(nameHuman1, "Robot", true);
		initView();

		// START RUNNING GAME
		executor = Executors.newCachedThreadPool();
		executor.execute(this::runGame);
	}

	public void initView()
	{
		// SHOWS GUI
		view.setVisible(true);
		view.setResizable(false);
		view.setLocationRelativeTo(null);

		// CREATES ACTION LISTER TO DRAW A CARD
		mouseListenerDrawnCard = new CardDrawnEvent(notifyObj);
		view.getCardDeckView().addMouseListener(mouseListenerDrawnCard);

		updateView();
	}

	public void initModel(String namePlayerOne, String namePlayerTwo)
	{
		Player playerOne = new Player("Human", model.generateStartingCards(), 0);
		Player playerTwo = new Player("Robot", model.generateStartingCards(), 1);

		model.initPlayer(playerOne);
		model.initPlayer(playerTwo);

		ai = new AI(model);
	}

	public void updateView()
	{

		// SETS TURN AND LOADS CARDS
		view.setTurn(model.getCurrentPlayer().getNamePlayer());
		view.loadCards(model.getPlayers().get(0).getHandCards(), 0);
		view.loadCards(model.getPlayers().get(1).getHandCards(), 1);
		view.changeDroppedCardView(model.getLastCardUsed(), model.getCurrentCardColor());

		// DISABLE BUTTONS
		view.setSayUnoButtonVisibile(false, model.getPreviousPlayerIndex());
		view.setSayUnoButtonVisibile(false, model.getCurrentPlayerIndex());

		// GETS ALL CARDS VIEWS FROM GAMEVIEW
		ArrayList<CardView> panelPlayerOneCards = view.getAllCards(0,
				model.getPlayers().get(0).getHandCards().getNumberOfCards());
		ArrayList<CardView> panelPlayerTwoCards = view.getAllCards(1,
				model.getPlayers().get(1).getHandCards().getNumberOfCards());

		// ADD MOUSE LISTENERS TO CARDS
		mouseListener = new CardSelectedEvent(notifyObj);
		panelPlayerOneCards.forEach(e -> e.addMouseListener(mouseListener));
		panelPlayerTwoCards.forEach(e -> e.addMouseListener(mouseListener));

		// ENABLES / DISABLE VIEW FOR PLAYERS
		view.enableViewPlayer(model.getCurrentPlayerIndex(), true);
		view.enableViewPlayer(model.getNextPlayerIndex(), false);

		// CHECKS IF UNO BUTTON SHOULD BE ENABLED
		view.setSayUnoButtonVisibile(model.hasPlayerOneCard(model.getCurrentPlayer()), model.getCurrentPlayerIndex());

		// REPAINTS VIEW
		view.repaint();
	}

	public void runGame()
	{

	}
}
