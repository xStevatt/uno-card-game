package unibs.pajc.uno.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class PlayerDetailsInfoSinglePlayer extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlayerDetailsInfoSinglePlayer()
	{
		getContentPane().setLayout(null);

		this.setSize(460, 305);

		JLabel lblTitle = new JLabel("Insert your details!");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(6, 6, 438, 42);
		getContentPane().add(lblTitle);

		String options[] = { "Human", "Computer" };

		JComboBox comboBox_1 = new JComboBox(options);
		comboBox_1.setBounds(192, 132, 206, 60);
		getContentPane().add(comboBox_1);

		JLabel playerOneLabel = new JLabel("Player 1");
		playerOneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerOneLabel.setBounds(64, 98, 116, 16);
		getContentPane().add(playerOneLabel);

		JLabel playerTwoLabel = new JLabel("Player 2");
		playerTwoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerTwoLabel.setBounds(64, 153, 116, 16);
		getContentPane().add(playerTwoLabel);

		JComboBox comboBox_1_1 = new JComboBox(options);
		comboBox_1_1.setBounds(192, 77, 206, 60);
		getContentPane().add(comboBox_1_1);

		JButton btnStartGame = new JButton("Start game! ");
		btnStartGame.setBounds(166, 215, 116, 51);
		getContentPane().add(btnStartGame);

		JSeparator separator = new JSeparator();
		separator.setBounds(64, 191, 334, 12);
		getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(166, 53, 116, 12);
		getContentPane().add(separator_1);
	}
}
