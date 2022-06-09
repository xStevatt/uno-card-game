package unibs.pajc.uno;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import unibs.pajc.uno.view.PlayerDetailsInfoOffline;
import unibs.pajc.uno.view.PlayerDetailsInfoOnline;
import unibs.pajc.uno.view.events.HoverButtonEvent;

/**
 * Enum describing card types
 * 
 * @author Stefano Valloncini, Yuhang Ye, Luigi Amarante
 *
 */
public class MainApp extends JFrame
{
	private static final String APP_SIGNATURE = "Made by Stefano Valloncini, Yuhang Ye, Luigi Amarante";
	private static final String DEFAULT_APP_TITLE = "UnoPAJC, a remake of the uno card game, in Java";

	private JPanel bannerPanel;
	private JLabel bannerLabel;

	private JPanel buttonsPanel;
	private JPanel mainContainer;

	private JPanel newGameButtonPanel;
	private JButton newOnlineGameButton;

	private JPanel newOnlineGamePanel;
	private JButton newLocalGameButton;

	private JPanel AIGamePanel;
	private JButton newAIGameButton;

	private PlayerDetailsInfoOnline playerInfoFrameOnline;
	private PlayerDetailsInfoOffline playerInfoFrameOffline;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex)
				{
					System.err.println("Error trying to set default system Look and Feel.");
				}

				try
				{
					MainApp window = new MainApp();
					window.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public MainApp()
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

		this.setResizable(false);
		this.setVisible(true);
		this.setIconImage(bannerImage);
		this.setLocationRelativeTo(null);
	}

	protected void initializeBannerPanel()
	{
		Image bannerImage = new ImageIcon("res/img/main_banner.png").getImage().getScaledInstance(350, 250,
				java.awt.Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(bannerImage);

		bannerLabel = new JLabel(""); // Add text to main banner

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
		newOnlineGameButton = new JButton("Online Game");
		newLocalGameButton = new JButton("Offline Game");
		newAIGameButton = new JButton("Against AI");

		newGameButtonPanel = new JPanel(new GridLayout());
		newGameButtonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 40, 25));
		newGameButtonPanel.add(newOnlineGameButton);

		newOnlineGamePanel = new JPanel(new GridLayout(1, 1));
		newOnlineGamePanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 40, 25));
		newOnlineGamePanel.add(newLocalGameButton);

		AIGamePanel = new JPanel(new GridLayout());
		AIGamePanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 40, 40));
		AIGamePanel.add(newAIGameButton);

		buttonsPanel = new JPanel(new GridLayout(1, 3));
		buttonsPanel.setPreferredSize(new Dimension(600, 150));
		buttonsPanel.add(newGameButtonPanel);
		buttonsPanel.add(newOnlineGamePanel);
		buttonsPanel.add(AIGamePanel);

		JLabel descriptorLabel = new JLabel(DEFAULT_APP_TITLE, SwingConstants.CENTER);
		JLabel authorLabel = new JLabel(APP_SIGNATURE, SwingConstants.CENTER);

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

		// STARTING A NEW GAME WHEN BUTTON IS CLICKED

		newLocalGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Start a new local game!");
				setVisible(false);
				playerInfoFrameOffline = new PlayerDetailsInfoOffline();

				playerInfoFrameOffline.setTitle("Input Window");
				playerInfoFrameOffline.setResizable(false);
				playerInfoFrameOffline.setVisible(true);
			}
		});

		newOnlineGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Start a new LAN game!");
				setVisible(false);
				playerInfoFrameOnline = new PlayerDetailsInfoOnline();

				playerInfoFrameOnline.setTitle("Input Window");
				playerInfoFrameOnline.setResizable(false);
				playerInfoFrameOnline.setVisible(true);
			}
		});

		newAIGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Start an AI game!");
				setVisible(false);

				playerInfoFrameOffline.setTitle("Input Window");
				playerInfoFrameOffline.setResizable(false);
				playerInfoFrameOffline.setVisible(true);
			}
		});

		// ADDING MOUSE LISTENER TO CHANGE TEXT WHEN BUTTON IS HOVERED

		newOnlineGameButton.addMouseListener(
				new HoverButtonEvent("Start a singleplayer game.", DEFAULT_APP_TITLE, descriptorLabel));

		newLocalGameButton.addMouseListener(
				new HoverButtonEvent("Start a multiplayer game.", DEFAULT_APP_TITLE, descriptorLabel));

		newLocalGameButton
				.addMouseListener(new HoverButtonEvent("Start a game against AI", DEFAULT_APP_TITLE, descriptorLabel));

	}
}
