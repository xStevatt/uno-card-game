package unibs.pajc.uno.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel lblTitle;
	private JComboBox comboBoxPlayerOne;
	private JLabel playerOneLabel;
	private JLabel playerTwoLabel;
	private JComboBox comboBoxPlayerTwo;
	private JTextField txtFieldPlayerOne;
	private JTextField txtFieldPlayerTwo;

	public PlayerDetailsInfoSinglePlayer()
	{
		getContentPane().setLayout(null);

		this.setSize(460, 305);

		lblTitle = new JLabel("Insert your details!");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(12, 6, 438, 42);
		getContentPane().add(lblTitle);

		String options[] = { "Human", "Computer" };

		comboBoxPlayerOne = new JComboBox(options);
		comboBoxPlayerOne.setBounds(192, 132, 116, 60);
		getContentPane().add(comboBoxPlayerOne);

		playerOneLabel = new JLabel("Player 1");
		playerOneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerOneLabel.setBounds(64, 98, 116, 16);
		getContentPane().add(playerOneLabel);

		playerTwoLabel = new JLabel("Player 2");
		playerTwoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerTwoLabel.setBounds(64, 153, 116, 16);
		getContentPane().add(playerTwoLabel);

		comboBoxPlayerTwo = new JComboBox(options);
		comboBoxPlayerTwo.setBounds(192, 77, 116, 60);
		getContentPane().add(comboBoxPlayerTwo);

		comboBoxPlayerTwo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String selection = (String) ((JComboBox<?>) e.getSource()).getSelectedItem();

				switch (selection.trim())
				{
				case "Human":
					txtFieldPlayerOne.setVisible(true);
					break;
				case "Computer":
					break;
				}
			}
		});

		JButton btnStartGame = new JButton("Start game! ");
		btnStartGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String selectionPlayerOne = (String) comboBoxPlayerOne.getSelectedItem();
				String selectionPlayerTwo = (String) comboBoxPlayerTwo.getSelectedItem();

				AIPlayerController aicontroller = new AIPlayerController();
			}
		});
		btnStartGame.setBounds(166, 215, 116, 51);
		getContentPane().add(btnStartGame);

		JSeparator separator = new JSeparator();
		separator.setBounds(64, 191, 334, 12);
		getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(172, 53, 116, 12);
		getContentPane().add(separator_1);

		txtFieldPlayerOne = new JTextField();
		txtFieldPlayerOne.setHorizontalAlignment(SwingConstants.CENTER);
		txtFieldPlayerOne.setToolTipText("Insert your name");
		txtFieldPlayerOne.setText("Insert your name");
		txtFieldPlayerOne.setBounds(320, 93, 130, 26);
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

		txtFieldPlayerTwo = new JTextField();
		txtFieldPlayerTwo.setHorizontalAlignment(SwingConstants.CENTER);
		txtFieldPlayerTwo.setToolTipText("Insert your name");
		txtFieldPlayerTwo.setText("Insert your name");
		txtFieldPlayerTwo.setColumns(10);
		txtFieldPlayerTwo.setBounds(320, 148, 130, 26);
		txtFieldPlayerTwo.setVisible(false);

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
		getContentPane().add(txtFieldPlayerTwo);
	}
}
