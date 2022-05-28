package unibs.pajc.uno.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class StopWatch
{
	int elapsedTime = 0;
	int seconds = 0;
	int minutes = 0;
	int hours = 0;
	boolean started = false;
	String seconds_string = String.format("%02d", seconds);
	String minutes_string = String.format("%02d", minutes);
	String hours_string = String.format("%02d", hours);

	Timer timer = new Timer(1000, new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			elapsedTime = elapsedTime + 1000;
			hours = (elapsedTime / 3600000);
			minutes = (elapsedTime / 60000) % 60;
			seconds = (elapsedTime / 1000) % 60;
			seconds_string = String.format("%02d", seconds);
			minutes_string = String.format("%02d", minutes);
			hours_string = String.format("%02d", hours);
		}
	});

	public void startStopwatch()
	{
		timer.start();
	}

	public String getTime()
	{
		return hours_string + ":" + minutes_string + ":" + seconds_string;
	}
}
