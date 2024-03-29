package unibs.pajc.uno.controller.net;

public class NetUtils
{
	// public static final String DEFAULT_IP_ADDRESS = "127.0.0.1";
	public static final int DEFAULT_PORT = 6161;
	public static final int DEFAULT_SERVER_TIME_OUT = 100000;
	public static final int DEFAULT_CLIENT_TIME_OUT = 1000;

	public static final boolean ONLINE_GAME = false;

	/**
	 * Validates a given IP address.
	 * 
	 * @param IPAddress the IP address that needs to be validated
	 * @return boolean value, true if IP is valid
	 */
	public static boolean validateIPAddress(String IPAddress)
	{
		String[] splittedString = IPAddress.split("-");

		for (int i = 0; i < splittedString.length; i++)
		{
			try
			{
				if (Integer.parseInt(splittedString[i]) >= 255)
				{
					return false;
				}
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}
}
