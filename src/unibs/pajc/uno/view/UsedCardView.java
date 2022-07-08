package unibs.pajc.uno.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import unibs.pajc.uno.model.card.Card;

/**
 * Pannello che serve alla rappresentazione della carta che è stata utilizzata
 * nel gioco, ed è nel pannello centrale (e non è possibile avere alcuna
 * interazione con essa).
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
public class UsedCardView extends JPanel
{
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

	public UsedCardView(Card card)
	{
		this.card = card;
		this.value = Util.getValueToDisplay(card);
		initView();
	}

	/**
	 * Inizializza la view della carta
	 */
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
				setBorder(defaultBorder);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				super.mouseExited(e);
				setBorder(defaultBorder);
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
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
	 * Il metodo fillBackground riempie lo sfondo della carta. Il funzionamento `e
	 * molto semplice, inizialmente colora lo sfondo di bianco, e successivamente
	 * colora la carta del suo colore effettivo, lasciando ai margini −2 pixel di
	 * spazio, in modo da mostrare una sorta di bordo bianco (dalla prima volta che
	 * viene colorato il rettangolo, in quanto avevamo colorato lo sfondo di
	 * bianco).
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
	 * Il metodo disegna un ovale al centro della carta e lo ruota di 45◦. In questo
	 * modo mima il disegno della carta del gioco originale. Per fare questo abbiamo
	 * utilizzato il metodo fillOlval che colora un ovale delle dimensioni che
	 * abbiamo ripetuto opportune.
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
	 * Il metodo si occupa di prendere il simbolo della carta e di disegnarlo al
	 * centro della carta. Per fare questo utilizza la classe Graphics2D, la quale
	 * permette di usare il metodo drawString. Inoltre abbiamo utizzato setFont per
	 * impostare il font che `e stato creato in precedenza, e setColor per impostare
	 * un particolare colore del font.
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
	 * Il metodo si occupa di disegnare il valore della carta e metterlo in alto a
	 * sinistra della carta. Per fare questo, utilizza come anche negli altri
	 * metodi, la classe Graphics2D. In particolare utilizza il metodo drawString
	 * per effettivamente disegnare la stringa; setFont per impostare il font che `e
	 * stato creato in precedenza, e setColor per impostare un particolare colore
	 * del font.
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

	public boolean isShouldAnimationsMove()
	{
		return shouldAnimationsMove;
	}

	public void setShouldAnimationsMove(boolean shouldAnimationsMove)
	{
		this.shouldAnimationsMove = shouldAnimationsMove;
	}
}
