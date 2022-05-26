package unibs.pajc.uno.view;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class PlayerDetailsInfoOffline extends JFrame
{

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					PlayerDetailsInfoOffline frame = new PlayerDetailsInfoOffline();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

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

		JLabel lblNewLabel = new JLabel("Insert your details");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(6, 6, 438, 29);
		contentPane.add(lblNewLabel);

		JSeparator separator = new JSeparator();
		separator.setBounds(16, 40, 428, 12);
		contentPane.add(separator);

		JPanel panel = new JPanel();
		panel.setBounds(26, 64, 398, 183);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Player 1 Name");
		lblNewLabel_1.setBounds(37, 33, 135, 16);
		panel.add(lblNewLabel_1);

		textField = new JTextField();
		textField.setBounds(184, 28, 186, 26);
		panel.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel_1_1 = new JLabel("Player 2 Name");
		lblNewLabel_1_1.setBounds(37, 71, 135, 16);
		panel.add(lblNewLabel_1_1);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(184, 66, 186, 26);
		panel.add(textField_1);

		JButton btnNewButton = new JButton("Start game");
		btnNewButton.setBounds(129, 135, 128, 42);
		panel.add(btnNewButton);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(37, 107, 333, 12);
		panel.add(separator_1);
	}
}
