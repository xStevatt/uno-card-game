package unibs.pajc.uno.view.events;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import unibs.pajc.uno.view.CardBackView;

public class CardDrawnEvent implements MouseListener
{
	private Object notifyObj;
	private volatile boolean isCardDrawn = false;

	public CardDrawnEvent(Object notifyObj)
	{
		this.notifyObj = notifyObj;
	}

	public boolean isCardDrawn()
	{
		return isCardDrawn;
	}

	public void setCardDrawn(boolean isCardDrawn)
	{
		this.isCardDrawn = isCardDrawn;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		synchronized (notifyObj)
		{
			if (((CardBackView) e.getSource()).isShouldCardWork())
			{
				isCardDrawn = true;

				notifyObj.notify();
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		if (((CardBackView) e.getSource()).isShouldCardWork())
		{
			((CardBackView) e.getSource()).setDefaultBorder();
			((CardBackView) e.getSource()).setToolTipText("");
		}
		else
		{
			((CardBackView) e.getSource()).setDefaultBorder();
			((CardBackView) e.getSource()).setToolTipText(null);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		if (((CardBackView) e.getSource()).isShouldCardWork())
		{
			((CardBackView) e.getSource()).setSelectedBorder();
			((CardBackView) e.getSource()).setToolTipText("Draw a card!");
		}
		else
		{
			((CardBackView) e.getSource()).setDefaultBorder();
			((CardBackView) e.getSource()).setToolTipText(null);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// NOT IMPLEMENTED
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// NOT IMPLEMENTED
	}
}
