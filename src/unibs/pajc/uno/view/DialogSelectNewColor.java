package unibs.pajc.uno.view;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import unibs.pajc.uno.model.card.CardColor;

/**
 * Pannello per mostrare a schermo i diversi colori disponibili quando si
 * sceglie una carta speciale che permette di cambiare un colore.
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
public class DialogSelectNewColor
{
	private final ArrayList<String> colors;
	private JFrame frame;

	public DialogSelectNewColor(JFrame frame)
	{
		this.frame = frame;

		colors = new ArrayList<>();

		for (var color : CardColor.values())
		{
			colors.add(color.name());
		}
	}

	/**
	 * Mostra a schermo effettivamente i colori che è possibile scegliere.
	 * 
	 * @return il valore del colore che è stato scelto dal giocatore
	 */
	public CardColor show()
	{
		String pickedColor = (String) JOptionPane.showInputDialog(frame, "Choose a color", "Wild Card",
				JOptionPane.PLAIN_MESSAGE, null, colors.toArray(), null);

		return CardColor.valueOf(pickedColor);
	}
}
