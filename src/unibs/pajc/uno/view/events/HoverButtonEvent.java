package unibs.pajc.uno.view.events;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class HoverButtonEvent extends MouseAdapter
{
	private String hoveredTitle;
	private String defaultTitle;

	private Color color;
	private Graphics2D graphics;

	private JPanel panelLogo;
	private JLabel label;

	public HoverButtonEvent(String hoveredTitle, String defaultTitle, JLabel label)
	{
		this.hoveredTitle = hoveredTitle;
		this.defaultTitle = defaultTitle;
		this.label = label;
		this.color = color;
		this.panelLogo = panelLogo;
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		label.setText(hoveredTitle);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		label.setText(defaultTitle);
	}
}
