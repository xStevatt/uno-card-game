package unibs.pajc.uno.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import unibs.pajc.uno.model.card.Card;

public class UsedCardView extends CardView
{
	public UsedCardView(Card card)
	{
		super(card);
		initView();
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
				setBorder(focusedBorder);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				super.mouseExited(e);
				setBorder(focusedBorder);
			}
		});
	}
}
