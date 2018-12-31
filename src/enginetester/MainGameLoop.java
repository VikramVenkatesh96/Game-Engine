package enginetester;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import gui.GuiRenderer;
import gui.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderengine.DisplayManager;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import renderengine.*;

public class MainGameLoop {

	public static void main(String[] args) {
	
    DisplayManager.CreateDisplay();
	Display.setTitle("New Game");
	Loader loader=new Loader();
	Light sun=new Light(new Vector3f(0,1000,-7000),new Vector3f(0.4f,0.4f,0.4f));
	List<Light> lights=new ArrayList<Light>();
	lights.add(sun);
	lights.add(new Light(new Vector3f(185,10,-293),new Vector3f(2,0,0),new Vector3f(1,0.01f,0.002f)));
	lights.add(new Light(new Vector3f(370,17,-300),new Vector3f(0,2,2),new Vector3f(1,0.01f,0.002f)));
	lights.add(new Light(new Vector3f(293,7,-305),new Vector3f(2,2,0),new Vector3f(1,0.01f,0.002f)));
	
	ModelData data=OBJFileLoader.loadOBJ("Player");
	RawModel model=OBJloader.loadObjModel("stall",loader);
	RawModel Playermodel=loader.loadtoVAO(data.getVertices(), data.getIndices(), data.getTextureCoords(), data.getNormals());
	ModelTexture texture=new ModelTexture(loader.loadTexture("stallTexture"));
	ModelTexture Playertexture=new ModelTexture(loader.loadTexture("Player"));
	texture.setReflectivity(0.1f);
	texture.setShineDamper(100);
	TexturedModel texmod=new TexturedModel(model,texture);
	TexturedModel Playertexmodel= new TexturedModel(Playermodel,Playertexture);
	List<Entity> entities=new ArrayList<Entity>();

	Player player=new Player(Playertexmodel,new Vector3f(0,0,0),0,0,0,1);
TerrainTexture background=new TerrainTexture(loader.loadTexture("grassy2"));
TerrainTexture r=new TerrainTexture(loader.loadTexture("Mud"));
TerrainTexture g=new TerrainTexture(loader.loadTexture("grassFlowers"));
TerrainTexture b=new TerrainTexture(loader.loadTexture("path"));
TerrainTexturePack texpack=new TerrainTexturePack(background, r, g, b);
TerrainTexture blendMap=new TerrainTexture(loader.loadTexture("blendMap")); 
	MasterRenderer render= new MasterRenderer(loader);
	Terrain terrain=new Terrain(0, -1, loader, texpack, blendMap,"hightmap");
	Camera camera= new Camera(player);
	List <GuiTexture> guis=new ArrayList<GuiTexture>();
	GuiTexture gui=new GuiTexture(loader.loadTexture("path"),new Vector2f(0.5f,0.5f),new Vector2f(0.25f,0.25f));
	//guis.add(gui);
	Random random =new Random();
	for(int i=0;i<100;++i){
		float x= random.nextFloat()*100+100;
		float z=random.nextFloat()*-700;
		float y=terrain.getHeightofTerrain(x, z);
		entities.add(new Entity(texmod,new Vector3f(x,y,z),0,0,0,1));
	}
	GuiRenderer guirenderer=new GuiRenderer(loader);
	
	while(!Display.isCloseRequested())
	{  

	    camera.move();
	   player.move(terrain);
	    render.processEntity(player);
	    render.processTerrain(terrain);
	  for(Entity entity:entities){
		  render.processEntity(entity);
	  }
	    render.render(lights, camera);
	    guirenderer.render(guis);
	    DisplayManager.UpdateDisplay();
	}
	guirenderer.cleanUp();
	render.cleanUp();
	loader.cleanUp();
	DisplayManager.DestroyDisplay();
	}

}
