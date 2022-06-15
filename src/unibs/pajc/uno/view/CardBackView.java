package unibs.pajc.uno.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class CardBackView extends JPanel
{
	private final int cardWidth = 100;
	private final int cardHeight = 150;
	private static final int margin = 5;

	private boolean shouldCardMove = false;

	public static volatile boolean isCardDrawnFromDeck;

	private final Dimension dimension = new Dimension(cardWidth, cardHeight);

	private final Border defaultBorder = BorderFactory.createEtchedBorder(JComponent.WHEN_FOCUSED, Color.white,
			Color.gray);
	private final Border focusedBorder = BorderFactory.createEtchedBorder(JComponent.WHEN_FOCUSED, Color.black,
			Color.gray);

	public CardBackView(boolean shouldCardMove)
	{
		this.shouldCardMove = shouldCardMove;

		initView();
	}

	public Dimension getDimension()
	{
		return dimension;
	}

	/**
	 * 
	 */
	private void initView()
	{
		setPreferredSize(dimension);
		setBorder(defaultBorder);

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (shouldCardMove)
				{
					CardBackView.isCardDrawnFromDeck = true;
				}
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				if (shouldCardMove)
				{
					setBorder(focusedBorder);
					setToolTipText("Draw a card!");
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				if (shouldCardMove)
				{
					setBorder(defaultBorder);
					setToolTipText(null);
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		fillBackground(g2, Color.BLACK);
		drawWhiteOvalInCenter(g2);
		drawValueInCenter(g2);
	}

	/**
	 * 
	 * @param g2
	 * @param cardColor
	 */
	private void fillBackground(Graphics2D g2, Color cardColor)
	{
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, cardWidth, cardHeight);

		g2.setColor(cardColor);
		g2.fillRect(margin, margin, cardWidth - 2 * margin, cardHeight - 2 * margin);
	}

	/**
	 * 
	 * @param g2
	 */
	private void drawWhiteOvalInCenter(Graphics2D g2)
	{
		var transformer = g2.getTransform();
		g2.setColor(Color.red);
		g2.rotate(45, (double) cardWidth * 3 / 4, (double) cardHeight * 3 / 4);
		g2.fillOval(0, cardHeight / 4, cardWidth * 3 / 5, cardHeight);

		g2.setTransform(transformer);
	}

	private void drawValueInCenter(Graphics2D g2)
	{
		// var defaultFont = new Font(Util.DEFAULT_FONT, Font.BOLD, cardWidth / 2 + 5);
		var defaultFont = new Font(Util.DEFAULT_FONT, Font.BOLD, cardWidth / 3);
		var fontMetrics = this.getFontMetrics(defaultFont);
		int stringWidth = fontMetrics.stringWidth("Uno") / 2;
		int fontHeight = defaultFont.getSize() / 2;

		g2.setColor(Color.white);

		AffineTransform affineTransform = new AffineTransform();
		affineTransform.rotate(Math.toRadians(-8), 0, 0);
		Font rotatedFont = defaultFont.deriveFont(affineTransform);

		g2.setFont(rotatedFont);
		g2.drawString("Uno", cardWidth / 2 - stringWidth, cardHeight / 2 + fontHeight);
	}
}
