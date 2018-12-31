package renderengine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
    private static final int WIDTH=1366;
    private static final int HEIGHT=768;
	private static final int FPS=120;
	private static long lastFrameTime;
	private static float delta;
    public static void CreateDisplay(){
		ContextAttribs attribs=new ContextAttribs(3,2)
				.withForwardCompatible(true)
				.withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
	        Display.create(new PixelFormat(), attribs);
		    Display.setFullscreen(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
      GL11.glViewport(0, 0, WIDTH, HEIGHT);
	lastFrameTime=currentTime();
    }
    public static void UpdateDisplay(){
    	Display.sync(FPS);
    	Display.update();
        long currentTime=currentTime();
        delta=(currentTime-lastFrameTime)/1000f;
        lastFrameTime=currentTime;
    }
    public static void DestroyDisplay(){
    	Display.destroy();
    }
 public static float getTimeinSeconds(){
	 return delta;
 }
    private static long currentTime(){
	 return Sys.getTime()*1000/Sys.getTimerResolution();
 }
}
