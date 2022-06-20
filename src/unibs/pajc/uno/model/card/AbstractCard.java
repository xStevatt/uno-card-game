package unibs.pajc.uno.model.card;

/**
 * Abstract card class: number, wild and action card extend from this class
 * 
 * @author Stefano Valloncini, Yuhang Ye, Luigi Amarante
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

	public abstract String toString();
}
