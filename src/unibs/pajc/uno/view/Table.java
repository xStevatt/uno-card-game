package unibs.pajc.uno.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.card.NumberCard;
import unibs.pajc.uno.model.player.Player;

public class Table extends JFrame
{

	private JPanel contentPane;
	private JTextField textField;

	private JPanel panelPlayerOne;
	private JPanel panelPlayerTwo;
	private JPanel centerPanel;

	/**
	 * Create the frame.
	 */
	public Table()
	{
		super("Uno - cards game");
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

		JPanel midTable = new JPanel();
		midTable.setOpaque(false);
		midTable.setBorder(new LineBorder(Color.DARK_GRAY));
		midTable.setBounds(6, 218, 936, 224);
		centerPanel.add(midTable);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(64, 64, 64), 1, true));
		panel_1.setBounds(966, 206, 228, 460);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setRows(1);
		textArea.setBounds(6, 6, 216, 386);
		panel_1.add(textArea);

		JButton btnNewButton = new JButton("Send");
		btnNewButton.setBounds(149, 425, 73, 29);
		panel_1.add(btnNewButton);

		textField = new JTextField();
		textField.setBounds(7, 425, 130, 26);
		panel_1.add(textField);
		textField.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBounds(6, 404, 216, 12);
		panel_1.add(separator);

		loadCards();
	}

	public void loadCards()
	{
		for (int i = 0; i < 5; i++)
		{
			panelPlayerOne.add(new CardView(new NumberCard(CardColor.RED, i)));
			panelPlayerTwo.add(new CardView(new NumberCard(CardColor.GREEN, i)));
		}
	}

	public void addCardToView(Card card, Player player)
	{

	}
}
