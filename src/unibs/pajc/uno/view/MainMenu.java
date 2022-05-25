package unibs.pajc.uno.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Enum describing card types
 * 
 * @author Stefano Valloncini, Yuhang Ye, Luigi Amarante
 *
 */
public class MainMenu extends JFrame
{
	private static final String TITLE = "UnoPACJ, an opensource uno game made in Java";

	private JPanel bannerPanel;
	private JLabel bannerLabel;

	private JPanel buttonsPanel;
	private JPanel mainContainer;

	private JPanel newGameButtonPanel;
	private JButton newLocalGameButton;

	private JPanel newOnlineGamePanel;
	private JButton newOnlineGameButton;

	private JPanel AIGamePanel;
	private JButton AIGameButton;

	private PlayerDetailsInfo playerInfoFrame; // new frame to input players' info

	public MainMenu()
	{
		super("Uno");
		loadInterface();
	}

	protected void loadInterface()
	{
		initializeBannerPanel();
		initializeButtonsPanel();

		this.setLayout(new BorderLayout());
		this.add(bannerPanel, BorderLayout.NORTH);
		// this.add(buttonsPanel, BorderLayout.SOUTH);
		this.add(mainContainer, BorderLayout.SOUTH);

		this.pack();

		Image bannerImage = new ImageIcon("res/img/main_icon.png").getImage().getScaledInstance(500, 500,
				java.awt.Image.SCALE_SMOOTH);

		this.setIconImage(bannerImage);
		this.setLocationRelativeTo(null);
	}

	protected void initializeBannerPanel()
	{
		Image bannerImage = new ImageIcon("res/img/main_banner.png").getImage().getScaledInstance(350, 250,
				java.awt.Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(bannerImage);

		bannerLabel = new JLabel(""); // Add text to main banner
		new JLabel("A chess game in Java");

		bannerLabel.setForeground(new Color(20, 20, 20));
		bannerLabel.setIcon(image);
		bannerLabel.setIconTextGap(25);

		bannerPanel = new JPanel();
		bannerPanel.setPreferredSize(new Dimension(600, 250));
		bannerPanel.add(bannerLabel);
		// bannerPanel.add(bannerLabelSubtitle);
		bannerPanel.setBackground(new Color(237, 28, 36));
	}

	protected void initializeButtonsPanel()
	{
		newLocalGameButton = new JButton("New Online Game");
		newOnlineGameButton = new JButton("New Offline Game");
		AIGameButton = new JButton("AI Simulated");

		newGameButtonPanel = new JPanel(new GridLayout());
		newGameButtonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 40, 25));
		newGameButtonPanel.add(newLocalGameButton);

		newOnlineGamePanel = new JPanel(new GridLayout(1, 1));
		newOnlineGamePanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 40, 25));
		newOnlineGamePanel.add(newOnlineGameButton);

		AIGamePanel = new JPanel(new GridLayout());
		AIGamePanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 40, 40));
		AIGamePanel.add(AIGameButton);

		buttonsPanel = new JPanel(new GridLayout(1, 3));
		buttonsPanel.setPreferredSize(new Dimension(600, 150));
		buttonsPanel.add(newGameButtonPanel);
		buttonsPanel.add(newOnlineGamePanel);
		buttonsPanel.add(AIGamePanel);

		JLabel descriptorLabel = new JLabel("UnoPAJC, an opensource uno card game written in Java",
				SwingConstants.CENTER);
		JLabel authorLabel = new JLabel("Made by Stefano Valloncini, Yuhang Ye, Luigi Amarante", SwingConstants.CENTER);

		JPanel descriptorPanelNorth = new JPanel();
		descriptorPanelNorth.setBorder(BorderFactory.createEmptyBorder(10, 25, 1, 40));
		JPanel descriptionPaneSouth = new JPanel();
		descriptionPaneSouth.setBorder(BorderFactory.createEmptyBorder(1, 25, 1, 40));

		descriptorPanelNorth.add(descriptorLabel);
		descriptionPaneSouth.add(authorLabel);

		mainContainer = new JPanel(new BorderLayout());
		mainContainer.add(descriptorPanelNorth, BorderLayout.NORTH);
		mainContainer.add(buttonsPanel, BorderLayout.CENTER);
		mainContainer.add(descriptionPaneSouth, BorderLayout.SOUTH);

		newLocalGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Start a new game!");

				setVisible(false);
				playerInfoFrame = new PlayerDetailsInfo();

				playerInfoFrame.setTitle("Input Window");
				playerInfoFrame.setResizable(false);
				playerInfoFrame.setVisible(true);
			}
		});

		// ADDING MOUSE LISTENER TO CHANGE TEXT WHEN BUTTON IS HOVERED
		newLocalGameButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				descriptorLabel.setText("Play single player game");
			}

			public void mouseExited(MouseEvent e)
			{
				descriptorLabel.setText(TITLE);
			}
		});

		newOnlineGameButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				descriptorLabel.setText("Play LAN multiplayer 2 vs 2");
			}

			public void mouseExited(MouseEvent e)
			{
				descriptorLabel.setText(TITLE);
			}
		});

		AIGameButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				descriptorLabel.setText("Play against AI");
			}

			public void mouseExited(MouseEvent e)
			{
				descriptorLabel.setText(TITLE);
			}
		});
	}
}
