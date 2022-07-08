package unibs.pajc.uno.model.card;

/**
 * 
 * Classe astratta per definire il funzionamento di una carta
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
 *
 */
public abstract class AbstractCard implements Card
{
	protected CardColor cardColor;
	protected CardType cardType;

	public AbstractCard(CardType cardType, CardColor cardColor)
	{
		this.cardColor = cardColor;
		this.cardType = cardType;
	}
}