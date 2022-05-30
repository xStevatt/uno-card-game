package v2.it.unibs.pajc.uno.model.card;

/**
 * Class that describes a wild card defined by only a type (not definied by a
 * color)
 * 
 * @author Stefano Valloncini, Yuhang Ye, Luigi Amarante
 *
 */
public class WildCard extends AbstractCard
{
	public WildCard(CardType type)
	{
		super(type, null);
	}

	public WildCard(CardType cardType, CardColor cardColor)
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
