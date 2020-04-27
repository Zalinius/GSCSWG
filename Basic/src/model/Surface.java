package model;

import shader.CurveShader;

public class Surface extends RenderableObject {

	public Surface(int VAO, int vertexVBO, int VERTICES, int RENDER_MODE, CurveShader shaderProgram) {
		super(VAO, vertexVBO, VERTICES, RENDER_MODE, shaderProgram);
	}

}
