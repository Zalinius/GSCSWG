package basic;

import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL40.*;

public class OpenGLConstants {
	public static int majorVersion() {
		return glGetInteger(GL_MAJOR_VERSION);
	}
	public static int minorVersion() {
		return glGetInteger(GL_MINOR_VERSION);
	}
	
	public static int maximumVertexAttributes() {
		return glGetInteger(GL_MAX_VERTEX_ATTRIBS);
	}
	
	public static int maximumTesselationLevel() {
		return glGetInteger(GL_MAX_TESS_GEN_LEVEL);
	}
	public static int maximumPatchSize() {
		return glGetInteger(GL_MAX_PATCH_VERTICES);
	}
	
	
	public static int patchesMode = GL_PATCHES;
	public static int patchesTarget = GL_PATCH_VERTICES ; //Target to set number of vertices per patch
	
}
