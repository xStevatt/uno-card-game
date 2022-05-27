package unibs.pajc.uno.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.card.NumberCard;
import unibs.pajc.uno.model.player.HandCards;
import unibs.pajc.uno.model.player.Player;

public class Table extends JFrame
{

	private JPanel contentPane;
	private JTextField textField;

	// Most important panels (player one cards, player two cards and the center
	// panel)
	private JPanel panelPlayerOne;
	private JPanel panelPlayerTwo;
	private JPanel centerPanel;

	private JPanel midTable;
	private JPanel panel_1;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JTextArea textArea;
	private JButton btnNewButton;
	private JPanel panel;

	/**
	 * Create the frame.
	 */
	public Table()
	{
		super("Uno - cards game");
		setTitle("Uno - card's game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 700);
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
		panelPlayerOne.setOpaque(false);
		panelPlayerOne
				.setBorder(new TitledBorder(null, "Player 1", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPlayerOne.setBounds(6, 454, 936, 200);
		centerPanel.add(panelPlayerOne);

		panelPlayerTwo = new JPanel();
		panelPlayerTwo.setOpaque(false);
		panelPlayerTwo
				.setBorder(new TitledBorder(null, "Player 2", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPlayerTwo.setBounds(6, 6, 936, 200);
		centerPanel.add(panelPlayerTwo);

		midTable = new JPanel();
		midTable.setOpaque(false);
		midTable.setBorder(new LineBorder(Color.DARK_GRAY));
		midTable.setBounds(6, 218, 936, 224);
		centerPanel.add(midTable);

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Chat",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(966, 224, 228, 442);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setRows(1);
		textArea.setBounds(6, 32, 215, 339);
		panel_1.add(textArea);

		JButton btnNewButton = new JButton("Send");
		btnNewButton.setBounds(148, 407, 73, 29);
		panel_1.add(btnNewButton);

		textField = new JTextField();
		textField.setBounds(6, 407, 130, 26);
		panel_1.add(textField);
		textField.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBounds(5, 383, 216, 12);
		panel_1.add(separator);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Timer", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel.setBounds(966, 6, 228, 206);
		contentPane.add(panel);
		panel.setLayout(null);

		lblNewLabel = new JLabel("00:00:00");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(6, 112, 216, 41);
		panel.add(lblNewLabel);

		lblNewLabel_1 = new JLabel("You've been playing for:");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(6, 33, 216, 44);
		panel.add(lblNewLabel_1);

		loadCards();
	}

	public void loadTimer()
	{

	}

	public void loadCards()
	{
		for (int i = 0; i < 5; i++)
		{
			panelPlayerOne.add(new CardView(new NumberCard(CardColor.RED, i)));
			panelPlayerTwo.add(new CardView(new NumberCard(CardColor.GREEN, i)));
		}
	}

	public void loadCards(HandCards handCards, Player[] players, Player player)
	{
		for (int i = 0; i < handCards.getNumberOfCards(); i++)
		{

		}
	}

	public void addCardToView(Card card, Player[] players)
	{

	}
}
