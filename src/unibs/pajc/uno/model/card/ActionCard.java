package unibs.pajc.uno.model.card;

/**
 * 
 * Classe che definisce la carta action del gioco
 * 
 * @author Stefano Valloncini
 * @author Yuhang Ye
 * @author Luigi Amarante
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

	@Override
	public boolean isCardSpecialWild()
	{
		return this.cardType == CardType.WILD_COLOR || this.cardType == CardType.WILD_DRAW_FOUR;
	}
}
