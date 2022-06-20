package unibs.pajc.uno.controller.net;

import unibs.pajc.uno.model.GameModel;

public class Packet
{
	private GameModel model;
	private int clientSending;

	public Packet(GameModel model, int clientSending)
	{
		this.model = model;
		this.clientSending = clientSending;
	}

	public GameModel getModel()
	{
		return model;
	}

	public void setModel(GameModel model)
	{
		this.model = model;
	}

	public int getClientSending()
	{
		return clientSending;
	}

	public void setClientSending(int clientSending)
	{
		this.clientSending = clientSending;
	}
}
