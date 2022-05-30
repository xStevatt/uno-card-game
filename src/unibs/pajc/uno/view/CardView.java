package unibs.pajc.uno.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import unibs.pajc.uno.model.card.Card;

public class CardView extends JPanel implements MouseListener
{
	/**
	 * 
	 */

	private final Card card;
	private final String value;

	private final int cardWidth = 100;
	private final int cardHeight = 150;
	private static final int margin = 5;

	public static Card cardSelected = null;

	private boolean shouldAnimationsMove = true;

	private final Dimension dimension = new Dimension(cardWidth, cardHeight);

	private final Border defaultBorder = BorderFactory.createEtchedBorder(JComponent.WHEN_FOCUSED, Color.white,
			Color.gray);
	private final Border focusedBorder = BorderFactory.createEtchedBorder(JComponent.WHEN_FOCUSED, Color.black,
			Color.gray);

	private final Consumer<Card> handleCardClick;

	public CardView(Card card)
	{
		this(card, null);
	}

	public CardView(Card card, Consumer<Card> onCardClick)
	{
		this.card = card;
		this.handleCardClick = onCardClick;
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

				handleCardClick.accept(card);
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
		setBorder(focusedBorder);

		if (shouldAnimationsMove)
		{
			Point p = getLocation();
			p.y -= 20;
			setLocation(p);
		}
	}

	private void removeHoverEffect()
	{
		setBorder(defaultBorder);

		if (shouldAnimationsMove)
		{
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

	@Override
	public void mouseClicked(MouseEvent e)
	{
		CardView.cardSelected = this.card;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

}
