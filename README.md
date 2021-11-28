# ABOUT
Enignets is a graphics library meant for game development built on top of LWJGL. The primary underlying 
graphics API used is OpenGL, while GLFW is used for window management, and OpenAL is used for audio.

# USAGE

Currently, examples of how to use enignets are located in `src/main/kotlin/example`, however this will soon be moved to
a seperate repository.

### Gradle

Enignets can be added to a gradle project by using [Jitpack](https://jitpack.io/), however this will not include
platform-specific natives, so they must also be added as dependencies. 

the following is an example of how to use it in a `build.gradle.kts`.

```kotlin
/*
 Should be one of the following depending on your operating system or the platform you are targeting
 
 x64 Windows - "windows"
 x86 Windows - "windows-x86"
 arm64 Windows - "windows-arm64"
 
 x64 Linux - "linux"
 x64 Linux - "linux-arm64"
 x64 Linux - "linux-arm32"
 */
val osString = "windows"
val lwjglNatives = "natives-${osString}"

//this can be any valid tag on this repo, or a short commit id. More info at https://jitpack.io/.
val enignetsVersion = "v1.0.1a_1"

repositories {
	//other repositories
	maven {
		url = uri("https://jitpack.io")
	}
}
dependencies {
	//other dependencies
	runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
	runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
	runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
	runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
	runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    
	implementation("com.github.c1fr1:Enignets:${enignetsVersion}")
}
```