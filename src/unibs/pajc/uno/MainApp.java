package unibs.pajc.uno;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.card.CardType;
import unibs.pajc.uno.model.card.NumberCard;
import unibs.pajc.uno.model.card.WildCard;
import unibs.pajc.uno.view.CardBackView;
import unibs.pajc.uno.view.CardView;
import unibs.pajc.uno.view.PlayerDetailsInfoOffline;
import unibs.pajc.uno.view.PlayerDetailsInfoOnline;
import unibs.pajc.uno.view.PlayerDetailsInfoSinglePlayer;
import unibs.pajc.uno.view.events.HoverButtonEvent;

/**
 * Enum describing card types
 * 
 * @author Stefano Valloncini, Yuhang Ye, Luigi Amarante
 *
 */
public class MainApp extends JFrame
{
	private static final long serialVersionUID = 1L;
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

	private JLayeredPane panelCards;
	private Card cards[] = { new NumberCard(CardColor.RED, 6), new WildCard(CardType.SKIP, CardColor.YELLOW),
			new NumberCard(CardColor.GREEN, 9) };
	CardView cardViews[];

	private PlayerDetailsInfoOnline playerInfoFrameOnline;
	private PlayerDetailsInfoOffline playerInfoFrameOffline;
	private PlayerDetailsInfoSinglePlayer playerInfoSinglePlayer;

	private JPanel panelLogo;
	private Graphics2D g2;
	private JLabel descriptorLabel;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadInterface();
	}

	protected void loadInterface()
	{
		initializeBannerPanel();
		initializeButtonsPanel();

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(bannerPanel, BorderLayout.NORTH);

		panelCards = new JLayeredPane();
		panelCards.setBounds(100, 39, 250, 150);
		bannerPanel.add(panelCards);
		// this.add(buttonsPanel, BorderLayout.SOUTH);
		getContentPane().add(mainContainer, BorderLayout.SOUTH);

		this.pack();

		Image bannerImage = new ImageIcon("res/img/main_icon.png").getImage().getScaledInstance(500, 500,
				java.awt.Image.SCALE_SMOOTH);

		this.setResizable(false);
		this.setVisible(true);
		this.setIconImage(bannerImage);
		this.setLocationRelativeTo(null);

		cardViews = new CardView[3];

		CardBackView backView = new CardBackView(false);
		backView.setBounds(0, 0, backView.getDimension().width, backView.getDimension().height);

		for (int i = 0, j = 50; i < 3; i++, j += 50)
		{
			cardViews[i] = new CardView(cards[i]);
			cardViews[i].setBounds(j, 0, backView.getDimension().width, backView.getDimension().height);
			cardViews[i].setActive(true);
			cardViews[i].setEnabled(rootPaneCheckingEnabled);
		}

		panelCards.add(backView);
		panelCards.add(cardViews[0]);
		panelCards.add(cardViews[1]);
		panelCards.add(cardViews[2]);

		backView.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				System.out.println("HERE");
				cardAnimation();
			}
		});

		panelCards.moveToFront(backView);

		panelLogo = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g)
			{
				g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

				int CARD_WIDTH = (int) ((int) 100 * 1.2);
				int CARD_HEIGHT = (int) ((int) 150 * 1.2);

				// var defaultFont = new Font(Util.DEFAULT_FONT, Font.BOLD, cardWidth / 2 + 5);
				var defaultFont = new Font("Corbel", Font.ITALIC + Font.BOLD, 55);
				var fontMetrics = this.getFontMetrics(defaultFont);
				int stringWidth = fontMetrics.stringWidth("UNO") / 2;
				int fontHeight = defaultFont.getSize() / 2;

				g2.setColor(Color.black);

				AffineTransform affineTransform = new AffineTransform();
				affineTransform.rotate(Math.toRadians(-8), 0, 0);
				Font rotatedFont = defaultFont.deriveFont(affineTransform);

				g2.setFont(rotatedFont);
				g2.drawString("UNO", CARD_WIDTH / 2 - stringWidth, CARD_HEIGHT / 2 + fontHeight);
				g2.setColor(new Color(88, 24, 31));
				g2.drawString("UNO", CARD_WIDTH / 2 + 2 - stringWidth, CARD_HEIGHT / 2 + fontHeight + 2);
			}
		};
		panelLogo.setBounds(379, 39, 215, 130);
		panelLogo.setOpaque(false);
		bannerPanel.add(panelLogo);

		JLabel lblNewLabel = new JLabel("Card game");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 15));
		lblNewLabel.setBounds(400, 168, 131, 21);
		bannerPanel.add(lblNewLabel);
	}

	protected void initializeBannerPanel()
	{
		// Image bannerImage = new
		// ImageIcon("res/img/main_banner.png").getImage().getScaledInstance(350, 250,
		// java.awt.Image.SCALE_SMOOTH);
		// ImageIcon image = new ImageIcon(bannerImage);

		bannerLabel = new JLabel(""); // Add text to main banner
		bannerLabel.setBounds(300, 5, 0, 0);

		bannerLabel.setForeground(new Color(20, 20, 20));
		// bannerLabel.setIcon(image);
		bannerLabel.setIconTextGap(25);

		bannerPanel = new JPanel();
		bannerPanel.setBorder(new LineBorder(new Color(165, 42, 42), 4));
		bannerPanel.setPreferredSize(new Dimension(600, 250));
		bannerPanel.setLayout(null);
		bannerPanel.add(bannerLabel);
		// bannerPanel.add(bannerLabelSubtitle);
		bannerPanel.setBackground(new Color(237, 28, 36));
	}

	protected void initializeButtonsPanel()
	{
		newOnlineGameButton = new JButton("Online Game");
		newOnlineGameButton.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		newLocalGameButton = new JButton("Offline Game");
		newLocalGameButton.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		newAIGameButton = new JButton("Singleplayer");
		newAIGameButton.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		newGameButtonPanel = new JPanel(new GridLayout());
		newGameButtonPanel.setBackground(new Color(224, 255, 255));
		newGameButtonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 40, 25));
		newGameButtonPanel.add(newOnlineGameButton);

		newOnlineGamePanel = new JPanel(new GridLayout(1, 1));
		newOnlineGamePanel.setBackground(new Color(224, 255, 255));
		newOnlineGamePanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 40, 25));
		newOnlineGamePanel.add(newLocalGameButton);

		AIGamePanel = new JPanel(new GridLayout());
		AIGamePanel.setBackground(new Color(224, 255, 255));
		AIGamePanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 40, 40));
		AIGamePanel.add(newAIGameButton);

		buttonsPanel = new JPanel(new GridLayout(1, 3));
		buttonsPanel.setPreferredSize(new Dimension(600, 150));
		buttonsPanel.add(newGameButtonPanel);
		buttonsPanel.add(newOnlineGamePanel);
		buttonsPanel.add(AIGamePanel);

		descriptorLabel = new JLabel(DEFAULT_APP_TITLE, SwingConstants.CENTER);
		JLabel authorLabel = new JLabel(APP_SIGNATURE, SwingConstants.CENTER);

		JPanel descriptorPanelNorth = new JPanel();
		descriptorPanelNorth.setBackground(new Color(224, 255, 255));
		descriptorPanelNorth.setBorder(BorderFactory.createEmptyBorder(10, 25, 1, 40));
		JPanel descriptionPaneSouth = new JPanel();
		descriptionPaneSouth.setBackground(new Color(224, 255, 255));
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
				playerInfoFrameOffline.setLocationRelativeTo(bannerLabel);

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
				setVisible(false);
				System.out.println("Start a new LAN game!");
				playerInfoFrameOnline = new PlayerDetailsInfoOnline();
				playerInfoFrameOnline.setVisible(true);
				playerInfoFrameOnline.setResizable(false);
			}
		});

		newAIGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Start a singleplayer game!");
				setVisible(false);

				playerInfoSinglePlayer = new PlayerDetailsInfoSinglePlayer();
				playerInfoSinglePlayer.setLocationRelativeTo(bannerLabel);

				playerInfoSinglePlayer.setTitle("Input Window");
				playerInfoSinglePlayer.setResizable(false);
				playerInfoSinglePlayer.setVisible(true);
			}
		});

		// ADDING MOUSE LISTENER TO CHANGE TEXT WHEN BUTTON IS HOVERED
		newOnlineGameButton.addMouseListener(
				new HoverButtonEvent("Start a singleplayer game.", DEFAULT_APP_TITLE, descriptorLabel));

		newLocalGameButton.addMouseListener(
				new HoverButtonEvent("Start a multiplayer game.", DEFAULT_APP_TITLE, descriptorLabel));

		newAIGameButton
				.addMouseListener(new HoverButtonEvent("Start a game against AI", DEFAULT_APP_TITLE, descriptorLabel));
	}

	public void cardAnimation()
	{
		new Thread(() -> {
			for (CardView card : cardViews)
			{
				card.showHoverEffect();

				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				card.removeHoverEffect();
			}
		}).start();
	}
}
