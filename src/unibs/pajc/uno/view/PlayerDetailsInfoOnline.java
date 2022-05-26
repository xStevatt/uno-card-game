package unibs.pajc.uno.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class PlayerDetailsInfoOnline extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_2;
	private JTextField textField_5;
	private JLabel lblNewLabel_5;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public PlayerDetailsInfoOnline()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 290);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JSeparator separator = new JSeparator();
		separator.setBounds(24, 47, 404, 12);
		contentPane.add(separator);

		JLabel lblNewLabel_2 = new JLabel("Please insert your details.");
		lblNewLabel_2.setBounds(24, 19, 232, 16);
		contentPane.add(lblNewLabel_2);

		JButton btnNewButton = new JButton("Start Game");
		btnNewButton.setBounds(311, 14, 117, 29);
		contentPane.add(btnNewButton);

		JLabel lblNewLabel = new JLabel("Are you a client or a server?");
		lblNewLabel.setBounds(24, 69, 209, 16);
		contentPane.add(lblNewLabel);

		JRadioButton rdbtnClient = new JRadioButton("Client");
		rdbtnClient.setSelected(true);
		rdbtnClient.setBounds(246, 65, 79, 23);
		contentPane.add(rdbtnClient);

		JRadioButton rdbtnServer = new JRadioButton("Server");
		rdbtnServer.setBounds(338, 65, 79, 23);
		contentPane.add(rdbtnServer);

		ButtonGroup clientAndServerGroup = new ButtonGroup();
		clientAndServerGroup.add(rdbtnServer);
		clientAndServerGroup.add(rdbtnClient);

		JPanel panelSettingsClient = new JPanel();
		panelSettingsClient.setBorder(
				new TitledBorder(null, "Edit client details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSettingsClient.setBounds(16, 107, 422, 67);
		contentPane.add(panelSettingsClient);
		panelSettingsClient.setLayout(null);

		JLabel lblNewLabel_1_1 = new JLabel("IP Address");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setBounds(6, 20, 100, 36);
		panelSettingsClient.add(lblNewLabel_1_1);

		textField_2 = new JTextField();
		textField_2.setText("127.0.0.1");
		textField_2.setBounds(108, 23, 130, 31);
		panelSettingsClient.add(textField_2);
		textField_2.setColumns(10);

		lblNewLabel_5 = new JLabel("Port");
		lblNewLabel_5.setBounds(237, 20, 57, 36);
		panelSettingsClient.add(lblNewLabel_5);
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);

		textField_5 = new JTextField();
		textField_5.setText("6761");
		textField_5.setBounds(306, 23, 85, 31);
		panelSettingsClient.add(textField_5);
		textField_5.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Your nickname:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_1.setBounds(91, 219, 117, 16);
		contentPane.add(lblNewLabel_1);

		textField = new JTextField();
		textField.setBounds(220, 214, 130, 26);
		contentPane.add(textField);
		textField.setColumns(10);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(24, 186, 404, 12);
		contentPane.add(separator_1);

		rdbtnClient.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				panelSettingsClient.setBorder(new TitledBorder(null, "Edit client details", TitledBorder.LEADING,
						TitledBorder.TOP, null, null));
			}
		});

		rdbtnServer.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				panelSettingsClient.setBorder(new TitledBorder(null, "Edit server details", TitledBorder.LEADING,
						TitledBorder.TOP, null, null));
			}
		});

		this.setLocationRelativeTo(null);
	}
}
