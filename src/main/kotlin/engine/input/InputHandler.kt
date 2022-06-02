package engine.input

import org.lwjgl.glfw.GLFW.*

open class InputHandler(windowId: Long) {
	open val keys = KeyStates()
	open val mouseButtons = ButtonStates()

	var glCursorX = 0f; private set
	var glCursorY = 0f; private set
	var cursorX = Double.NaN; private set
	var cursorY = Double.NaN; private set

	var cursorDelX = 0f; private set
	var cursorDelY = 0f; private set

	init {
		glfwSetKeyCallback(windowId) { _ : Long, key : Int, _ : Int, action : Int, _ : Int -> keys.update(key, action) }
		glfwSetMouseButtonCallback(windowId) { _ : Long, button : Int, action : Int, _ : Int ->
			mouseButtons.update(button, action)
		}
		glfwSetCursorPosCallback(windowId) {_ : Long, xpos : Double, ypos : Double ->
			if (cursorX.isFinite() && cursorY.isFinite()) {
				cursorDelX = (xpos - cursorX).toFloat()
				cursorDelY = (ypos - cursorY).toFloat()
			}
			cursorX = xpos
			cursorY = ypos
			val widthBuf = IntArray(1)
			val heightBuf = IntArray(1)
			glfwGetWindowSize(windowId, widthBuf, heightBuf)
			val width = widthBuf[0]
			val height = heightBuf[0]
			glCursorX = (2 * cursorX - width).toFloat() / width.toFloat()
			glCursorY = (2 * cursorY - height).toFloat() / height.toFloat()
		}
	}

	open fun update() {
		keys.polledInput()
		mouseButtons.polledInput()
		cursorDelX = 0f
		cursorDelY = 0f
	}
}

enum class KeyState(val isDown : Boolean, val intermediateState : Boolean) {
	Pressed(true, true),
	Down(true, false),
	Released(false, true),
	Up(false, false);

	fun next() = when (intermediateState) {
		true -> when (isDown) {
			true -> Down
			false -> Up
		}
		false -> this
	}

	val isPressed : Boolean get() = isDown && intermediateState
	val isReleased : Boolean get() = !isDown && intermediateState
}