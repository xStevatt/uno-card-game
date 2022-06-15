package unibs.pajc.uno.view.events;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import unibs.pajc.uno.model.card.Card;
import unibs.pajc.uno.view.CardView;

public class CardSelectedEvent implements MouseListener
{
	private Object notifyObj;
	private volatile Card cardSelected = null;

	public CardSelectedEvent(Object notifyObj)
	{
		this.notifyObj = notifyObj;
	}

	public Card getCardSelected()
	{
		return cardSelected;
	}

	public void setCardSelectedNull()
	{
		this.cardSelected = null;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if ((CardView) e.getSource() != null && ((CardView) e.getSource()).isActive())
		{
			synchronized (notifyObj)
			{
				cardSelected = ((CardView) e.getSource()).getCard();
				notifyObj.notify();
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		((CardView) e.getSource()).setToolTipText("");
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		((CardView) e.getSource()).setToolTipText("Place card!");
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
