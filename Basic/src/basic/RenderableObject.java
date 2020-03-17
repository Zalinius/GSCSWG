package basic;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Matrix4x3f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class RenderableObject {

	public final int VBO;
	public final int VAO;
	public final int VERTICES;

	public RenderableObject(int VBO, int VAO, int VERTICES) {
		this.VBO = VBO;
		this.VAO = VAO;
		this.VERTICES = VERTICES;
	}

	public static final RenderableObject LINE = sampleLine();
	public static final RenderableObject QUAD = setupQuad();
	public static final RenderableObject BEZIER_SPLINE = sampleSplineCurve();
	
	private static RenderableObject sampleSplineCurve() {
		Vector3f p1 = new Vector3f(0,0,0);
		Vector3f p2 = new Vector3f(0.5f,0,0);
		Vector3f p3 = new Vector3f(1.0f,-0.1f,0);
		Vector3f p4 = new Vector3f(1.5f,0.1f,0);
		return setupSplineCurve(p1,p2,p3,p4);
	}
	private static RenderableObject sampleLine() {
		Vector3f p1 = new Vector3f(0,0,0);
		Vector3f p2 = new Vector3f(0.5f,0,0);
		Vector3f p3 = new Vector3f(1.0f,-0.1f,0);
		Vector3f p4 = new Vector3f(1.5f,0.1f,0);
		List<Vector3f> points = new ArrayList<>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		return setupPoints(points);
	}
	
	private static RenderableObject setupVertices(float[] vertices) {
		int vao = glGenVertexArrays();
		glBindVertexArray(vao);
		int vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);


		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();

		int vertexCount = vertices.length / 3;

		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		return new RenderableObject(vbo, vao, vertexCount);
	}


	private static RenderableObject setupQuad() {
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
		return setupVertices(vertices);
	}
	
	private static RenderableObject setupSplineCurve(Vector3f p1, Vector3f p2, Vector3f p3, Vector3f p4){
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
		System.out.println(p4);
		
		Matrix4x3f pointMatrix = new Matrix4x3f(p1, p2, p3, p4);
		Matrix4f bezierBasicMatrix = new Matrix4f(1, 0, 0, 0, -3, 3, 0, 0, 3, -6, 3, 0, 1, 3, -3, 1);
		float resolution = 10; // This relates to the segment size as 1/resolution
		
		int vertexCount = Math.round(resolution + 1);
		float[] vertexData = new float[vertexCount*3];
		
		for(int i = 0; i <= resolution; ++i) {
			float t = i / resolution;
			Vector4f tParameter = new Vector4f(1, t, t*t, t*t*t);
			
			Vector4f mt = tParameter.mul(bezierBasicMatrix);
			pointMatrix.transform(mt);
			Vector3f newPoint = new Vector3f(mt.x, mt.y, mt.z);
			vertexData[i]   = newPoint.x;
			vertexData[i+1] = newPoint.y;
			vertexData[i+2] = newPoint.z;
		}
		return setupVertices(vertexData);
	}
	
	public static RenderableObject setupPoints(List<Vector3f> points) {
		float[] vertexData = new float[points.size() * 3];
		
		int vertexDataIndex = 0;
		
		for (Iterator<Vector3f> iterator = points.iterator(); iterator.hasNext(); vertexDataIndex += 3) {
			Vector3f point = iterator.next();
			
			vertexData[vertexDataIndex] =   point.x;
			vertexData[vertexDataIndex+1] = point.y;
			vertexData[vertexDataIndex+2] = point.z;
		}
		
		return setupVertices(vertexData);
	}

}
