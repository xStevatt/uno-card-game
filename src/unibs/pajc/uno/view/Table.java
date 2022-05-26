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

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.player.Player;

public class Table extends JFrame
{

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public Table()
	{
		super("Uno - cards game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 980, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel()
		{
			@Override
			public void paintComponent(Graphics g)
			{
				ImageIcon wallpaper = new ImageIcon("res/img/wallpaper.png");
				Image i = wallpaper.getImage();

				g.drawImage(i, 0, 0, this.getSize().width, this.getSize().height, this);
			}
		};
		panel.setBackground(Color.GREEN);
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBounds(6, 6, 728, 460);
		contentPane.add(panel);
		panel.setLayout(null);

		JPanel panelPlayerTwo = new JPanel();
		panelPlayerTwo.setOpaque(false);
		panelPlayerTwo.setBorder(new LineBorder(Color.DARK_GRAY));
		panelPlayerTwo.setBounds(6, 6, 716, 117);
		panel.add(panelPlayerTwo);

		JPanel panelPlayerOne = new JPanel();
		panelPlayerOne.setOpaque(false);
		panelPlayerOne.setBorder(new LineBorder(Color.DARK_GRAY));
		panelPlayerOne.setBounds(6, 337, 716, 117);
		panel.add(panelPlayerOne);

		JPanel midTable = new JPanel();
		midTable.setOpaque(false);
		midTable.setBorder(new LineBorder(Color.DARK_GRAY));
		midTable.setBounds(6, 135, 716, 190);
		panel.add(midTable);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(64, 64, 64), 1, true));
		panel_1.setBounds(746, 6, 228, 460);
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
	}

	public void addCardToView(Card card, Player player)
	{

	}
}
