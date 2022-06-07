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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
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
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.player.HandCards;

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

	public static JTextArea textAreaChat;
	private JButton btnSendMessage;
	private JSeparator separatorChat;

	private JButton sayUnoButtonPlayerTwo;
	private JButton sayUnoButtonPlayerOne;
	private JLabel titleLabel;
	private JLabel lblMatchDescriptor;

	private JLabel lblStopWatch;

	private boolean isGameLocal = true;

	private boolean unoButtonPressed = false;

	private JLabel turnFinalLabel;
	private JLabel lblPlayerTurn;

	public static String message = "";

	private JSeparator separatorInfo1;
	private JSeparator separatorInfo2;
	private JScrollBar scrollBar;

	private JPanel panelInfo;
	private JSeparator separator;
	private JScrollPane scroll;

	private volatile int status = 1;

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
		this.setLocationRelativeTo(null);

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
				unoButtonPressed = true;
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
				unoButtonPressed = true;
				System.out.println("UNO!");
			}
		});
		sayUnoButtonPlayerTwo.setVisible(false);
		sayUnoButtonPlayerTwo.setBounds(6, 6, 135, 39);
		midTable.add(sayUnoButtonPlayerTwo);

		panelPlaced = new JPanel(new GridBagLayout());
		panelPlaced.setBounds(469, 28, 105, 155);
		panelPlaced.setOpaque(false);
		midTable.add(panelPlaced);

		panelDeck = new JPanel();
		panelDeck.setBounds(319, 28, 105, 155);
		midTable.add(panelDeck);
		panelDeck.setOpaque(false);

		panelDeck.add(new CardBackView(true));

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
		btnSendMessage.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String messageInput = txtSendMessageField.getText();

				if (messageInput.length() > 0)
				{
					txtSendMessageField.setText("");
					addChatMessage(messageInput, "You");
					TableView.message = messageInput;
				}
			}
		});
		btnSendMessage.setBounds(159, 405, 73, 37);

		panelChat.add(btnSendMessage);

		txtSendMessageField = new JTextField();
		txtSendMessageField.setBounds(17, 401, 130, 43);
		panelChat.add(txtSendMessageField);
		txtSendMessageField.setColumns(10);

		scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		textAreaChat.add(scroll);

		separator = new JSeparator();
		separator.setBounds(17, 377, 216, 12);
		panelChat.add(separator);

		scrollBar = new JScrollBar();
		scrollBar.setBounds(218, 24, 15, 341);
		panelChat.add(scrollBar);

		panelInfo = new JPanel();
		panelInfo.setBorder(new LineBorder(Color.DARK_GRAY));
		panelInfo.setBounds(1045, 6, 249, 183);
		contentPane.add(panelInfo);
		panelInfo.setLayout(null);

		titleLabel = new JLabel("Uno Game");
		titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 18));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(6, 14, 237, 23);
		panelInfo.add(titleLabel);

		lblMatchDescriptor = new JLabel(namePlayerOne + " vs " + namePlayerTwo);
		lblMatchDescriptor.setHorizontalAlignment(SwingConstants.CENTER);
		lblMatchDescriptor.setBounds(16, 73, 227, 16);
		panelInfo.add(lblMatchDescriptor);

		separatorInfo1 = new JSeparator();
		separatorInfo1.setBounds(16, 49, 227, 12);
		panelInfo.add(separatorInfo1);

		lblStopWatch = new JLabel("00:00:00");
		lblStopWatch.setHorizontalAlignment(SwingConstants.CENTER);
		lblStopWatch.setBounds(6, 101, 237, 16);
		panelInfo.add(lblStopWatch);

		separatorInfo2 = new JSeparator();
		separatorInfo2.setBounds(16, 126, 227, 12);
		panelInfo.add(separatorInfo2);

		lblPlayerTurn = new JLabel("");
		lblPlayerTurn.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblPlayerTurn.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerTurn.setBounds(106, 150, 137, 27);
		panelInfo.add(lblPlayerTurn);

		turnFinalLabel = new JLabel("Turn:");
		turnFinalLabel.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		turnFinalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		turnFinalLabel.setBounds(6, 150, 88, 27);
		panelInfo.add(turnFinalLabel);

		if (isGameLocal)
		{
			btnSendMessage.setEnabled(false);
			txtSendMessageField.setEditable(false);
			textAreaChat.setEnabled(true);
			scroll.setEnabled(false);

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

	public void setMatchLabel(String playerCurrent, String playerAdversary)
	{
		lblMatchDescriptor.setText(playerCurrent + " vs " + playerAdversary);
	}

	/**
	 * 
	 * @param playerOne
	 * @param playerTwo
	 * @param lastCardUsed
	 * @param currentPlayer
	 */

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
	 * Replaces the cards dropped on the table
	 * 
	 * @param card
	 */
	public synchronized void changeDroppedCardView(Card card, CardColor currentColor)
	{
		panelPlaced.removeAll();
		panelPlaced.repaint();

		UsedCardView cardToAdd = new UsedCardView(card);

		panelPlaced.add(cardToAdd);
		panelPlaced.repaint();

		Color color = Util.convertCardColor(card.getCardColor());

		midTable.setOpaque(true);

		if (color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() == 0)
		{
			color = Util.convertCardColor(currentColor);
			midTable.setBorder(new LineBorder(color, 6));
			midTable.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
			panelPlaced.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
		}
		else
		{
			midTable.setBorder(new LineBorder(color, 6));
			midTable.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
			panelPlaced.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
		}

		midTable.repaint();
		panelPlaced.repaint();
		this.repaint();
	}

	/**
	 * 
	 * @param currentTurn
	 */
	public void setTurn(String currentTurn)
	{
		lblPlayerTurn.setText(currentTurn);
	}

	/**
	 * 
	 * @param index
	 */
	public synchronized void enableViewPlayer(int index, boolean enabled)
	{
		if (isGameLocal == true)
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
		else if (isGameLocal == false)
		{
			if (index == 0)
			{
				for (int i = 0; i < handCardsViewActual.getComponentCount(); i++)
				{
					((CardView) handCardsViewActual.getComponent(i)).setShouldAnimationsMove(enabled);
				}
			}
		}

		repaint();
	}

	/**
	 * Sets "say uno" buttons invisible. "say uno" buttons are visible only when a
	 * player only has one card left. If the user doesn't say "uno" (doesn't press
	 * the button), two cards are added to his hand.
	 */
	public synchronized void setSayUnoButtonVisibile(boolean visibility, int indexPlayer)
	{
		if (indexPlayer == 0)
		{
			sayUnoButtonPlayerOne.setVisible(visibility);

		}
		if (indexPlayer == 1)
		{
			sayUnoButtonPlayerTwo.setVisible(visibility);

		}

		panelAdversaryPlayer.repaint();
		panelAdversaryPlayer.repaint();
		handCardsViewActual.repaint();
		panelActualPlayer.repaint();
	}

	/**
	 * 
	 * Method to load all cards.
	 * 
	 * @param handCards
	 * @param players
	 */
	public synchronized void loadCards(HandCards cards, int playingPlayer)
	{
		if (isGameLocal == true)
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
		}
		else if (isGameLocal == false)
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

				addCardsToViewBack(cards, panelAdversaryPlayer, handCardsViewAdversary);
			}
		}

		// REPAINTS ALL COMPONENTS
		panelAdversaryPlayer.repaint();
		panelAdversaryPlayer.repaint();
		handCardsViewActual.repaint();
		panelActualPlayer.repaint();
	}

	/**
	 * Adds cards of the players
	 * 
	 * @param handCards
	 * @param panelToAddCards
	 */
	public synchronized void addCardsToView(HandCards handCards, JPanel panelToAddCards, JLayeredPane cardsView)
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
	 * Adds cards of the adversary player (reversed cards)
	 * 
	 * @param handCards
	 * @param panelToAddCards
	 * @param cardsView
	 */
	public synchronized void addCardsToViewBack(HandCards handCards, JPanel panelToAddCards, JLayeredPane cardsView)
	{
		Point originPoint = getFirstCardPoint(handCards.getNumberOfCards(), cardsView);
		int offset = calculateOffset(cardsView.getWidth(), handCards.getNumberOfCards());

		int i = 0;

		for (int j = 0; j < handCards.getNumberOfCards(); j++)
		{
			CardBackView cardView = new CardBackView(false);

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
	private synchronized Point getFirstCardPoint(int totalCards, JLayeredPane handCardsView)
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
	private synchronized int calculateOffset(int width, int totalCards)
	{
		if (totalCards <= GameRules.DEFAULT_NUMBER_OF_CARDS)
		{
			return 120;
		}
		else
		{
			return (width - 100) / (totalCards - 1);
		}
	}

	/**
	 * 
	 * @param message
	 * @param playerName
	 */
	public synchronized void addChatMessage(String message, String playerName)
	{
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm");
		String formattedDate = time.format(myFormatObj);

		textAreaChat.append("\n" + formattedDate + " " + playerName + ": " + message);
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

	public boolean isUnoButtonPressed()
	{
		return unoButtonPressed;
	}

	public void setUnoButtonPressed(boolean unoButtonPressed)
	{
		this.unoButtonPressed = unoButtonPressed;
	}

	public String getMessage()
	{
		return message;
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