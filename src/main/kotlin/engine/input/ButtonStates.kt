package engine.input

import org.lwjgl.glfw.GLFW.*

open class ButtonStates {
	private val internalStates = Array(GLFW_MOUSE_BUTTON_LAST + 1) { KeyState.Up }

	open operator fun get(i : Int) = internalStates[i]

	open fun update(button : Int, action : Int) {
		if (button !in internalStates.indices) return
		internalStates[button] = when (action) {
			GLFW_RELEASE -> KeyState.Released
			GLFW_PRESS -> KeyState.Pressed
			else -> internalStates[button]
		}
	}

	open fun polledInput() {
		for (i in internalStates.indices) {
			internalStates[i] = internalStates[i].next()
		}
	}

	open val mb1 : KeyState get() = internalStates[GLFW_MOUSE_BUTTON_1]
	open val mb2 : KeyState get() = internalStates[GLFW_MOUSE_BUTTON_2]
	open val mb3 : KeyState get() = internalStates[GLFW_MOUSE_BUTTON_3]
	open val mb4 : KeyState get() = internalStates[GLFW_MOUSE_BUTTON_4]
	open val mb5 : KeyState get() = internalStates[GLFW_MOUSE_BUTTON_5]
	open val mb6 : KeyState get() = internalStates[GLFW_MOUSE_BUTTON_6]
	open val mb7 : KeyState get() = internalStates[GLFW_MOUSE_BUTTON_7]
	open val mb8 : KeyState get() = internalStates[GLFW_MOUSE_BUTTON_8]
	open val mbLeft : KeyState get() = internalStates[GLFW_MOUSE_BUTTON_LEFT]
	open val mbRight : KeyState get() = internalStates[GLFW_MOUSE_BUTTON_RIGHT]
	open val mbMiddle : KeyState get() = internalStates[GLFW_MOUSE_BUTTON_MIDDLE]
}