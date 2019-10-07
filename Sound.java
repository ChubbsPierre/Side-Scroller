package game2D;

import java.io.*;
import javax.sound.sampled.*;

public class Sound extends Thread {

	String filename;	// The name of the file to play
	boolean finished;	// A flag showing that the thread has finished
	boolean repeat;
	boolean filteredSound;
	
	public Sound(String fname, boolean repeat, boolean filteredSound) {
		filename = fname;
		finished = false;
		this.filteredSound = filteredSound;
		this.repeat = repeat;
	}

	/**
	 * run will play the actual sound but you should not call it directly.
	 * You need to call the 'start' method of your sound object (inherited
	 * from Thread, you do not need to declare your own). 'run' will
	 * eventually be called by 'start' when it has been scheduled by
	 * the process scheduler.
	 */
	public void run() {
		try {
			do
			{
			File file = new File(filename);
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			AudioFormat	format = stream.getFormat();
			Filter filtered = new Filter(stream);
			AudioInputStream f = new AudioInputStream(filtered,format,stream.getFrameLength());
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip)AudioSystem.getLine(info);
			if(filteredSound == false)
			{
				clip.open(stream);
				clip.start();
			}
			else
			{
				clip.open(f);
				clip.start();
			}
			
			Thread.sleep(50);
			while (clip.isRunning() && finished == false) { Thread.sleep(50); }
			clip.close();
			} while(repeat == true); //keeps the music repeating if the sound in question has the repeating property
		}
		catch (Exception e) {	}
		finished = true;

	}
	
	public boolean getFinished()
	{
		return finished;
	}
	
	public boolean getRepeat()
	{
		return repeat;
	}
	
	public void setFinished(boolean toggle)
	{
		finished = toggle;
	}
	
	public void setRepeat(boolean toggle)
	{
		repeat = toggle;
	}	
}
