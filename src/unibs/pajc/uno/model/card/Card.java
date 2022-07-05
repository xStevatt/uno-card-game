package unibs.pajc.uno.model.card;

import java.io.Serializable;

/**
 * Card interface
 * 
 * @author Stefano Valloncini, Yuhang Ye, Luigi Amarante
 *
 */
public interface Card extends Serializable
{
	public CardType getCardType();

	public CardColor getCardColor();

	public boolean isCardSpecialWild();
}