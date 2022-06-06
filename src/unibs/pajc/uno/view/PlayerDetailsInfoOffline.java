package unibs.pajc.uno.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import unibs.pajc.uno.controller.LocalPlayerController;

public class PlayerDetailsInfoOffline extends JFrame
{
	private JPanel contentPane;
	private JTextField txtPlayerOne;
	private JTextField txtPlayerTwo;

	/**
	 * Create the frame.
	 */
	public PlayerDetailsInfoOffline()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitle = new JLabel("Insert your details");
		lblTitle.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(6, 6, 438, 29);
		contentPane.add(lblTitle);

		JSeparator separator = new JSeparator();
		separator.setBounds(16, 40, 428, 12);
		contentPane.add(separator);

		JPanel panel = new JPanel();
		panel.setBounds(26, 64, 398, 183);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblPlayerOneName = new JLabel("Player 1 Name");
		lblPlayerOneName.setBounds(37, 33, 135, 16);
		panel.add(lblPlayerOneName);

		txtPlayerOne = new JTextField();
		txtPlayerOne.setBounds(184, 28, 186, 26);
		panel.add(txtPlayerOne);
		txtPlayerOne.setColumns(10);

		JLabel lblPlayerTwoName = new JLabel("Player 2 Name");
		lblPlayerTwoName.setBounds(37, 71, 135, 16);
		panel.add(lblPlayerTwoName);

		txtPlayerTwo = new JTextField();
		txtPlayerTwo.setColumns(10);
		txtPlayerTwo.setBounds(184, 66, 186, 26);
		panel.add(txtPlayerTwo);

		JButton btnStartNewGame = new JButton("Start game");
		btnStartNewGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String playerOneNameString = txtPlayerOne.getText();
				String playerTwoNameString = txtPlayerTwo.getText();

				if (playerOneNameString.length() > 0 && playerTwoNameString.length() > 0)
				{
					setVisible(false);

					new LocalPlayerController(playerOneNameString, playerTwoNameString);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Please insert a valid name!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnStartNewGame.setBounds(129, 135, 128, 42);
		panel.add(btnStartNewGame);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(37, 107, 333, 12);
		panel.add(separator_1);

		this.setLocationRelativeTo(null);
	}
}
