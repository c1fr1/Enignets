package engine.openal

import engine.getResourceStream
import engine.opengl.GLResource
import org.lwjgl.openal.AL10.*
import java.io.BufferedInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.UnsupportedAudioFileException

/**
 * a lot of this is copied straight from the LWJGL github page, I couldn't find the WaveData class in the library that I had locally, so I went to the github page.
 * @param file file path to a .wav file
 */
open class Sound(file: String) : GLResource(alGenBuffers()) {

	init {
		try {
			val url = BufferedInputStream(getResourceStream(file))
			val ais = AudioSystem.getAudioInputStream(url)
			val format = ais.format
			var channels = 0
			when (format.channels) {
				1 -> when (format.sampleSizeInBits) {
					8 -> channels = AL_FORMAT_MONO8
					16 -> channels = AL_FORMAT_MONO16
					else -> assert(false) { "Illegal sample size" }
				}
				2 -> when (format.sampleSizeInBits) {
					8 -> channels = AL_FORMAT_STEREO8
					16 -> channels = AL_FORMAT_STEREO16
					else -> assert(false) { "Illegal sample size" }
				}
				else -> assert(false) { "Only mono or stereo is supported" }
			}

			//read data into buffer
			val buffer: ByteBuffer
			try {
				val buf = ByteArray(ais.available())
				var read: Int
				var total = 0
				while (ais.read(buf, total, buf.size - total).also { read = it } != -1
					&& total < buf.size) {
					total += read
				}
				val dest = ByteBuffer.allocateDirect(buf.size)
				dest.order(ByteOrder.nativeOrder())
				val src = ByteBuffer.wrap(buf)
				src.order(if (format.isBigEndian) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN)
				if (format.sampleSizeInBits == 16) {
					val destShort = dest.asShortBuffer()
					val srcShort = src.asShortBuffer()
					while (srcShort.hasRemaining()) destShort.put(srcShort.get())
				} else {
					while (src.hasRemaining()) dest.put(src.get())
				}
				dest.rewind()
				buffer = dest
			} catch (ioe: IOException) {
				throw ioe
			}


			//create our result
			alBufferData(id, channels, buffer, format.sampleRate.toInt())
			buffer.clear()

			//close stream
			try {
				ais.close()
			} catch (ioe: IOException) {}
		} catch (e: UnsupportedAudioFileException) {
			e.printStackTrace()
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	override fun destroy() {
		alDeleteBuffers(id)
	}
}