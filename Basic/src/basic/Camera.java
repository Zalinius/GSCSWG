package basic;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

import java.nio.DoubleBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class Camera {
	private Vector3f position;
	private Vector3f direction;
	private Vector3f center;
	private Vector3f up;
	private Vector3f side;
	private float cameraHorizontalAngle;
	private float cameraVerticalAngle;
    private double lastMousePosX;
    private double lastMousePosY;

	private Matrix4f view;
	
	
	public Camera(long window) {
		position = new Vector3f(0,0,1f);
		direction = new Vector3f(0,0,-1f);
		center = new Vector3f().add(direction).add(position); 
		up = new Vector3f(0,1,0);
		side = new Vector3f(1,0,0);		
		DoubleBuffer mouseBuffX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer mouseBuffY = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(window, mouseBuffX, mouseBuffY);
		lastMousePosX = mouseBuffX.get(0);
		lastMousePosY = mouseBuffY.get(0);
		
		view = new Matrix4f();
		view.lookAt(position, center, up, new Matrix4f());
	}
	

	public Matrix4f view() {
		center.set(direction).add(position); 
		view = new Matrix4f().lookAt(position, center, up, new Matrix4f());
		return view;
	}


	public void keyInput(int key, int action) {
		if(key == GLFW_KEY_W && action == GLFW_RELEASE) {
			position.add(direction);
		} else if(key == GLFW_KEY_S && action == GLFW_RELEASE) {
			position.sub(direction);
		} else if(key == GLFW_KEY_A && action == GLFW_RELEASE) {
			position.sub(side);
		} else if(key == GLFW_KEY_D && action == GLFW_RELEASE) {
			position.add(side);
		}
	}
	
	public void mouseMoved(double xpos, double ypos) {
        float dt = 1/60f; //TODO get real DeltaTime
        double dx = xpos - lastMousePosX;
        double dy = ypos - lastMousePosY;
        
        System.out.println(xpos + " " +ypos);
        
        lastMousePosX = xpos;
        lastMousePosY = ypos;
        
        // Convert to spherical coordinates
        float cameraAngularSpeed = 10.0f;
        cameraHorizontalAngle -= dx * cameraAngularSpeed * dt;
        cameraVerticalAngle   -= dy * cameraAngularSpeed * dt;
        
        // Clamp vertical angle to [-85, 85] degrees
        cameraVerticalAngle = java.lang.Math.max(-85.0f, java.lang.Math.min(85.0f, cameraVerticalAngle));
        if (cameraHorizontalAngle > 360)
        {
            cameraHorizontalAngle -= 360;
        }
        else if (cameraHorizontalAngle < -360)
        {
            cameraHorizontalAngle += 360;
        }
        
        double theta = java.lang.Math.toRadians(cameraHorizontalAngle);
        double phi = java.lang.Math.toRadians(cameraVerticalAngle);
        	        
        direction.set(-(float)(Math.cos(phi)*Math.sin(theta))
        			, (float) Math.sin(phi)
        			,-(float)(Math.cos(phi)*Math.cos(theta)));
        direction.normalize();
        
        side.zero().add(direction).cross(up);
        side.normalize();
	}
}
