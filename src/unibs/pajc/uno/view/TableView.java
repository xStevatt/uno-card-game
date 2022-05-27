package unibs.pajc.uno.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

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
import unibs.pajc.uno.model.player.Player;

public class TableView extends JFrame
{

	private JPanel contentPane;
	private JTextField txtSendMessageField;

	// Most important panels (player one cards, player two cards and the center
	// panel)
	private JPanel panelPlayerOne;
	private JPanel panelPlayerTwo;
	private JPanel centerPanel;

	private JPanel midTable;
	private JPanel panelChat;
	private JTextArea textArea;
	private JButton btnNewButton;
	private JPanel panel;

	private JLayeredPane handCardsView;
	JTextArea textAreaChat;
	JButton btnSendMessage;
	JSeparator separator;

	JButton sayUnoButtonPlayerTwo;
	JButton sayUnoButtonPlayerOne;
	private JPanel panelPlaced;

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

		panelPlayerOne = new JPanel();
		panelPlayerOne
				.setBorder(new TitledBorder(null, namePlayerOne, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPlayerOne.setBounds(6, 454, 936, 200);
		panelPlayerOne.setOpaque(false);

		handCardsView = new JLayeredPane();
		handCardsView.setPreferredSize(new Dimension(600, 175));
		handCardsView.setOpaque(false);

		centerPanel.add(panelPlayerOne);

		panelPlayerTwo = new JPanel();
		panelPlayerTwo.setOpaque(false);
		panelPlayerTwo
				.setBorder(new TitledBorder(null, namePlayerTwo, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPlayerTwo.setBounds(6, 6, 936, 200);
		centerPanel.add(panelPlayerTwo);

		midTable = new JPanel();
		midTable.setOpaque(false);
		midTable.setBorder(new LineBorder(Color.DARK_GRAY));
		midTable.setBounds(6, 218, 936, 224);
		centerPanel.add(midTable);
		midTable.setLayout(null);

		sayUnoButtonPlayerOne = new JButton("SAY UNO!");
		sayUnoButtonPlayerOne.setVisible(false);
		sayUnoButtonPlayerOne.setBounds(795, 179, 135, 39);
		midTable.add(sayUnoButtonPlayerOne);

		sayUnoButtonPlayerTwo = new JButton("SAY UNO!");
		sayUnoButtonPlayerTwo.setVisible(false);
		sayUnoButtonPlayerTwo.setBounds(795, 6, 135, 39);
		midTable.add(sayUnoButtonPlayerTwo);

		JPanel panelDeck = new JPanel();
		// +5 is due to card's border
		panelDeck.setBounds(314, 30, 100 + 5, 150 + 5);
		midTable.add(panelDeck);
		panelDeck.setOpaque(false);

		panelDeck.add(new CardBackView());

		panelPlaced = new JPanel();
		// +5 is due to card's border
		panelPlaced.setBounds(443, 30, 100 + 5, 150 + 5);
		panelPlaced.add(new CardDropped(firstRandomCard));
		panelPlaced.setOpaque(false);
		midTable.add(panelPlaced);

		panelChat = new JPanel();
		panelChat.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Chat",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelChat.setBounds(966, 6, 249, 660);
		contentPane.add(panelChat);
		panelChat.setLayout(null);

		textAreaChat = new JTextArea();
		textAreaChat.setLineWrap(true);
		textAreaChat.setEditable(false);
		textAreaChat.setRows(1);
		textAreaChat.setBounds(16, 32, 216, 523);
		panelChat.add(textAreaChat);

		btnSendMessage = new JButton("Send");
		btnSendMessage.setBounds(159, 595, 73, 37);
		panelChat.add(btnSendMessage);

		txtSendMessageField = new JTextField();
		txtSendMessageField.setBounds(17, 591, 130, 43);
		panelChat.add(txtSendMessageField);
		txtSendMessageField.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBounds(16, 567, 216, 12);
		panelChat.add(separator);
	}

	/**
	 * 
	 */
	public void makeSayUnoButtonVisibile()
	{
		sayUnoButtonPlayerOne.setVisible(true);
		sayUnoButtonPlayerTwo.setVisible(true);
	}

	/**
	 * 
	 */
	public void makeSayUnoButtonInvisible()
	{
		sayUnoButtonPlayerOne.setVisible(false);
		sayUnoButtonPlayerTwo.setVisible(false);
	}

	/**
	 * 
	 * @param handCards
	 */
	public void loadCards(HandCards handCards)
	{
		handCardsView.removeAll();

		Point originPoint = getFirstCardPoint(handCards.getNumberOfCards());
		int offset = calculateOffset(handCardsView.getWidth(), handCards.getNumberOfCards());

		int i = 0;

		for (var card : handCards.getCardList())
		{
			var cardView = new CardView(card);

			cardView.setBounds(originPoint.x, originPoint.y, cardView.getDimension().width,
					cardView.getDimension().height);
			handCardsView.add(cardView, i++);
			handCardsView.moveToFront(cardView);

			originPoint.x += offset;
		}

		handCardsView.revalidate();
		panelPlayerOne.add(handCardsView);
	}

	/**
	 * 
	 * @param totalCards
	 * @return
	 */
	private Point getFirstCardPoint(int totalCards)
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

	/**
	 * 
	 * @param handCards
	 * @param players
	 */
	public void loadCards(HandCards handCards, Player[] players)
	{
		for (int i = 0; i < handCards.getNumberOfCards(); i++)
		{

		}
	}

	/**
	 * 
	 */
	public void addCardToView()
	{
		panelPlayerOne.add(new CardBackView());
	}
}
