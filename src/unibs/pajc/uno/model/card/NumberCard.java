package unibs.pajc.uno.model.card;

/**
 * Class describing a number card, definied by a color, a type and a value
 * 
 * @author Stefano Valloncini, Yuhang Ye, Luigi Amarante
 *
 */
public class NumberCard extends AbstractCard
{
	private int value;

	public NumberCard(CardColor cardColor, int value)
	{
		super(CardType.NUMBER, cardColor);
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	@Override
	public CardType getCardType()
	{
		return cardType;
	}

	@Override
	public CardColor getCardColor()
	{
		// TODO Auto-generated method stub
		return cardColor;
	}

	@Override
	public boolean isCardSpecialWild()
	{
		return this.cardType == CardType.WILD_COLOR || this.cardType == CardType.WILD_DRAW_FOUR;
	}
}
