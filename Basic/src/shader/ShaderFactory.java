package shader;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_SHADER_TYPE;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.OBJLoader;

public class ShaderFactory {
	public static final SurfaceShader BEZIER_SURFACE = new SurfaceShader(bezierPatchShadersProgram());

	public static final Shader BASIC = new Shader(basicShadersProgram());
	public static final Shader COLOR = new Shader(colorShadersProgram());
	
	public static final CurveShader BEZIER_CURVE = new CurveShader(bezierSplineShadersProgram());
	
	public static final SurfaceShader TRIANGLE_TESS = new SurfaceShader(curveTessShadersProgram());
	
	private static int basicShadersProgram() {
		return makeShader(new File("res/shaders/"), "basic");
	}
	private static int colorShadersProgram() {
		return makeShader(new File("res/shaders/"), "color");
	}
	private static int curveTessShadersProgram() {
		return makeShader(new File("res/shaders/"), "simple");
	}
	private static int bezierSplineShadersProgram() {
		return makeShader(new File("res/shaders/"), "bezier");
	}
	private static int bezierPatchShadersProgram() {
		return makeShader(new File("res/shaders/surface/"), "bicubicBezier");
	}


	private static int makeShader(File shaderProgramDirectory, String shaderName) {
		if(!shaderProgramDirectory.isDirectory()) {
			throw new RuntimeException("Not a directory");
		}
		if(!shaderProgramDirectory.exists()) {
			throw new RuntimeException("Directory does not exist");
		}
		
		File[] shaderFiles = shaderProgramDirectory.listFiles(filter(shaderName));


		
		List<Integer> compiledShaders = new ArrayList<>();
		for (int i = 0; i < shaderFiles.length; i++) {
			File file = shaderFiles[i];
			String shaderSource = OBJLoader.readEntireFile(file);
			int shaderType = shaderGLTypes().get(file.getName().substring(file.getName().lastIndexOf('.')+1));
			compiledShaders.add(compileShader(shaderSource, shaderName, shaderType));
		}
		
		return compileProgram(compiledShaders, shaderName);
	}
	
	private static int compileProgram(List<Integer> shaders, String shaderName) {
		int program = glCreateProgram();
		for (Iterator<Integer> it = shaders.iterator(); it.hasNext();) {
			Integer shader = it.next();
			glAttachShader(program, shader);
		}
		
        glLinkProgram(program);
        
        int linked = glGetProgrami(program, GL_LINK_STATUS);
        String programLog = glGetProgramInfoLog(program);
        if (programLog.trim().length() > 0) {
            System.err.println("Link error: " + programLog);
        }
        if (linked == 0) {
            throw new AssertionError("Could not link program " + '"' + shaderName + '"');
        }
        
        return program;
	}
	
	private static void shaderErrors(int shader, String name) {
        int compiled = glGetShaderi(shader, GL_COMPILE_STATUS);
        String shaderLog = glGetShaderInfoLog(shader);
        if (shaderLog.trim().length() > 0) {
        	System.err.println("Shader: " + name + "." + shaderTypeExtensions().get(glGetShaderi(shader, GL_SHADER_TYPE)));
            System.err.println(shaderLog);
        }
        if (compiled == 0) {
            throw new AssertionError("Could not compile shader");
        }
	}
	
	private static int compileShader(String source, String name, int type) {
		int shader = glCreateShader(type);
		
		glShaderSource(shader, source);
		glCompileShader(shader);
		shaderErrors(shader, name);
		
        return shader;
	}
	
	
	
	public static final String VS =  "vs";
	public static final String TCS = "tcs";
	public static final String TES = "tes";
	public static final String FS =  "fs";
	public static final String GS =  "gs";
	
	
	private static FilenameFilter filter(String shaderName) {
		return new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				for (Iterator<String> iterator = shaderFileExtensions().iterator(); iterator.hasNext();) {
					String type = iterator.next();
					
					if(name.equals(shaderName + "." + type)) {
						return true;
					}
				}
				
				return false;
			}
		};
	}
		
	private static Set<String> shaderFileExtensions(){
		return shaderGLTypes().keySet();
	}
	
	private static Map<Integer, String> shaderTypeExtensions(){
		Map<Integer, String> map = new HashMap<>();
		map.put(GL_VERTEX_SHADER, VS);
		map.put(GL_TESS_CONTROL_SHADER, TCS);
		map.put(GL_TESS_EVALUATION_SHADER, TES);
		map.put(GL_GEOMETRY_SHADER, GS);
		map.put(GL_FRAGMENT_SHADER, FS);
		
		return map;
	}
	private static Map<String, Integer> shaderGLTypes(){
		Map<String, Integer> map = new HashMap<>();
		map.put(VS, GL_VERTEX_SHADER);
		map.put(TCS, GL_TESS_CONTROL_SHADER);
		map.put(TES, GL_TESS_EVALUATION_SHADER);
		map.put(GS, GL_GEOMETRY_SHADER);
		map.put(FS, GL_FRAGMENT_SHADER);
		return map;
	}

}
