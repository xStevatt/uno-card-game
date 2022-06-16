package unibs.pajc.uno.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class PlayerDetailsInfoSinglePlayer extends JFrame
{
	public PlayerDetailsInfoSinglePlayer()
	{
		getContentPane().setLayout(null);

		JLabel lblTitle = new JLabel("Insert your details!");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(6, 6, 438, 42);
		getContentPane().add(lblTitle);
	}
}
