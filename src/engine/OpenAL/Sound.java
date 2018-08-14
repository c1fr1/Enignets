package engine.OpenAL;

import com.sun.media.sound.WaveFileReader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import static org.lwjgl.openal.AL10.*;

public class Sound {
	
	public static ArrayList<Integer> soundIDs = new ArrayList<>();
	
	private int id;
	
	/**
	 * a lot of this is copied straight from the LWJGL github page, I couldn't find the WaveData class in the library that I had locally, so I went to the github page.
	 * @param file file path to a .wav file
	 */
	public Sound(String file) {
		try {
			id = alGenBuffers();
			soundIDs.add(id);
			
			InputStream url = getClass().getClassLoader().getResourceAsStream(file);
			AudioInputStream ais = new WaveFileReader().getAudioInputStream(url);
			AudioFormat format = ais.getFormat();
			int channels = 0;
			if (format.getChannels() == 1) {
				if (format.getSampleSizeInBits() == 8) {
					channels = AL_FORMAT_MONO8;
				} else if (format.getSampleSizeInBits() == 16) {
					channels = AL_FORMAT_MONO16;
				} else {
					assert false : "Illegal sample size";
				}
			} else if (format.getChannels() == 2) {
				if (format.getSampleSizeInBits() == 8) {
					channels = AL_FORMAT_STEREO8;
				} else if (format.getSampleSizeInBits() == 16) {
					channels = AL_FORMAT_STEREO16;
				} else {
					assert false : "Illegal sample size";
				}
			} else {
				assert false : "Only mono or stereo is supported";
			}
			
			//read data into buffer
			ByteBuffer buffer = null;
			try {
				int available = ais.available();
				if(available <= 0) {
					available = ais.getFormat().getChannels() * (int) ais.getFrameLength() * ais.getFormat().getSampleSizeInBits() / 8;
				}
				byte[] buf = new byte[ais.available()];
				int read = 0, total = 0;
				while ((read = ais.read(buf, total, buf.length - total)) != -1
						&& total < buf.length) {
					total += read;
				}
				ByteBuffer dest = ByteBuffer.allocateDirect(buf.length);
				dest.order(ByteOrder.nativeOrder());
				ByteBuffer src = ByteBuffer.wrap(buf);
				src.order(format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
				if (format.getSampleSizeInBits() == 16) {
					ShortBuffer dest_short = dest.asShortBuffer();
					ShortBuffer src_short = src.asShortBuffer();
					while (src_short.hasRemaining())
						dest_short.put(src_short.get());
				} else {
					while (src.hasRemaining())
						dest.put(src.get());
				}
				dest.rewind();
				buffer = dest;
			} catch (IOException ioe) {
				throw ioe;
			}
			
			
			//create our result
			alBufferData(id, channels, buffer, (int) format.getSampleRate());
			buffer.clear();
			
			//close stream
			try {
				ais.close();
			} catch (IOException ioe) {
			}
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * gets the handle for the openAL sound buffer
	 * @return sound handle
	 */
	public int getID() {
		return id;
	}
}
