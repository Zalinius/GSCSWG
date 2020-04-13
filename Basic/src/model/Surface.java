package model;

import shader.CurveShader;

public class Surface extends RenderableObject {

	public Surface(int VAO, int VERTICES, int RENDER_MODE, CurveShader shaderProgram) {
		super(VAO, VERTICES, RENDER_MODE, shaderProgram);
	}

}
