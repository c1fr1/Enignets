package engine.opengl

interface Resource {
	fun destroy()

	fun delete() {
		EnigContext.removeResource(this)
		destroy()
	}
}

abstract class GLResource(val id : Int) : Resource {
	init {
		EnigContext.addResource(this)
	}
}