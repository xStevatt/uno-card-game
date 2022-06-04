package unibs.pajc.uno.controller;

import unibs.pajc.uno.controller.net.NetClient;
import unibs.pajc.uno.controller.net.NetServer;
import unibs.pajc.uno.controller.net.NetUtils;
import unibs.pajc.uno.model.GameModel;
import unibs.pajc.uno.view.TableView;

public class OnlinePlayerController
{
	private boolean isServer = false;

	private NetClient netClient;
	private NetServer netServer;

	private TableView view;
	private GameModel model;

	public OnlinePlayerController(String IPAddress, int port, String playerName, boolean isServer)
	{
		this.isServer = isServer;

		if (isServer)
		{
			netServer = new NetServer(IPAddress, port, playerName);

			if (netServer.isConnected())
			{
				view = new TableView(playerName, netServer.getClientName(), NetUtils.ONLINE_GAME);
				view.setVisible(true);
				view.setResizable(false);
			}
		}
		else
		{
			netClient = new NetClient(IPAddress, port, playerName);

			if (netClient.isConnected())
			{
				view = new TableView(playerName, netClient.getServerName(), NetUtils.ONLINE_GAME);
				view.setVisible(true);
				view.setResizable(false);
			}
		}
	}
}
