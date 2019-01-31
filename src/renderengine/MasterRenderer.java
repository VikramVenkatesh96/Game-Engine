package renderengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shader.StaticShader;
import shader.TerrainShader;
import skybox.SkyBoxRenderer;
import terrain.Terrain;

public class MasterRenderer {

private static final float FOV=70;
private static final float NEAR_PLANE=0.1f;
private static final float FAR_PLANE=1000;
private float RED=0.8f,GREEN=0.75f,BLUE=0.8f;
private float time=0;
private Matrix4f projectionMatrix;
private StaticShader shader=new StaticShader();
private EntityRenderer entrender;
private TerrainRenderer terrender;
private TerrainShader terrainShader=new TerrainShader();
private SkyBoxRenderer skyrender;
private Map<TexturedModel,List<Entity>> entities=new HashMap<TexturedModel,List<Entity>>();
private List<Terrain> terrains=new ArrayList<Terrain>();;

//Main render method to be called by main class
public void render(List<Light> lights,Camera camera){
	Prepare();
	shader.start();
	//loadSkyColor();
	shader.loadSkyColor(new Vector3f(RED,GREEN,BLUE));
	shader.loadLight(lights);
	shader.loadViewMatrix(camera);
	entrender.render(entities);
	shader.stop();
	terrainShader.start();
	terrainShader.loadLight(lights);
	terrainShader.loadViewMatrix(camera);
	terrainShader.loadSkyColor(new Vector3f(RED,GREEN,BLUE));
	terrender.render(terrains);
	terrainShader.stop();
	skyrender.render(camera,RED,GREEN,BLUE);
	entities.clear();
	terrains.clear();
}
public void processTerrain(Terrain terrain){
	terrains.add(terrain);
}
public void processEntity(Entity entity){
	TexturedModel entitymodel=entity.getTexmodel();
	List<Entity> batch=entities.get(entitymodel);
	if(batch!=null){
		batch.add(entity);
	}else{
		List<Entity> newBatch=new ArrayList<Entity>();
	    newBatch.add(entity);
	    entities.put(entitymodel, newBatch);
	}
}
public void cleanUp(){
	shader.cleanup();
	terrainShader.cleanup();
    }
public MasterRenderer(Loader loader){
	 GL11.glEnable(GL11.GL_CULL_FACE);
	GL11.glCullFace(GL11.GL_BACK);
	createProjectionMatrix();
    entrender=new EntityRenderer(shader,projectionMatrix);
    terrender=new TerrainRenderer(terrainShader,projectionMatrix);
    skyrender=new SkyBoxRenderer(loader,projectionMatrix);
}

public void  Prepare(){
	  GL11.glEnable(GL11.GL_DEPTH_TEST);
	  GL11.glClearColor(RED,GREEN,BLUE, 1);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
      }

/*private void loadSkyColor(){
	time+=DisplayManager.getTimeinSeconds()*1000;
	time%=24000;
	if(time>=0 && time<5000){
		RED+=0.1f;
		BLUE+=0.05f;
		GREEN+=0.08f;
	}else if(time>=5000 && time<8000){
		RED-=0.1f;
		BLUE+=0.2f;
		GREEN+=0.09f;
	}else if(time>=8000 && time<21000){
		
	}else{
		RED-=0.1f;
		if(BLUE<0)
			BLUE=0;
		else
			BLUE-=0.3f;
		if(GREEN<0)
			GREEN=0;
		else
			GREEN-=0.3f;
		
	}
}*/
private void createProjectionMatrix(){
    float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
    float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
    float x_scale = y_scale / aspectRatio;
    float frustum_length = FAR_PLANE - NEAR_PLANE;

    projectionMatrix = new Matrix4f();
    projectionMatrix.m00 = x_scale;
    projectionMatrix.m11 = y_scale;
    projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
    projectionMatrix.m23 = -1;
    projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
    projectionMatrix.m33 = 0;
}

}
