package unibs.pajc.uno.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import unibs.pajc.uno.model.card.Card;

/**
 * 
 * Pannello per la visualzzazione di una carta del gioco.
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
public class CardView extends JPanel
{
	private static final long serialVersionUID = 1L;
	protected final Card card;
	protected final String value;

	private final int cardWidth = 100;
	private final int cardHeight = 150;
	private static final int margin = 5;

	protected boolean active = false;

	protected final Dimension dimension = new Dimension(cardWidth, cardHeight);

	protected final Border defaultBorder = BorderFactory.createEtchedBorder(JComponent.WHEN_FOCUSED, Color.white,
			Color.gray);
	protected final Border focusedBorder = BorderFactory.createEtchedBorder(JComponent.WHEN_FOCUSED, Color.black,
			Color.gray);

	public static Card cardSelected = null;

	public static volatile boolean isCardSelected = false;

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

	/**
	 * Inizializza la grafica della view
	 */
	private void initView()
	{
		setPreferredSize(dimension);
		setBorder(defaultBorder);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		// ANTIALIASING
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		var cardColor = Util.convertCardColor(card.getCardColor());

		fillBackground(g2, cardColor);
		drawWhiteOvalInCenter(g2);
		drawValueInCenter(g2, cardColor);
		drawValueInCorner(g2);
	}

	/**
	 * 
	 * Colora lo sfondo della carta del colore effettivo della carta
	 * 
	 * @param g2
	 * @param cardColor
	 */
	private void fillBackground(Graphics2D g2, Color cardColor)
	{
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, cardWidth, cardHeight);

		g2.setColor(cardColor);
		g2.fillRect(margin, margin, cardWidth - 2 * margin, cardHeight - 2 * margin);
	}

	/**
	 * 
	 * Colora l'ovale nel centro della carta di bianco
	 * 
	 * @param g2
	 */
	private void drawWhiteOvalInCenter(Graphics2D g2)
	{
		var transformer = g2.getTransform();
		g2.setColor(Color.white);
		g2.rotate(45, (double) cardWidth * 3 / 4, (double) cardHeight * 3 / 4);
		g2.fillOval(0, cardHeight / 4, cardWidth * 3 / 5, cardHeight);

		g2.setTransform(transformer);
	}

	/**
	 * 
	 * Disegna il valore della carta al centro della carta
	 * 
	 * @param g2
	 * @param cardColor
	 */
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

	/**
	 * 
	 * Disegna il valore della carta all'angolo della carta.
	 * 
	 * @param g2
	 */
	private void drawValueInCorner(Graphics2D g2)
	{
		var defaultFont = new Font(Util.DEFAULT_FONT, Font.ITALIC, cardWidth / 5);

		g2.setColor(Color.white);
		g2.setFont(defaultFont);
		g2.drawString(value, margin, 5 * margin);
	}

	/**
	 * Aggiunge un effetto di hover qunado la carta viene selezionata dal cursore.
	 * Quando la carta viene selezionta è posta a un'altezza maggiore rispetto alle
	 * altre carte. Viene incrementato di 20pixel il numero di pixel a cui si trova
	 * la carta
	 */
	public void showHoverEffect()
	{
		if (active)
		{
			setBorder(focusedBorder);
			Point p = getLocation();
			p.y -= 20;
			setLocation(p);
		}
	}

	/**
	 * Rimuove l'effetto di hover quando la carta non è più selezionata dal cursore.
	 */
	public void removeHoverEffect()
	{
		if (active)
		{
			setBorder(defaultBorder);
			Point p = getLocation();
			p.y += 20;
			setLocation(p);
		}
	}

	public Card getCard()
	{
		return card;
	}

	public Card getCardSelected()
	{
		return cardSelected;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean shouldAnimationsMove)
	{
		this.active = shouldAnimationsMove;
	}
}
