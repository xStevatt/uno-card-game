package unibs.pajc.uno.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import unibs.pajc.uno.controller.AIPlayerController;

public class PlayerDetailsInfoSinglePlayer extends JFrame
{
	private JLabel lblTitle;
	private JComboBox comboBoxPlayerOne;
	private JLabel playerOneLabel;
	private JLabel playerTwoLabel;
	private JComboBox comboBoxPlayerTwo;
	private JTextField txtFieldPlayerOne;

	private enum PlayerType
	{
		COMPUTER, HUMAN;
	}

	public PlayerDetailsInfoSinglePlayer()
	{
		getContentPane().setLayout(null);

		this.setSize(500, 329);

		lblTitle = new JLabel("Insert your details!");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(12, 6, 482, 42);
		getContentPane().add(lblTitle);

		PlayerType options[] = { PlayerType.COMPUTER, PlayerType.HUMAN };

		comboBoxPlayerOne = new JComboBox(options);
		comboBoxPlayerOne.setEnabled(false);

		comboBoxPlayerOne.setSelectedIndex(0);

		comboBoxPlayerOne.setBounds(162, 132, 146, 60);
		getContentPane().add(comboBoxPlayerOne);

		playerOneLabel = new JLabel("Player 1");
		playerOneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerOneLabel.setBounds(34, 98, 116, 16);
		getContentPane().add(playerOneLabel);

		playerTwoLabel = new JLabel("Player 2");
		playerTwoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerTwoLabel.setBounds(34, 153, 116, 16);
		getContentPane().add(playerTwoLabel);

		comboBoxPlayerTwo = new JComboBox(options);
		comboBoxPlayerTwo.setSelectedIndex(1);
		comboBoxPlayerTwo.setBounds(162, 77, 146, 60);
		getContentPane().add(comboBoxPlayerTwo);

		comboBoxPlayerTwo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				PlayerType selection = (PlayerType) ((JComboBox<?>) e.getSource()).getSelectedItem();

				switch (selection)
				{
				case HUMAN:
					txtFieldPlayerOne.setVisible(true);
					break;
				case COMPUTER:
					txtFieldPlayerOne.setVisible(false);
					break;
				}
			}
		});

		JButton btnStartGame = new JButton("Start game! ");
		btnStartGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);

				AIPlayerController aicontroller = new AIPlayerController(txtFieldPlayerOne.getText().trim(), "");
			}
		});
		btnStartGame.setBounds(192, 217, 116, 51);
		getContentPane().add(btnStartGame);

		JSeparator separator = new JSeparator();
		separator.setBounds(64, 191, 386, 12);
		getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(192, 60, 116, 12);
		getContentPane().add(separator_1);

		txtFieldPlayerOne = new JTextField();
		txtFieldPlayerOne.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				txtFieldPlayerOne.setText("");
			}
		});
		txtFieldPlayerOne.setHorizontalAlignment(SwingConstants.CENTER);
		txtFieldPlayerOne.setToolTipText("Insert your name");
		txtFieldPlayerOne.setText("Insert your name");
		txtFieldPlayerOne.setBounds(320, 87, 130, 42);
		getContentPane().add(txtFieldPlayerOne);
		txtFieldPlayerOne.setColumns(10);
		txtFieldPlayerOne.setVisible(false);

		txtFieldPlayerOne.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				txtFieldPlayerOne.requestFocus();
				txtFieldPlayerOne.setText("");
			}
		});

		txtFieldPlayerOne.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				txtFieldPlayerOne.requestFocus();
				txtFieldPlayerOne.setText("");
			}
		});

		txtFieldPlayerOne.setVisible(true);
	}
}
