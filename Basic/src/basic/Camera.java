package basic;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

import java.nio.DoubleBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
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
    
    private Vector2i movement;
    private float speed;

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
		movement = new Vector2i();
		speed = 1;
		
		view = new Matrix4f();
		view.lookAt(position, center, up, new Matrix4f());
	}
	

	public Matrix4f view() {
		center.set(direction).add(position); 
		view = new Matrix4f().lookAt(position, center, up, new Matrix4f());
		return view;
	}
	
	public void update(float dt) {
		Vector3f forwardMotion = new Vector3f().add(direction).mul(movement.y).mul(dt * speed);
		Vector3f horizontalMotion = new Vector3f().add(side).mul(movement.x).mul(dt * speed);
		
		position.add(forwardMotion).add(horizontalMotion);
	}


	public void keyInput(int key, int action) {
		if(key == GLFW_KEY_W) {
			if(action == GLFW_PRESS) {
				movement.y += 1;
			}else if (action == GLFW_RELEASE) {
				movement.y -= 1;
			}
		} else if(key == GLFW_KEY_S) {
			if(action == GLFW_PRESS) {
				movement.y -= 1;
			}else if (action == GLFW_RELEASE) {
				movement.y += 1;
			}
		} else if(key == GLFW_KEY_A) {
			if(action == GLFW_PRESS) {
				movement.x -= 1;
			}else if (action == GLFW_RELEASE) {
				movement.x += 1;
			}
		} else if(key == GLFW_KEY_D) {
			if(action == GLFW_PRESS) {
				movement.x += 1;
			}else if (action == GLFW_RELEASE) {
				movement.x -= 1;
			}
		} else if(key == GLFW_KEY_LEFT_SHIFT) {
			if(action == GLFW_PRESS) {
				speed = 5;
			}else if (action == GLFW_RELEASE) {
				speed = 1;
			}
		}
	}
	
	public void mouseMoved(double xpos, double ypos) {
        float dt = 1/60f; //TODO get real DeltaTime
        double dx = xpos - lastMousePosX;
        double dy = ypos - lastMousePosY;
                
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
