package unibs.pajc.uno.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.model.card.CardColor;
import unibs.pajc.uno.model.card.NumberCard;

public class CardView extends JPanel
{
	public static Card cardSelected = new NumberCard(CardColor.RED, 1);
	public static boolean isCardSelected = false;

	protected final Card card;
	protected final String value;

	private final int cardWidth = 100;
	private final int cardHeight = 150;
	private static final int margin = 5;

	protected boolean shouldAnimationsMove = true;

	protected final Dimension dimension = new Dimension(cardWidth, cardHeight);

	protected final Border defaultBorder = BorderFactory.createEtchedBorder(JComponent.WHEN_FOCUSED, Color.white,
			Color.gray);
	protected final Border focusedBorder = BorderFactory.createEtchedBorder(JComponent.WHEN_FOCUSED, Color.black,
			Color.gray);

	public CardView(Card card)
	{
		this.card = card;
		this.value = Util.getValueToDisplay(card);

		initView();
	}

	public Dimension getDimension()
	{
		return dimension;
	}

	private void initView()
	{
		setPreferredSize(dimension);
		setBorder(defaultBorder);

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				super.mouseEntered(e);
				showHoverEffect();
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				super.mouseExited(e);
				removeHoverEffect();
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				CardView.cardSelected = card;
				isCardSelected = true;
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		var cardColor = Util.convertCardColor(card.getCardColor());

		fillBackground(g2, cardColor);
		drawWhiteOvalInCenter(g2);
		drawValueInCenter(g2, cardColor);
		drawValueInCorner(g2);
	}

	private void fillBackground(Graphics2D g2, Color cardColor)
	{
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, cardWidth, cardHeight);

		g2.setColor(cardColor);
		g2.fillRect(margin, margin, cardWidth - 2 * margin, cardHeight - 2 * margin);
	}

	private void drawWhiteOvalInCenter(Graphics2D g2)
	{
		var transformer = g2.getTransform();
		g2.setColor(Color.white);
		g2.rotate(45, (double) cardWidth * 3 / 4, (double) cardHeight * 3 / 4);
		g2.fillOval(0, cardHeight / 4, cardWidth * 3 / 5, cardHeight);

		g2.setTransform(transformer);
	}

	private void drawValueInCenter(Graphics2D g2, Color cardColor)
	{
		var defaultFont = new Font(Util.DEFAULT_FONT, Font.BOLD, cardWidth / 2 + 5);
		var fontMetrics = this.getFontMetrics(defaultFont);
		int stringWidth = fontMetrics.stringWidth(value) / 2;
		int fontHeight = defaultFont.getSize() / 3;

		g2.setColor(cardColor);
		g2.setFont(defaultFont);
		g2.drawString(value, cardWidth / 2 - stringWidth, cardHeight / 2 + fontHeight);
	}

	private void drawValueInCorner(Graphics2D g2)
	{
		var defaultFont = new Font(Util.DEFAULT_FONT, Font.ITALIC, cardWidth / 5);

		g2.setColor(Color.white);
		g2.setFont(defaultFont);
		g2.drawString(value, margin, 5 * margin);
	}

	private void showHoverEffect()
	{
		if (shouldAnimationsMove)
		{
			setBorder(focusedBorder);
			Point p = getLocation();
			p.y -= 20;
			setLocation(p);
		}
	}

	private void removeHoverEffect()
	{
		if (shouldAnimationsMove)
		{
			setBorder(defaultBorder);
			Point p = getLocation();
			p.y += 20;
			setLocation(p);
		}
	}

	public Card getCardSelected()
	{
		return cardSelected;
	}

	public boolean isShouldAnimationsMove()
	{
		return shouldAnimationsMove;
	}

	public void setShouldAnimationsMove(boolean shouldAnimationsMove)
	{
		this.shouldAnimationsMove = shouldAnimationsMove;
	}
}
