package engine.OpenAL;

import engine.Entities.Camera;
import engine.Entities.PositionInfo;
import org.joml.Vector4f;

import java.util.ArrayList;

import static org.lwjgl.openal.AL10.*;

public class SoundSource extends PositionInfo {
	public static ArrayList<Integer> sourceIDs = new ArrayList<>();
	
	private int id;
	
	private float volume;
	private float pitch;
	
	/**
	 * creates a new source
	 */
	public SoundSource() {
		super();
		id = alGenSources();
		setVolume(1f);
		setPitch(1f);
		updateSourcePosition();
	}
	
	/**
	 * sets the volume of the sound
	 * @param newValue new volume
	 */
	public void setVolume(float newValue) {
		volume = newValue;
		alSourcef(id, AL_GAIN, volume);
	}
	
	/**
	 * sets the pitch of the sound
	 * @param newValue new pitch
	 */
	public void setPitch(float newValue) {
		pitch = newValue;
		alSourcef(id, AL_PITCH, pitch);
	}
	
	/**
	 * updates the position of the source to the position of the object
	 */
	public void updateSourcePosition() {
		alSource3f(id, AL_POSITION, x, y, z);
	}
	
	/**
	 * sets the position of the source so that it is in the right position relative to the listener
	 * @param camera the camera of the listener
	 */
	public void updateSourcePosition(Camera camera) {
		Vector4f tempPos = new Vector4f(x, y, z, 1);
		tempPos.x -= camera.x;
		tempPos.y -= camera.y;
		tempPos.z -= camera.z;
		tempPos.mul(camera.rotationMatrix);
		alSource3f(id, AL_POSITION, tempPos.x, tempPos.y, tempPos.z);
	}
	
	/**
	 * starts playing a sound
	 * @param sound sound to play
	 */
	public void playSound(Sound sound) {
		alSourcei(id, AL_BUFFER, sound.getID());
		alSourcePlay(id);
	}
	
	/**
	 * plays or resumes the last
	 */
	public void play() {
		alSourcePlay(id);
	}
	
	/**
	 * pauses the current sound
	 */
	public void pause()  {
		alSourcePause(id);
	}
	
	/**
	 * stops the current sound
	 */
	public void stop() {
		alSourceStop(id);
	}
	
	/**
	 * tells the source to loop when playing a sound
	 */
	public void setLoop() {
		alSourcei(id, AL_LOOPING, 1);
	}
	
	/**
	 * tells the source to not loop when playing a sound
	 */
	public void setNoLoop() {
		alSourcei(id, AL_LOOPING, 0);
	}
}
