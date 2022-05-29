package unibs.pajc.uno.view;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import unibs.pajc.uno.model.card.CardColor;

public class DialogSelectNewColor
{
	private final ArrayList<String> colors;

	public DialogSelectNewColor()
	{
		colors = new ArrayList<>();

		for (var color : CardColor.values())
		{
			colors.add(color.name());
		}
	}

	public CardColor show()
	{
		String pickedColor = (String) JOptionPane.showInputDialog(null, "Choose a color", "Wild Card",
				JOptionPane.PLAIN_MESSAGE, null, colors.toArray(), null);

		return CardColor.valueOf(pickedColor);
	}
}
