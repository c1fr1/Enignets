# ABOUT
Enignets is a graphics library meant for game development built on top of LWJGL. The primary underlying 
graphics API used is OpenGL, while GLFW is used for window management, and OpenAL is used for audio.

# USAGE

Examples of how to use Enignets are availible [here](https://github.com/c1fr1/EnignetsExamples). 

### Gradle

Enignets can be added to a gradle project by using [Jitpack](https://jitpack.io/), however this will not include
platform-specific natives, so they must also be added as dependencies. 

the following is an example of  how to use it in a `build.gradle.kts`.

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

val lwjglVersion = "3.2.3"
val jomlVersion = "1.10.1"

//this can be any valid tag on this repo, or a short commit id. More info at https://jitpack.io/.
val enignetsVersion = "v1.0.1a_2"

repositories {
	//other repositories
	maven {
		url = uri("https://jitpack.io")
	}
}
dependencies {
	//other dependencies

	implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

	implementation("org.lwjgl", "lwjgl-assimp")
	implementation("org.lwjgl", "lwjgl-glfw")
	implementation("org.lwjgl", "lwjgl-openal")
	implementation("org.lwjgl", "lwjgl-opengl")
	implementation("org.lwjgl", "lwjgl-stb")
	//none of the above are strictly necessary, but will probably be useful depending on your project.
    
	implementation("org.joml", "joml", jomlVersion)
    
	runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
	runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
	runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
	runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
	runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    
	implementation("com.github.c1fr1:Enignets:${enignetsVersion}")
}
```