package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class OBJLoader {
	public static List<String> readEntireFileLines(String file) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return lines;
	}
	
	public static String readEntireFile(String path) {
		return readEntireFile(new File(path));
	}
	
	public static String readEntireFile(File file) {
		String text = null;
		try {
			 text = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return text;
	}
}
