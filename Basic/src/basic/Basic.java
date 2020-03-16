package basic;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.*;
import org.joml.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL40.*;

public class Basic {


	public static final float SIXTEEN_BY_NINE = 16f/9f;
	// The window handle
	private long window;
	private Matrix4f projection;
	private Matrix4f view;
	private Matrix4f model;
	
	private Vector3f position;
	private Vector3f direction;
	private Vector3f center;

	public void run() {
		System.out.println("LWJGL:  " + Version.getVersion());
		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
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

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}



	private RenderableObject setupTriangle() {
		int vao = glGenVertexArrays();
		glBindVertexArray(vao);
		int vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		float[] vertices = {
				// Left bottom triangle
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				// Right top triangle
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 0f
		};
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();

		int vertexCount = vertices.length;

		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		return new RenderableObject(vbo, vao, vertexCount);
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		setKeys();
		System.out.println("OpenGL: " + glGetInteger(GL_MAJOR_VERSION) + "." + glGetInteger(GL_MINOR_VERSION));
		//	    System.out.println(glGetInteger(GL_MAX_TESS_GEN_LEVEL));

		int program = new ShaderFactory("res/shaders/", "basic").PROGRAM;
		RenderableObject object = setupTriangle();


		//Set up transformation matrices
		position = new Vector3f(0,0,1f);
		direction = new Vector3f(0,0,-1f);
		center = new Vector3f().add(direction).add(position); 
		
		model = new Matrix4f();
		projection = new Matrix4f().perspective((float)java.lang.Math.toRadians(45), SIXTEEN_BY_NINE , 0.1f, 100f);
		view = new Matrix4f().lookAt(position.x, position.y, position.z, center.x, center.y, center.z, 0, 1, 0, new Matrix4f());

		int mmLoc = glGetUniformLocation(program, "mm");
		int pmLoc = glGetUniformLocation(program, "pm");
		int vmLoc = glGetUniformLocation(program, "vm");

		FloatBuffer mmBuf = BufferUtils.createFloatBuffer(16);
		FloatBuffer pmBuf = BufferUtils.createFloatBuffer(16);
		FloatBuffer vmBuf = BufferUtils.createFloatBuffer(16);




		glUseProgram(program);

		// Set the clear color
		glClearColor(1.0f, 0.5f, 0.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			glUseProgram(program);

			glBindVertexArray(object.VAO);
			glEnableVertexAttribArray(0);
			
			center = new Vector3f().add(direction).add(position); 
			view = new Matrix4f().lookAt(position.x, position.y, position.z, center.x, center.y, center.z, 0, 1, 0, new Matrix4f());
			model.get(mmBuf);
			projection.get(pmBuf);
			view.get(vmBuf);
			glUniformMatrix4fv(mmLoc, false, mmBuf);
			glUniformMatrix4fv(pmLoc, false, pmBuf);
			glUniformMatrix4fv(vmLoc, false, vmBuf);


			glDrawArrays(GL_TRIANGLES, 0, object.VERTICES);

			glBindVertexArray(0);
			glDisableVertexAttribArray(0);


			int error = glGetError();
			if(error != 0) {
				System.err.println("ERROR:" + error);
			}

			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	private void setKeys() {
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			} else if(key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
				System.out.println("Rawr!");
			} else if(key == GLFW_KEY_W && action == GLFW_RELEASE) {
				position.z -= 1;
			} else if(key == GLFW_KEY_S && action == GLFW_RELEASE) {
				position.z += 1;
			} else if(key == GLFW_KEY_A && action == GLFW_RELEASE) {
				position.x -= 1;
			} else if(key == GLFW_KEY_D && action == GLFW_RELEASE) {
				position.x += 1;
			}
		});
	}

	public static void main(String[] args) {
		new Basic().run();
	}
}