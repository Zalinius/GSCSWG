package basic;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.*;
import org.joml.Matrix4f;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL40.*;

public class Basic {

	public static final float SIXTEEN_BY_NINE = 16f/9f;

	public static void main(String[] args) {
		new Basic().run();
	}


	private long window;
	private Matrix4f projection;
	private Matrix4f modelMatrix;
	private Camera camera;

	public void run() {
		init();
		loop();
		end();
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(1920, 1080, "Hello World!", glfwGetMonitors().get(0), NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");



		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
					);
		} // the stack frame is popped automatically

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);     // Enable v-sync
		glfwShowWindow(window);
		GL.createCapabilities(); //critical for LWJGL's interoperation with GLFW's
		initializeInput();
		initializeTesselation();

		System.out.println("LWJGL:  " + Version.getVersion());
		System.out.println("OpenGL: " + glGetInteger(GL_MAJOR_VERSION) + "." + glGetInteger(GL_MINOR_VERSION));
	}



	private void loop() {

		int cProgram = ShaderFactory.colorShadersProgram();
		int bProgram = ShaderFactory.splineCurveTessShadersProgram();
		int oldProgram = ShaderFactory.basicShadersProgram();
		int activeProgram = cProgram;
		RenderableObject axes = RenderableObject.AXES_COLORED;
		RenderableObject model = RenderableObject.BEZIER_SPLINE_POINTS;
		RenderableObject sample = RenderableObject.BEZIER_SPLINE;

		//Set up transformation matrices
		modelMatrix = new Matrix4f();
		camera = new Camera(window);
		projection = new Matrix4f().perspective((float)java.lang.Math.toRadians(45), SIXTEEN_BY_NINE , 0.1f, 100f);



		// Set the clear color
		glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		double lastFrameTime = glfwGetTime();

		while ( !glfwWindowShouldClose(window) ) {
			// Frame time calculation

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			activeProgram = bProgram;
			glUseProgram(activeProgram);

			glBindVertexArray(model.VAO);

			int mmLoc = glGetUniformLocation(activeProgram, "mm");
			int pmLoc = glGetUniformLocation(activeProgram, "pm");
			int vmLoc = glGetUniformLocation(activeProgram, "vm");
			int tlLoc = glGetUniformLocation(activeProgram, "tessLevel");

			FloatBuffer mmBuf = BufferUtils.createFloatBuffer(16);
			FloatBuffer pmBuf = BufferUtils.createFloatBuffer(16);
			FloatBuffer vmBuf = BufferUtils.createFloatBuffer(16);

			modelMatrix.get(mmBuf);
			projection.get(pmBuf);
			camera.view().get(vmBuf);
			glUniformMatrix4fv(mmLoc, false, mmBuf);
			glUniformMatrix4fv(pmLoc, false, pmBuf);
			glUniformMatrix4fv(vmLoc, false, vmBuf);
			glUniform1f(tlLoc, tesselation);
			checkError("model uniform calls");

			glDrawArrays(model.RENDER_MODE, 0, model.VERTICES);

			checkError("model draw call");


			activeProgram = cProgram;
			glUseProgram(activeProgram);

			glBindVertexArray(axes.VAO);

			mmLoc = glGetUniformLocation(activeProgram, "mm");
			pmLoc = glGetUniformLocation(activeProgram, "pm");
			vmLoc = glGetUniformLocation(activeProgram, "vm");

			mmBuf = BufferUtils.createFloatBuffer(16);
			pmBuf = BufferUtils.createFloatBuffer(16);
			vmBuf = BufferUtils.createFloatBuffer(16);

			modelMatrix.get(mmBuf);
			projection.get(pmBuf);
			camera.view().get(vmBuf);
			glUniformMatrix4fv(mmLoc, false, mmBuf);
			glUniformMatrix4fv(pmLoc, false, pmBuf);
			glUniformMatrix4fv(vmLoc, false, vmBuf);

			glDrawArrays(axes.RENDER_MODE, 0, axes.VERTICES);
			checkError("axes draw call");
			
			
			
			activeProgram = oldProgram;
			glUseProgram(activeProgram);

			glBindVertexArray(sample.VAO);

			mmLoc = glGetUniformLocation(activeProgram, "mm");
			pmLoc = glGetUniformLocation(activeProgram, "pm");
			vmLoc = glGetUniformLocation(activeProgram, "vm");

			mmBuf = BufferUtils.createFloatBuffer(16);
			pmBuf = BufferUtils.createFloatBuffer(16);
			vmBuf = BufferUtils.createFloatBuffer(16);

			modelMatrix.get(mmBuf);
			projection.get(pmBuf);
			camera.view().get(vmBuf);
			glUniformMatrix4fv(mmLoc, false, mmBuf);
			glUniformMatrix4fv(pmLoc, false, pmBuf);
			glUniformMatrix4fv(vmLoc, false, vmBuf);

			glDrawArrays(sample.RENDER_MODE, 0, sample.VERTICES);
			checkError("sample draw call");

			glBindVertexArray(0);

			glfwSwapBuffers(window); // swap the color buffers
			glfwPollEvents();

			double dt = glfwGetTime() - lastFrameTime;
			lastFrameTime += dt;
			update((float)dt);
		}
	}

	private void update(float delta) {
		camera.update(delta);
	}

	private String description(int error) {
		switch (error) {
		case GL_INVALID_ENUM:	
			return "Invalid Enum";
		case GL_INVALID_OPERATION:
			return "Invalid Operation";
		default:
			return "No Description";
		}
	}

	private void end() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void checkError(String location) {
		int error = glGetError();
		if(error != 0) {
			String message;
			if(location.isEmpty()) {
				message = "ERROR:" + error + " - " + description(error);
			}
			else {
				message = "ERROR after " + location + ":" + error + " - " + description(error);
			}
			System.err.println(message);	
		}
	}

	private void initializeInput() {
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			} else if(key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
				System.out.println("Rawr!");
			} else if (key == GLFW_KEY_E && action == GLFW_RELEASE){
				tesselation *= 2;
				tesselation = Math.min(OpenGLConstants.maximumTesselationLevel(), tesselation);
			} else if (key == GLFW_KEY_Q && action == GLFW_RELEASE){
				tesselation /= 2;
				tesselation = Math.max(1, tesselation);
			} else {
				camera.keyInput(key, action);
			}
		});

		glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
			camera.mouseMoved(xpos, ypos);
		});
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}

	private int tesselation;
	private void initializeTesselation() {
		//4 for bezier splines
		glPatchParameteri(OpenGLConstants.patchesTarget, 4);
		tesselation = 16;
	}

}