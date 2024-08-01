package renderEngine;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import engineRunner.Game;
import toolbox.file.JSONFile;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;

public class DisplayManager {

	private static int WIDTH = 1280;
	private static int HEIGHT = 720;
	private static final int FPS_CAP = 240;
	public static boolean hasResized = false;
	
	private static long lasFrameTime;
	private static float delta;
	
	 private static int frameCount = 0;
	 private static long lastFPSUpdateTime = 0;
	
	public static void createDisplay() {
		
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("MyGame");
			Display.setResizable(true);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lasFrameTime = getCurrentTime();
	}

	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		resized();
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lasFrameTime) / 1000f;
		lasFrameTime = currentFrameTime;
	}
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	public static void resized() {
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	    MasterRenderer.updateProjectionMatrix(Display.getWidth(), Display.getHeight());
	}
	
}
