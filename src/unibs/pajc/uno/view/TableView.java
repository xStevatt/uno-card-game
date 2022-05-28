package unibs.pajc.uno.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import unibs.pajc.uno.model.GameRules;
import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.player.HandCards;

public class TableView extends JFrame
{

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

	private JTextArea textArea;
	private JButton btnNewButton;

	private JLayeredPane handCardsViewActual;
	private JLayeredPane handCardsViewAdversary;

	private JTextArea textAreaChat;
	private JButton btnSendMessage;
	private JSeparator separator;

	private JButton sayUnoButtonPlayerTwo;
	private JButton sayUnoButtonPlayerOne;

	/**
	 * Constructor to create the form.
	 * 
	 * @param namePlayerOne
	 * @param namePlayerTwo
	 * @param firstRandomCard
	 */
	public TableView(String namePlayerOne, String namePlayerTwo, Card firstRandomCard)
	{
		super("Uno - cards game");
		setTitle("Uno - card's game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1221, 700);
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
		centerPanel.setBounds(6, 6, 948, 660);
		contentPane.add(centerPanel);
		centerPanel.setLayout(null);

		panelActualPlayer = new JPanel();
		panelActualPlayer
				.setBorder(new TitledBorder(null, namePlayerOne, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelActualPlayer.setBounds(6, 454, 936, 200);
		panelActualPlayer.setOpaque(false);

		handCardsViewActual = new JLayeredPane();
		handCardsViewActual.setPreferredSize(new Dimension(600, 175));
		handCardsViewActual.setOpaque(false);

		handCardsViewAdversary = new JLayeredPane();
		handCardsViewAdversary.setPreferredSize(new Dimension(600, 175));
		handCardsViewAdversary.setOpaque(false);

		centerPanel.add(panelActualPlayer);

		panelAdversaryPlayer = new JPanel();
		panelAdversaryPlayer.setOpaque(false);
		panelAdversaryPlayer
				.setBorder(new TitledBorder(null, namePlayerTwo, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelAdversaryPlayer.setBounds(6, 6, 936, 200);
		centerPanel.add(panelAdversaryPlayer);

		midTable = new JPanel();
		midTable.setOpaque(false);
		midTable.setBorder(new LineBorder(Color.DARK_GRAY));
		midTable.setBounds(6, 218, 936, 224);
		centerPanel.add(midTable);
		midTable.setLayout(null);

		sayUnoButtonPlayerOne = new JButton("SAY UNO!");
		sayUnoButtonPlayerOne.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{

			}
		});
		sayUnoButtonPlayerOne.setVisible(false);
		sayUnoButtonPlayerOne.setBounds(795, 179, 135, 39);
		midTable.add(sayUnoButtonPlayerOne);

		sayUnoButtonPlayerTwo = new JButton("SAY UNO!");
		sayUnoButtonPlayerTwo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{

			}
		});
		sayUnoButtonPlayerTwo.setVisible(false);
		sayUnoButtonPlayerTwo.setBounds(6, 6, 135, 39);
		midTable.add(sayUnoButtonPlayerTwo);

		JPanel panelDeck = new JPanel();
		// +5 is due to card's border
		panelDeck.setBounds(331, 30, 100 + 5, 150 + 5);
		midTable.add(panelDeck);
		panelDeck.setOpaque(false);

		panelDeck.add(new CardBackView());

		panelPlaced = new JPanel();
		// +5 is due to card's border
		panelPlaced.setBounds(471, 30, 100 + 5, 150 + 5);
		panelPlaced.add(new CardDropped(firstRandomCard));
		panelPlaced.setOpaque(false);
		midTable.add(panelPlaced);

		panelChat = new JPanel();
		panelChat.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Chat",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelChat.setBounds(966, 141, 249, 525);
		contentPane.add(panelChat);
		panelChat.setLayout(null);

		textAreaChat = new JTextArea();
		textAreaChat.setLineWrap(true);
		textAreaChat.setEditable(false);
		textAreaChat.setRows(1);
		textAreaChat.setBounds(16, 31, 216, 395);
		panelChat.add(textAreaChat);

		btnSendMessage = new JButton("Send");
		btnSendMessage.setBounds(159, 472, 73, 37);
		panelChat.add(btnSendMessage);

		txtSendMessageField = new JTextField();
		txtSendMessageField.setBounds(17, 468, 130, 43);
		panelChat.add(txtSendMessageField);
		txtSendMessageField.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBounds(16, 444, 216, 12);
		panelChat.add(separator);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(966, 6, 249, 124);
		contentPane.add(panel_1);
	}

	/**
	 * Sets "say uno" buttons invisible. "say uno" buttons are visible only when a
	 * player only has one card left. If the user doesn't say "uno" (doesn't press
	 * the button), two cards are added to his hand.
	 */
	public void makeSayUnoButtonVisibile()
	{
		sayUnoButtonPlayerOne.setVisible(true);
		sayUnoButtonPlayerTwo.setVisible(true);
	}

	/**
	 * Sets "say uno" buttons invisible. "say uno" buttons are visible only when a
	 * player only has one card left. If the user doesn't say "uno" (doesn't press
	 * the button), two cards are added to his hand.
	 */
	public void makeSayUnoButtonInvisible()
	{
		sayUnoButtonPlayerOne.setVisible(false);
		sayUnoButtonPlayerTwo.setVisible(false);
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
			addCardsToView(cards, panelActualPlayer, handCardsViewActual);
		}
		if (playingPlayer == 1)
		{
			addCardsToView(cards, panelAdversaryPlayer, handCardsViewAdversary);
		}
	}

	/**
	 * 
	 * @param handCards
	 * @param panelToAddCards
	 */
	public void addCardsToView(HandCards handCards, JPanel panelToAddCards, JLayeredPane cardsView)
	{
		cardsView.removeAll();

		Point originPoint = getFirstCardPoint(handCards.getNumberOfCards(), cardsView);
		int offset = calculateOffset(cardsView.getWidth(), handCards.getNumberOfCards());

		int i = 0;

		for (var card : handCards.getCardList())
		{
			var cardView = new CardView(card);

			cardView.setBounds(originPoint.x, originPoint.y, cardView.getDimension().width,
					cardView.getDimension().height);
			cardsView.add(cardView, i++);
			cardsView.moveToFront(cardView);

			originPoint.x += offset;
		}

		cardsView.revalidate();
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
		if (totalCards < GameRules.DEFAULT_NUMBER_OF_CARDS)
		{
			var width = handCardsView.getWidth() == 0 ? handCardsView.getPreferredSize().width
					: handCardsView.getWidth();

			var offset = calculateOffset(width, totalCards);
			p.x = (width - offset * totalCards) / 2;
		}
		return p;
	}

	/**
	 * 
	 * @param width
	 * @param totalCards
	 * @return
	 */
	private int calculateOffset(int width, int totalCards)
	{
		if (totalCards <= GameRules.DEFAULT_NUMBER_OF_CARDS)
		{
			return 71;
		}
		else
		{
			return (width - 100) / (totalCards - 1);
		}
	}
}
