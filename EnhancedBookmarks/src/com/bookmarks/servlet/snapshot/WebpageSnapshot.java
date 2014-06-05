package com.bookmarks.servlet.snapshot;

import java.io.IOException;
import java.lang.ProcessBuilder;

public class WebpageSnapshot {
	public static String takeScreenshot(String url, String file) {
	
		String phantomjsHome = "C:\\Compiled Java Resources\\phantomjs-1.9.7-windows\\";
		String phantomjsRasterizeScript = phantomjsHome + "bookmark-snap.js";
		//String phantomjsRasterizeScript = phantomjsHome + "examples\\rasterize.js";
		//String url = "http://google.com";
		//String file = "C:\\Compiled Java Resources\\phantomjs-screenshots\\google.png";

		ProcessBuilder pb = new ProcessBuilder(phantomjsHome + "phantomjs", phantomjsRasterizeScript, url, file);
		ProcessBuilder.Redirect error = pb.redirectError();
		ProcessBuilder.Redirect out = pb.redirectOutput();
		ProcessBuilder.Redirect in = pb.redirectInput();
		Process process;
		
		try {
			System.out.println("Attempting to take Screenshot of site " + url);
			process = pb.start();			
			process.waitFor();
			//UserUtils.
			System.out.println("Screenshot of site " + url + " taken: " + file);
			if(resizeImage(file)){
				System.out.println("Successfully resized the file " + file);
				return file;
			} else {
				System.out.println("Unable to resize the file " + file);
				return "";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private static boolean resizeImage(String file) {
		ProcessBuilder pb = new ProcessBuilder("C:\\Compiled Java Resources\\ImageMagick-6.8.9\\ImageMagick-6.8.9-1-Q16-x64-dll.exe", "convert", file, "-resize 245x150", file);
		ProcessBuilder.Redirect error = pb.redirectError();
		ProcessBuilder.Redirect out = pb.redirectOutput();
		ProcessBuilder.Redirect in = pb.redirectInput();
		Process process;
		
		try {
			process = pb.start();			
			process.waitFor();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
