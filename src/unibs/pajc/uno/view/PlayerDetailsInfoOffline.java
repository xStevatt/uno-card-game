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

/**
 * 
 * Pannello per l'inseriemento del gioco in multiplayer offline.
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
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
		setBounds(100, 100, 470, 335);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitle = new JLabel("Insert your details");
		lblTitle.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(6, 21, 458, 29);
		contentPane.add(lblTitle);

		JSeparator separator = new JSeparator();
		separator.setBounds(24, 51, 420, 12);
		contentPane.add(separator);

		JPanel panel = new JPanel();
		panel.setBounds(26, 75, 420, 208);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblPlayerOneName = new JLabel("Player 1 Name");
		lblPlayerOneName.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblPlayerOneName.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerOneName.setBounds(16, 14, 179, 26);
		panel.add(lblPlayerOneName);

		txtPlayerOne = new JTextField();
		txtPlayerOne.setHorizontalAlignment(SwingConstants.CENTER);
		txtPlayerOne.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		txtPlayerOne.setBounds(207, 6, 186, 42);
		panel.add(txtPlayerOne);
		txtPlayerOne.setColumns(10);

		JLabel lblPlayerTwoName = new JLabel("Player 2 Name");
		lblPlayerTwoName.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblPlayerTwoName.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerTwoName.setBounds(16, 84, 179, 16);
		panel.add(lblPlayerTwoName);

		txtPlayerTwo = new JTextField();
		txtPlayerTwo.setHorizontalAlignment(SwingConstants.CENTER);
		txtPlayerTwo.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		txtPlayerTwo.setColumns(10);
		txtPlayerTwo.setBounds(207, 72, 186, 41);
		panel.add(txtPlayerTwo);

		JButton btnStartNewGame = new JButton("Start game!");
		btnStartNewGame.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
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
		btnStartNewGame.setBounds(144, 155, 128, 53);
		panel.add(btnStartNewGame);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(6, 125, 408, 12);
		panel.add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(6, 52, 408, 12);
		panel.add(separator_2);

		this.setLocationRelativeTo(null);
	}
}
