package unibs.pajc.uno.view.events;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import unibs.pajc.uno.view.CardBackView;

/**
 * 
 * Classe che gestisce quando viene selezionata una carta dal tavolo
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
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
		setDetailsWhenSelected(e);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		setDrawGuiWhenSelected(e);
	}

	/**
	 * @param e
	 */
	public void setDrawGuiWhenSelected(MouseEvent e)
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

	/**
	 * @param e
	 */
	public void setDetailsWhenSelected(MouseEvent e)
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
