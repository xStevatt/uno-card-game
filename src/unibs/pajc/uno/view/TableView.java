package unibs.pajc.uno.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import unibs.pajc.uno.model.GameRules;
import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.player.HandCards;
import unibs.pajc.uno.model.player.Player;

public class TableView extends JFrame
{

	public static boolean isLocalPlayerUnoButtonPressed = false;
	public static boolean isAdversaryPlayerUnoButtonPressed = false;

	private JPanel contentPane;
	private JTextField txtSendMessageField;

	// Most important panels (player one cards, player two cards and the center
	// panel)
	private JPanel panelActualPlayer;
	private JPanel panelAdversaryPlayer;
	private JPanel centerPanel;

	private JPanel midTable;
	private JPanel panelChat;
	private JPanel panelPlaced;
	private JPanel panel;
	private JPanel panelDeck;

	private JTextArea textArea;
	private JButton btnNewButton;

	private JLayeredPane handCardsViewActual;
	private JLayeredPane handCardsViewAdversary;

	private JTextArea textAreaChat;
	private JButton btnSendMessage;
	private JSeparator separator;

	private JButton sayUnoButtonPlayerTwo;
	private JButton sayUnoButtonPlayerOne;
	private JLabel lblNewLabel;
	private JLabel matchDescriptor;

	private JLabel lblStopWatch;

	private ArrayList<CardView> listCardsView;

	private boolean isGameLocal = true;
	private JLabel lblNewLabel_2;

	/**
	 * Constructor to create the form.
	 * 
	 * @param namePlayerOne
	 * @param namePlayerTwo
	 * @param firstRandomCard
	 */
	public TableView(String namePlayerOne, String namePlayerTwo, boolean isGameLocal)
	{
		super("Uno - cards game");
		this.isGameLocal = isGameLocal;

		setTitle("Uno - card's game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1300, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		centerPanel = new JPanel()
		{
			@Override
			public void paintComponent(Graphics g)
			{
				ImageIcon wallpaper = new ImageIcon("res/img/wallpaper.png");
				Image i = wallpaper.getImage();

				g.drawImage(i, 0, 0, this.getSize().width, this.getSize().height, this);
			}
		};
		centerPanel.setBackground(Color.GREEN);
		centerPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		centerPanel.setBounds(6, 6, 1027, 660);
		contentPane.add(centerPanel);
		centerPanel.setLayout(null);

		panelActualPlayer = new JPanel();
		panelActualPlayer
				.setBorder(new TitledBorder(null, namePlayerOne, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelActualPlayer.setBounds(6, 454, 1015, 200);
		panelActualPlayer.setOpaque(false);
		panelActualPlayer.setLayout(new GridBagLayout());

		handCardsViewActual = new JLayeredPane();
		handCardsViewActual.setPreferredSize(new Dimension(940, 175));
		handCardsViewActual.setOpaque(false);

		handCardsViewAdversary = new JLayeredPane();
		handCardsViewAdversary.setPreferredSize(new Dimension(940, 175));
		handCardsViewAdversary.setOpaque(false);
		panelActualPlayer.setLayout(new GridBagLayout());

		centerPanel.add(panelActualPlayer);

		panelAdversaryPlayer = new JPanel();
		panelAdversaryPlayer.setOpaque(false);
		panelAdversaryPlayer
				.setBorder(new TitledBorder(null, namePlayerTwo, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelAdversaryPlayer.setBounds(6, 6, 1015, 200);

		centerPanel.add(panelAdversaryPlayer);
		GridBagLayout gbl_panelAdversaryPlayer = new GridBagLayout();
		gbl_panelAdversaryPlayer.columnWidths = new int[] { 0 };
		gbl_panelAdversaryPlayer.rowHeights = new int[] { 0 };
		gbl_panelAdversaryPlayer.columnWeights = new double[] { Double.MIN_VALUE };
		gbl_panelAdversaryPlayer.rowWeights = new double[] { Double.MIN_VALUE };
		panelAdversaryPlayer.setLayout(gbl_panelAdversaryPlayer);

		midTable = new JPanel();
		midTable.setOpaque(false);
		midTable.setBorder(new LineBorder(Color.DARK_GRAY));
		midTable.setBounds(73, 218, 869, 224);
		centerPanel.add(midTable);
		midTable.setLayout(null);

		sayUnoButtonPlayerOne = new JButton("SAY UNO!");
		sayUnoButtonPlayerOne.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("UNO!");
			}
		});
		sayUnoButtonPlayerOne.setVisible(false);
		sayUnoButtonPlayerOne.setBounds(728, 179, 135, 39);
		midTable.add(sayUnoButtonPlayerOne);

		sayUnoButtonPlayerTwo = new JButton("SAY UNO!");
		sayUnoButtonPlayerTwo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("UNO!");
			}
		});
		sayUnoButtonPlayerTwo.setVisible(false);
		sayUnoButtonPlayerTwo.setBounds(6, 6, 135, 39);
		midTable.add(sayUnoButtonPlayerTwo);

		panelDeck = new JPanel();
		// +5 is due to card's border
		panelDeck.setBounds(304, 30, 100 + 5, 150 + 5);
		midTable.add(panelDeck);
		panelDeck.setOpaque(false);

		panelDeck.add(new CardBackView());

		panelPlaced = new JPanel();
		// +5 is due to card's border
		panelPlaced.setBounds(455, 30, 100 + 5, 150 + 5);
		panelPlaced.setOpaque(false);
		midTable.add(panelPlaced);

		panelChat = new JPanel();
		panelChat.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Chat",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelChat.setBounds(1045, 201, 249, 465);
		contentPane.add(panelChat);
		panelChat.setLayout(null);

		textAreaChat = new JTextArea();
		textAreaChat.setLineWrap(true);
		textAreaChat.setEditable(false);
		textAreaChat.setRows(1);
		textAreaChat.setBounds(17, 24, 216, 341);
		panelChat.add(textAreaChat);

		btnSendMessage = new JButton("Send");
		btnSendMessage.setBounds(159, 405, 73, 37);

		panelChat.add(btnSendMessage);

		txtSendMessageField = new JTextField();
		txtSendMessageField.setBounds(17, 401, 130, 43);
		panelChat.add(txtSendMessageField);
		txtSendMessageField.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBounds(17, 377, 216, 12);
		panelChat.add(separator);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.DARK_GRAY));
		panel_1.setBounds(1045, 6, 249, 183);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		lblNewLabel = new JLabel("Uno Game");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(6, 14, 237, 23);
		panel_1.add(lblNewLabel);

		matchDescriptor = new JLabel(namePlayerOne + " vs " + namePlayerTwo);
		matchDescriptor.setHorizontalAlignment(SwingConstants.CENTER);
		matchDescriptor.setBounds(16, 73, 227, 16);
		panel_1.add(matchDescriptor);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(16, 49, 227, 12);
		panel_1.add(separator_1);

		lblStopWatch = new JLabel("00:00:00");
		lblStopWatch.setHorizontalAlignment(SwingConstants.CENTER);
		lblStopWatch.setBounds(6, 101, 237, 16);
		panel_1.add(lblStopWatch);

		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setBounds(16, 126, 227, 12);
		panel_1.add(separator_1_1);

		JLabel lblPlayerTurn = new JLabel("Player's Turn");
		lblPlayerTurn.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblPlayerTurn.setHorizontalAlignment(SwingConstants.LEFT);
		lblPlayerTurn.setBounds(106, 150, 137, 27);
		panel_1.add(lblPlayerTurn);

		lblNewLabel_2 = new JLabel("Turn:");
		lblNewLabel_2.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setBounds(6, 150, 88, 27);
		panel_1.add(lblNewLabel_2);

		if (isGameLocal)
		{
			btnSendMessage.setEnabled(false);
			txtSendMessageField.setEditable(false);
			textAreaChat.setEnabled(true);

			textAreaChat.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseEntered(MouseEvent e)
				{
					btnSendMessage.setToolTipText("Chat is disabled as you're playing a local game.");
					txtSendMessageField.setToolTipText("Chat is disabled as you're playing a local game.");
					panelChat.setToolTipText("Chat is disabled as you're playing a local game.");
				}
			});
		}

		initTimer();
	}

	/**
	 * 
	 * @param playerOne
	 * @param playerTwo
	 * @param lastCardUsed
	 * @param currentPlayer
	 */
	public void updateView(Player playerOne, Player playerTwo, Card lastCardUsed, int currentPlayer)
	{
		if (currentPlayer == 0)
		{
			loadCards(playerOne.getHandCards(), currentPlayer);
		}
		if (currentPlayer == 1)
		{
			loadCards(playerTwo.getHandCards(), currentPlayer);
		}

		panelPlaced.removeAll();
		changeDroppedCardView(lastCardUsed);
	}

	/**
	 * Inits game timer. Timer starts at 00:00:00 and stops when game is over.
	 */
	public void initTimer()
	{
		Timer timer = new Timer(1000, new ActionListener()
		{
			int elapsedTime = 0;
			int seconds = 0;
			int minutes = 0;
			int hours = 0;

			String seconds_string = String.format("%02d", seconds);
			String minutes_string = String.format("%02d", minutes);
			String hours_string = String.format("%02d", hours);

			public void actionPerformed(ActionEvent e)
			{
				elapsedTime = elapsedTime + 1000;
				hours = (elapsedTime / 3600000);
				minutes = (elapsedTime / 60000) % 60;
				seconds = (elapsedTime / 1000) % 60;
				seconds_string = String.format("%02d", seconds);
				minutes_string = String.format("%02d", minutes);
				hours_string = String.format("%02d", hours);
				lblStopWatch.setText(hours_string + ":" + minutes_string + ":" + seconds_string);
			}
		});

		timer.setCoalesce(true);
		timer.start();
	}

	/**
	 * 
	 * @param index
	 */
	public void enableViewPlayer(int index, boolean enabled)
	{
		if (index == 0)
		{
			for (int i = 0; i < handCardsViewActual.getComponentCount(); i++)
			{
				((CardView) handCardsViewActual.getComponent(i)).setShouldAnimationsMove(enabled);
			}
		}

		if (index == 1)
		{
			for (int i = 0; i < handCardsViewAdversary.getComponentCount(); i++)
			{
				((CardView) handCardsViewAdversary.getComponent(i)).setShouldAnimationsMove(enabled);
			}
		}
	}

	/**
	 * Sets "say uno" buttons invisible. "say uno" buttons are visible only when a
	 * player only has one card left. If the user doesn't say "uno" (doesn't press
	 * the button), two cards are added to his hand.
	 */
	public void makeSayUnoButtonVisibile(boolean visibility)
	{
		sayUnoButtonPlayerOne.setVisible(visibility);
		sayUnoButtonPlayerTwo.setVisible(visibility);
	}

	/**
	 * Replaces the cards dropped on the table
	 * 
	 * @param card
	 */
	public void changeDroppedCardView(Card card)
	{
		panelPlaced.removeAll();
		CardView cardToAdd = new CardView(card);
		cardToAdd.setShouldAnimationsMove(false);

		panelPlaced.add(cardToAdd);
	}

	/**
	 * 
	 * Method to load all cards.
	 * 
	 * @param handCards
	 * @param players
	 */
	public void loadCards(HandCards cards, int playingPlayer)
	{
		if (playingPlayer == 0)
		{
			handCardsViewActual.removeAll();
			panelActualPlayer.removeAll();
			addCardsToView(cards, panelActualPlayer, handCardsViewActual);
		}
		if (playingPlayer == 1)
		{
			handCardsViewAdversary.removeAll();
			panelAdversaryPlayer.removeAll();
			addCardsToView(cards, panelAdversaryPlayer, handCardsViewAdversary);
		}

		// REPAINTS ALL COMPONENTS
		panelAdversaryPlayer.repaint();
		panelAdversaryPlayer.repaint();
		handCardsViewActual.repaint();
		panelActualPlayer.repaint();
	}

	/**
	 * Method to add cards of the players
	 * 
	 * @param handCards
	 * @param panelToAddCards
	 */
	public void addCardsToView(HandCards handCards, JPanel panelToAddCards, JLayeredPane cardsView)
	{
		Point originPoint = getFirstCardPoint(handCards.getNumberOfCards(), cardsView);
		int offset = calculateOffset(cardsView.getWidth(), handCards.getNumberOfCards());

		int i = 0;

		for (Card card : handCards.getCardList())
		{
			CardView cardView = new CardView(card);

			cardView.setBounds(originPoint.x, originPoint.y, cardView.getDimension().width,
					cardView.getDimension().height);
			cardsView.add(cardView, i++);

			cardsView.moveToFront(cardView);

			originPoint.x += offset;
		}

		cardsView.revalidate();
		// ADDS CARDS PANEL TO OUTSIDE COMPONENT
		panelToAddCards.add(cardsView);
	}

	/**
	 * 
	 * @param totalCards
	 * @return
	 */
	private Point getFirstCardPoint(int totalCards, JLayeredPane handCardsView)
	{
		Point p = new Point(0, 20);

		// var width = handCardsView.getWidth() == 0 ?
		// handCardsView.getPreferredSize().width : handCardsView.getWidth();

		// var offset = calculateOffset(width, totalCards);
		// p.x = (width - offset * totalCards) / 2;

		return p;
	}

	/**
	 * Calculates the offset to set cards
	 * 
	 * @param width
	 * @param totalCards
	 * @return
	 */
	private int calculateOffset(int width, int totalCards)
	{
		if (totalCards <= GameRules.DEFAULT_NUMBER_OF_CARDS)
		{
			return 120;
		}
		else
		{
			System.out.println((width - 100) / (totalCards - 1));
			return (width - 100) / (totalCards - 1);
		}

	}

	// GETTERS AND SETTERS

	public ArrayList<CardView> getAllCards(int indexPlayer)
	{
		ArrayList<CardView> list = new ArrayList<CardView>();

		if (indexPlayer == 0)
		{
			for (int i = 0; i < handCardsViewActual.getComponentCount(); i++)
			{
				list.add((CardView) handCardsViewActual.getComponent(i));
			}
		}

		if (indexPlayer == 1)
		{
			for (int i = 0; i < handCardsViewAdversary.getComponentCount(); i++)
			{
				list.add((CardView) handCardsViewActual.getComponent(i));
			}
		}

		return list;
	}

	public JPanel getMidTable()
	{
		return midTable;
	}

	public void setMidTable(JPanel midTable)
	{
		this.midTable = midTable;
	}

	public JPanel getPanelPlaced()
	{
		return panelPlaced;
	}

	public void setPanelPlaced(JPanel panelPlaced)
	{
		this.panelPlaced = panelPlaced;
	}

	public JPanel getPanelDeck()
	{
		return panelDeck;
	}

	public void setPanelDeck(JPanel panelDeck)
	{
		this.panelDeck = panelDeck;
	}

	public JLayeredPane getHandCardsViewActual()
	{
		return handCardsViewActual;
	}

	public void setHandCardsViewActual(JLayeredPane handCardsViewActual)
	{
		this.handCardsViewActual = handCardsViewActual;
	}

	public JLayeredPane getHandCardsViewAdversary()
	{
		return handCardsViewAdversary;
	}

	public void setHandCardsViewAdversary(JLayeredPane handCardsViewAdversary)
	{
		this.handCardsViewAdversary = handCardsViewAdversary;
	}

	public JButton getBtnSendMessage()
	{
		return btnSendMessage;
	}

	public void setBtnSendMessage(JButton btnSendMessage)
	{
		this.btnSendMessage = btnSendMessage;
	}

	public JButton getSayUnoButtonPlayerTwo()
	{
		return sayUnoButtonPlayerTwo;
	}

	public void setSayUnoButtonPlayerTwo(JButton sayUnoButtonPlayerTwo)
	{
		this.sayUnoButtonPlayerTwo = sayUnoButtonPlayerTwo;
	}

	public JButton getSayUnoButtonPlayerOne()
	{
		return sayUnoButtonPlayerOne;
	}

	public void setSayUnoButtonPlayerOne(JButton sayUnoButtonPlayerOne)
	{
		this.sayUnoButtonPlayerOne = sayUnoButtonPlayerOne;
	}
}
