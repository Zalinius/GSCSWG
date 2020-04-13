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
	
	public final int PROGRAM;
	
	public static int basicShadersProgram() {
		return new ShaderFactory("res/shaders/", "basic").PROGRAM;
	}
	public static int colorShadersProgram() {
		return new ShaderFactory("res/shaders/", "color").PROGRAM;
	}
	public static int tessShadersProgram() {
		return new ShaderFactory("res/shaders/", "simple").PROGRAM;
	}
	public static int splineCurveTessShadersProgram() {
		return new ShaderFactory("res/shaders/", "bezier").PROGRAM;
	}

	public ShaderFactory(String shaderProgramDirectory, String shaderName) {
		this(new File(shaderProgramDirectory), shaderName);
	}
	public ShaderFactory(File shaderProgramDirectory, String shaderName) {
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
			compiledShaders.add(compileShader(shaderSource, shaderType));
		}
		
		PROGRAM = compileProgram(compiledShaders);
	}
	
	private int compileProgram(List<Integer> shaders) {
		int program = glCreateProgram();
		for (Iterator<Integer> it = shaders.iterator(); it.hasNext();) {
			Integer shader = it.next();
			glAttachShader(program, shader);
		}
		
        glLinkProgram(program);
        
        int linked = glGetProgrami(program, GL_LINK_STATUS);
        String programLog = glGetProgramInfoLog(program);
        if (programLog.trim().length() > 0) {
            System.err.println(programLog);
        }
        if (linked == 0) {
            throw new AssertionError("Could not link program");
        }
        
        return program;
	}
	
	private void shaderErrors(int shader) {
        int compiled = glGetShaderi(shader, GL_COMPILE_STATUS);
        String shaderLog = glGetShaderInfoLog(shader);
        if (shaderLog.trim().length() > 0) {
        	System.err.println("Shader Type: ." + shaderTypeExtensions().get(glGetShaderi(shader, GL_SHADER_TYPE)));
            System.err.println(shaderLog);
        }
        if (compiled == 0) {
            throw new AssertionError("Could not compile shader");
        }
	}
	
	private int compileShader(String source, int type) {
		int shader = glCreateShader(type);
		
		glShaderSource(shader, source);
		glCompileShader(shader);
		shaderErrors(shader);
		
        return shader;
	}
	
	
	
	public static final String VS =  "vs";
	public static final String TCS = "tcs";
	public static final String TES = "tes";
	public static final String FS =  "fs";
	public static final String GS =  "gs";
	
	
	private FilenameFilter filter(String shaderName) {
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
