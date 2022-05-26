package unibs.pajc.uno.view;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Table extends JFrame
{

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public Table()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(6, 6, 650, 460);
		contentPane.add(panel);
		panel.setLayout(null);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.DARK_GRAY));
		panel_2.setBounds(6, 6, 638, 117);
		panel.add(panel_2);

		JPanel panel_2_1 = new JPanel();
		panel_2_1.setBorder(new LineBorder(Color.DARK_GRAY));
		panel_2_1.setBounds(6, 337, 638, 117);
		panel.add(panel_2_1);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(Color.DARK_GRAY));
		panel_3.setBounds(6, 135, 638, 190);
		panel.add(panel_3);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.DARK_GRAY));
		panel_1.setBounds(666, 6, 228, 460);
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
}
