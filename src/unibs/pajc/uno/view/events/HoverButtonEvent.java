package unibs.pajc.uno.view.events;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

public class HoverButtonEvent extends MouseAdapter
{
	private String hoveredTitle;
	private String defaultTitle;

	private JLabel label;

	public HoverButtonEvent(String hoveredTitle, String defaultTitle, JLabel label)
	{
		this.hoveredTitle = hoveredTitle;
		this.defaultTitle = defaultTitle;
		this.label = label;
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
