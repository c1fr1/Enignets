package engine.opengl

import org.lwjgl.glfw.GLFW.*

open class InputHandler(windowId: Long) {
	open val keys = Array(GLFW_KEY_LAST + 1) {KeyState.Up}
	open val mouseButtons = Array(GLFW_MOUSE_BUTTON_LAST + 1) {KeyState.Up}
	var glCursorX = 0f; private set
	var glCursorY = 0f; private set
	var cursorX = Double.NaN; private set
	var cursorY = Double.NaN; private set

	var cursorDelX = 0f; private set
	var cursorDelY = 0f; private set

	init {
		glfwSetKeyCallback(windowId) { _: Long, key: Int, _: Int, action: Int, _: Int ->
			if (key >= 0) {
				keys[key] = when (action) {
					0 -> KeyState.Released
					1 -> KeyState.Pressed
					else -> keys[key]
				}
			}
		}
		glfwSetMouseButtonCallback(windowId) { _: Long, button: Int, action: Int, _: Int ->
			if (button >= 0) {
				mouseButtons[button] = when (action) {
					0 -> KeyState.Released
					1 -> KeyState.Pressed
					else -> mouseButtons[button]
				}
			}
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
		for (i in keys.indices) {
			keys[i] = keys[i].next()
		}
		for (i in mouseButtons.indices) {
			mouseButtons[i] = mouseButtons[i].next()
		}
		cursorDelX = 0f
		cursorDelY = 0f
	}
}

enum class KeyState(val isDown : Boolean, val intermediateState : Boolean) {
	Pressed(true, true),
	Down(true, false),
	Released(false, true),
	Up(false, true);

	fun next() = when (intermediateState) {
		true -> when (isDown) {
			true -> Down
			false -> Up
		}
		false -> this
	}
}