package engine.openal

import engine.entities.Orientation2D
import engine.entities.Orientation3D
import engine.opengl.EnigContext
import engine.opengl.GLResource
import engine.opengl.Resource
import engine.opengl.jomlExtensions.xz
import org.joml.*
import org.lwjgl.openal.AL10.*
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.util.ArrayList

@Suppress("unused", "MemberVisibilityCanBePrivate")
open class SoundSource : Vector3f, Resource {

	private val id = alGenSources()

	open var volume = 0f
		set(value) {
			alSourcef(id, AL_GAIN, value)
			field = value
		}

	var pitch = 0f
		set(value) {
			alSourcef(id, AL_PITCH, value)
			field = value
		}

	var looping = false
		set(value) {
			alSourcei(id, AL_LOOPING, if (value) 1 else 0)
			field = value
		}

	constructor() : super()
	constructor(d : Float) : super(d)
	constructor(x : Float, y : Float, z : Float) : super(x, y, z)
	constructor(v : Vector3fc) : super(v)
	constructor(v : Vector3ic) : super(v)
	constructor(v : Vector2fc, z : Float) : super(v, z)
	constructor(v : Vector2ic, z : Float) : super(v, z)
	constructor(xyz : FloatArray) : super(xyz)
	constructor(buffer : ByteBuffer) : super(buffer)
	constructor(index : Int, buffer : ByteBuffer) : super(index, buffer)
	constructor(buffer : FloatBuffer) : super(buffer)
	constructor(index : Int, buffer : FloatBuffer) : super(index, buffer)

	init {
		EnigContext.addResource(this)
		volume = 1f
		pitch = 1f
		updateSourcePosition()
	}

	/**
	 * updates the position of the source to the position of the object
	 */
	open fun updateSourcePosition() {
		alSource3f(id, AL_POSITION, x, y, z)
	}

	/**
	 * sets the position of the source so that it is in the right position relative to the listener
	 * @param listener the orientation of the listener
	 */
	open fun updateSourcePosition(listener : Orientation3D) {
		val tempPos = listener.getRelativePosition(this)
		alSource3f(id, AL_POSITION, tempPos.x, tempPos.y, tempPos.z)
	}

	/**
	 * sets the position of the source so that it is in the right position relative to the listener
	 * @param listener the orientation of the listener
	 */
	open fun updateSourcePosition(listener : Orientation2D) {
		val tempPos = listener.getRelativePosition(this.xz)
		alSource3f(id, AL_POSITION, tempPos.x, 0f, tempPos.y)
	}

	/**
	 * starts playing a sound
	 * @param sound sound to play
	 */
	open fun playSound(sound: Sound) {
		alSourcei(id, AL_BUFFER, sound.id)
		alSourcePlay(id)
	}

	/**
	 * plays or resumes the last
	 */
	open fun play() {
		alSourcePlay(id)
	}

	/**
	 * pauses the current sound
	 */
	open fun pause() {
		alSourcePause(id)
	}

	/**
	 * stops the current sound
	 */
	open fun stop() {
		alSourceStop(id)
	}

	/**
	 * tells the source to loop when playing a sound
	 */
	@Deprecated("set looping property")
	open fun setLoop() {
		alSourcei(id, AL_LOOPING, 1)
	}

	/**
	 * tells the source to not loop when playing a sound
	 */
	@Deprecated("set looping property")
	open fun setNoLoop() {
		alSourcei(id, AL_LOOPING, 0)
	}

	override fun destroy() {
		alDeleteSources(id)
	}

	companion object {
		var sourceIDs = ArrayList<Int>()
	}
}