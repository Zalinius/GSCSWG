package model;

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
import java.util.ListIterator;

import org.joml.Matrix4f;
import org.joml.Matrix4x3f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;

public class RenderableObject {

	public final int VAO;
	public final int VERTICES;
	public final int RENDER_MODE;

	public RenderableObject(int VAO, int VERTICES, int RENDER_MODE) {
		this.VAO = VAO;
		this.VERTICES = VERTICES;
		this.RENDER_MODE = RENDER_MODE;
	}

	public static final RenderableObject LINE = sampleLine();
	public static final RenderableObject QUAD = setupQuad();
	public static final RenderableObject MESH_TRIANGLE = setupMeshTriangle();
	public static final RenderableObject COLOR_QUAD = setupColorQuad();
	public static final RenderableObject BEZIER_SPLINE = sampleSplineCurve();
	public static final RenderableObject BEZIER_SPLINE_POINTS = samplec1SmoothSplineCurve();
	public static final RenderableObject AXES_COLORED = coloredAxes();
	
	
	private static RenderableObject setupVertices(float[] vertices, int renderMode) {
		int vao = glGenVertexArrays();

		glBindVertexArray(vao);
				
		int vbo = glGenBuffers();
		setupVBO(vbo, vertices, 0, 3);

		int vertexCount = vertices.length / 3;
		glBindVertexArray(0);

		return new RenderableObject(vao, vertexCount, renderMode);
	}
	
	private static RenderableObject setupVerticesWithColors(float[] vertices, float[] colors, int renderMode) {
		if(vertices.length != colors.length) {
			throw new RuntimeException("missing data points");
		}

		int vao = glGenVertexArrays();
		glBindVertexArray(vao);

		int[] vbos = new int[2];
		glGenBuffers(vbos);;
		setupVBO(vbos[0], vertices, 0, 3);
		
		setupVBO(vbos[1], colors, 1, 3);
		
		glBindVertexArray(0);
		int vertexCount = vertices.length / 3;

		return new RenderableObject(vao, vertexCount, renderMode);
	}
	
	private static void setupVBO(int vbo, float[] data, int attributeIndex, int dimensionality) {
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		//System.out.println("Buffering " + buffer.capacity() + " floats. (" + buffer.capacity()/3 +") vertices.");
		glVertexAttribPointer(attributeIndex, dimensionality, GL_FLOAT, false, 0, 0);	
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		GL20.glEnableVertexAttribArray(attributeIndex);
	}
	
	private static RenderableObject coloredAxes() {
		float[] vertices = {
				//X - axis
				0.0f, 0.0f, 0.0f,
				10.0f, 0.0f, 0.0f,
				//Y
				0.0f, 0.0f, 0.0f,
				0.0f, 10.0f, 0.0f,
				//Z
				0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 10.0f
		};
		
		float[] colors = {
				//X - axis - RED
				1.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f,
				//Y - axis - GREEN
				0.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f,
				//Z - axis - BLUE
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f
		};
		
		return setupVerticesWithColors(vertices, colors, GL11.GL_LINES);
	}
	
	private static RenderableObject splineCurvePoints() {
		Vector3f p1 = new Vector3f(0.1f,0.1f,0.1f);
		Vector3f p2 = new Vector3f(1.1f,0.1f,0.1f);
		Vector3f p3 = new Vector3f(1.1f,1.1f,1.1f);
		Vector3f p5 = new Vector3f(0.1f,2.1f,1.1f);
		Vector3f p6 = new Vector3f(1.1f,2.1f,1.1f);
		Vector3f p7 = new Vector3f(1.1f,2.1f,3.1f);
		
		Vector3f p4 = new Vector3f().add(p3).add(p5).mul(0.5f);

		List<Vector3f> points = new ArrayList<>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		points.add(p4);
		points.add(p5);
		points.add(p6);
		points.add(p7);
		return setupPoints(points, GL40.GL_PATCHES);
	}
	
	public static RenderableObject samplec1SmoothSplineCurve(){
		List<Vector3f> points = new ArrayList<>();
		points.add(new Vector3f(0,0,0));
		points.add(new Vector3f(0,1,0));
		points.add(new Vector3f(0,1,1));
		
		points.add(new Vector3f(1,1,2));
		points.add(new Vector3f(1,2,3));
		
		points.add(new Vector3f(4,2,1));
		points.add(new Vector3f(3,2,3));
		
		points.add(new Vector3f(4,2,2));
		points.add(new Vector3f(2,2,0));
		
		points.add(new Vector3f(1,1,1));
		points.add(new Vector3f(0,-1,0));
		points.add(new Vector3f(0,0,0));

		return c1SmoothSplineCurve(points);
	}
	
	public static RenderableObject c1SmoothSplineCurve(List<Vector3f> points) {
		int n = points.size();
		if(n < 4 || n%2 != 0 ) {
			throw new RuntimeException("Wrong number of points to make curve. Must be even and at least 4");
		}
		
		
		if(n == 4) { //No generated points
			return setupPoints(points, GL40.GL_PATCHES);
		}
		
		List<Vector3f> smoothedPoints = new ArrayList<>(points);
		
		int newIndex = 0;
		System.out.println("Creating smooth curve");
		for(ListIterator<Vector3f> it = smoothedPoints.listIterator(); it.hasNext();) {
			Vector3f point = it.next();
			newIndex++;
			if(newIndex % 4 == 3 && newIndex != 0 && newIndex != (2*points.size() -5)) {
				Vector3f collinearPoint = new Vector3f().add(point).add(smoothedPoints.get(newIndex)).mul(0.5f);
				it.add(collinearPoint);
				it.add(collinearPoint);
				System.out.println(newIndex + ": " + collinearPoint);
				newIndex++;
				System.out.println(newIndex + ": " + collinearPoint);
				newIndex++;
			}
		}
		
		return setupPoints(smoothedPoints, GL40.GL_PATCHES);
	}

	
	private static RenderableObject sampleSplineCurve() {
		Vector3f p1 = new Vector3f(0.0f,0.0f,0.0f);
		Vector3f p2 = new Vector3f(1.0f,0.0f,0.0f);
		Vector3f p3 = new Vector3f(1.0f,1.0f,1.0f);
		Vector3f p4 = new Vector3f(0.0f,1.0f,1.0f);
		return setupSplineCurve(p1,p2,p3,p4);
	}
	
	private static RenderableObject sampleLine() {
		Vector3f p1 = new Vector3f(0,0,0);
		Vector3f p2 = new Vector3f(1,0.5f,0);
		Vector3f p3 = new Vector3f(2,2,0);
		Vector3f p4 = new Vector3f(3,4.5f,0);
		Vector3f p5 = new Vector3f(4,8,0);
		Vector3f p6 = new Vector3f(5,4.5f,0);
		Vector3f p7 = new Vector3f(6,2,0);
		Vector3f p8 = new Vector3f(7,0.5f,0);
		Vector3f p9 = new Vector3f(8,0,0);
		List<Vector3f> points = new ArrayList<>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		points.add(p5);
		points.add(p6);
		points.add(p7);
		points.add(p8);
		points.add(p9);
		return setupPoints(points, GL11.GL_LINE_STRIP);
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
		return setupVertices(vertices, GL11.GL_TRIANGLES);
	}
	
	private static RenderableObject setupMeshTriangle() {
		float[] vertices = {
				// Left bottom triangle
				0.0f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f
		};
		return setupVertices(vertices, GL40.GL_PATCHES);
	}
	
	private static RenderableObject setupColorQuad() {
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
		//purple, orange, light blue, golden yellow
		float[] colors = {
				// Left bottom triangle
				1.0f, 0.5f, 0.0f, //TL - orange
				0.5f, 0.0f, 1.0f, //BL - purple
				0.8f, 1.0f, 1.0f,  //BR - Light Blue
				// Right top triangle
				0.8f, 1.0f, 1.0f,  //BR - Light Blue
				1.0f, 0.8f, 0.0f,   //TR - Gold
				1.0f, 0.5f, 0.0f   //TL - Orange
		};
		return setupVerticesWithColors(vertices, colors, GL11.GL_TRIANGLES);
	}
	
	private static RenderableObject setupSplineCurve(Vector3f p1, Vector3f p2, Vector3f p3, Vector3f p4){		
		Matrix4x3f pointMatrix = new Matrix4x3f(p1, p2, p3, p4);
		Matrix4f bezierBasicMatrix = new Matrix4f(1, 0, 0, 0, -3, 3, 0, 0, 3, -6, 3, 0, -1, 3, -3, 1);
		float resolution = 20; // This relates to the segment size as 1/resolution
		
		int vertexCount = Math.round(resolution + 1);
		List<Vector3f> points = new ArrayList<>();
		
		for(int i = 0; i != vertexCount; ++i) {
			float t = i / resolution;
			Vector4f tParameter = new Vector4f(1, t, t*t, t*t*t);
			
			Vector4f mt = tParameter.mul(bezierBasicMatrix);
			pointMatrix.transform(mt);
			Vector3f newPoint = new Vector3f(mt.x, mt.y, mt.z);
			points.add(newPoint);
			//System.out.println(i + ": " + vertexData[i] + ", " + vertexData[i+1] + ", " + vertexData[i+2]);
		}

		return setupPoints(points, GL11.GL_LINE_STRIP);
	}
	
	public static RenderableObject setupPoints(List<Vector3f> points, int renderMode) {
		float[] vertexData = new float[points.size() * 3];
		
		int vertexDataIndex = 0;
		
		for (Iterator<Vector3f> iterator = points.iterator(); iterator.hasNext(); vertexDataIndex += 3) {
			Vector3f point = iterator.next();
			
			vertexData[vertexDataIndex] =   point.x;
			vertexData[vertexDataIndex+1] = point.y;
			vertexData[vertexDataIndex+2] = point.z;
		}
		
		return setupVertices(vertexData, renderMode);
	}

}