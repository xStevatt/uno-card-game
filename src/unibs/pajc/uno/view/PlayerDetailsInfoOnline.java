package unibs.pajc.uno.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import unibs.pajc.uno.controller.net.NetClient;
import unibs.pajc.uno.controller.net.NetServer;

public class PlayerDetailsInfoOnline extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtIPAddress;
	private JTextField txtPort;
	private JLabel lblNewLabel_5;
	private JTextField txtName;

	private JSeparator separator;
	private JLabel lblNewLabel_2;
	private JButton btnNewGameButton;
	private JRadioButton rdbtnClient;
	private JLabel lblNewLabel;
	private JRadioButton rdbtnServer;

	String machineIP = null;

	public PlayerDetailsInfoOnline()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 305);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setVisible(true);

		separator = new JSeparator();
		separator.setBounds(24, 47, 404, 12);
		contentPane.add(separator);

		lblNewLabel_2 = new JLabel("Please insert your details.");
		lblNewLabel_2.setBounds(24, 19, 232, 16);
		contentPane.add(lblNewLabel_2);

		btnNewGameButton = new JButton("Start Game");
		btnNewGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int portNumber = 0;

				try
				{
					portNumber = Integer.parseInt(txtPort.getText());
				}
				catch (NumberFormatException e1)
				{
					JOptionPane.showMessageDialog(null, "Please insert a valid port!", "Error",
							JOptionPane.ERROR_MESSAGE);

					portNumber = 0;
				}

				if (rdbtnClient.isSelected())
				{
					if (portNumber != 0 || txtIPAddress.getText().length() == 0 || txtName.getText().length() > 0)
					{
						new NetClient(txtIPAddress.getText(), portNumber, txtName.getText());
						setVisible(false);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Please insert a valid data!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
				if (rdbtnServer.isSelected())
				{
					if (portNumber != 0 || txtIPAddress.getText().length() == 0 || txtName.getText().length() > 0)
					{
						new NetServer(portNumber, txtName.getText());
						setVisible(false);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Please insert a valid data!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnNewGameButton.setBounds(311, 14, 117, 29);
		contentPane.add(btnNewGameButton);

		lblNewLabel = new JLabel("Are you a client or a server?");
		lblNewLabel.setBounds(24, 69, 209, 16);
		contentPane.add(lblNewLabel);

		rdbtnClient = new JRadioButton("Client");
		rdbtnClient.setSelected(true);
		rdbtnClient.setBounds(246, 65, 79, 23);
		contentPane.add(rdbtnClient);

		rdbtnServer = new JRadioButton("Server");
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

		txtIPAddress = new JTextField();

		try
		{
			machineIP = Inet4Address.getLocalHost().toString();
		}
		catch (UnknownHostException e1)
		{
			System.out.println("Couldn't retrieve IP Address");
			e1.printStackTrace();
		}

		machineIP.trim();
		String splittedIP[] = machineIP.split("/");
		txtIPAddress.setText(splittedIP[splittedIP.length - 1]);

		txtIPAddress.setBounds(108, 23, 130, 31);
		panelSettingsClient.add(txtIPAddress);
		txtIPAddress.setColumns(10);

		lblNewLabel_5 = new JLabel("Port");
		lblNewLabel_5.setBounds(237, 20, 57, 36);
		panelSettingsClient.add(lblNewLabel_5);
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);

		txtPort = new JTextField();
		txtPort.setText("6761");
		txtPort.setBounds(306, 23, 85, 31);
		panelSettingsClient.add(txtPort);
		txtPort.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Your nickname:");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(58, 216, 150, 37);
		contentPane.add(lblNewLabel_1);

		txtName = new JTextField();
		txtName.setText("Player");
		txtName.setHorizontalAlignment(SwingConstants.CENTER);
		txtName.setBounds(220, 214, 208, 42);
		contentPane.add(txtName);
		txtName.setColumns(10);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(24, 186, 404, 12);
		contentPane.add(separator_1);

		rdbtnClient.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				txtIPAddress.setEditable(true);
				txtIPAddress.setEnabled(true);
				txtIPAddress.setText("127.0.0.1");
				txtName.setText("Client");
				panelSettingsClient.setBorder(new TitledBorder(null, "Edit client details", TitledBorder.LEADING,
						TitledBorder.TOP, null, null));
			}
		});

		rdbtnServer.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				txtIPAddress.setEditable(false);
				txtIPAddress.setEnabled(false);

				machineIP.trim();
				String splittedIP[] = machineIP.split("/");
				txtIPAddress.setText(splittedIP[splittedIP.length - 1]);

				txtName.setText("Server");
				panelSettingsClient.setBorder(new TitledBorder(null, "Edit server details", TitledBorder.LEADING,
						TitledBorder.TOP, null, null));
			}
		});

		rdbtnServer.doClick();
		this.setLocationRelativeTo(null);
	}
}
