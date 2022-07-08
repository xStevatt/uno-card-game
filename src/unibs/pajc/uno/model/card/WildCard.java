package unibs.pajc.uno.model.card;

/**
 * 
 * Classe che definisce il tipo di carta wildcard
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
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

	@Override
	public boolean isCardSpecialWild()
	{
		return this.cardType == CardType.WILD_COLOR || this.cardType == CardType.WILD_DRAW_FOUR;
	}

	@Override
	public String toString()
	{
		return "WILD CARD";
	}
}
