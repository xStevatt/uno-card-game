package unibs.pajc.uno.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class CardBackView extends JPanel implements MouseListener
{
	private final int cardWidth = 100;
	private final int cardHeight = 150;
	private static final int margin = 5;

	public static boolean isCardDrawnFromDeck = false;

	private final Dimension dimension = new Dimension(cardWidth, cardHeight);

	private final Border defaultBorder = BorderFactory.createEtchedBorder(JComponent.WHEN_FOCUSED, Color.white,
			Color.gray);
	private final Border focusedBorder = BorderFactory.createEtchedBorder(JComponent.WHEN_FOCUSED, Color.black,
			Color.gray);

	public CardBackView()
	{
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
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		fillBackground(g2, Color.BLACK);
		drawWhiteOvalInCenter(g2);
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

	@Override
	public void mouseClicked(MouseEvent e)
	{
		isCardDrawnFromDeck = true;
		System.out.println("Card taken from deck");
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
