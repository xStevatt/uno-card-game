package unibs.pajc.uno.controller.net;

public class NetUtils
{
	public static final String DEFAULT_IP_ADDRESS = "127.0.0.1";
	public static final int DEFAULT_PORT = 6161;

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
