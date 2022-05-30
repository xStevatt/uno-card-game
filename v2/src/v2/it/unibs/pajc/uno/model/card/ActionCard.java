package v2.it.unibs.pajc.uno.model.card;

/**
 * Class to describe an action card, defined by a type and a color
 * 
 * @author Stefano Valloncini, Yuhang Ye, Luigi Amarante
 *
 */
public class ActionCard extends AbstractCard
{
	public ActionCard(CardType cardType, CardColor cardColor)
	{
		super(cardType, cardColor);
	}

	@Override
	public CardType getCardType()
	{
		return cardType;
	}

	@Override
	public CardColor getCardColor()
	{
		return cardColor;
	}

}