package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import entities.Camera;
import models.RawModel;
import renderengine.DisplayManager;
import renderengine.Loader;

public class SkyBoxRenderer {
	private static final float SIZE = 500f;
	private float time=0;
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	private static String[] TEXTURE_FILES_MORN={"right","left","top","bottom","back","front"};
	private static String[] TEXTURE_FILES_NIGHT={"nightRight","nightLeft","nightTop","nightBottom","nightBack","nightFront"};
	private RawModel cube;
	private int texture;
	private int nighttexture;
	private SkyboxShader shader;
	public SkyBoxRenderer(Loader loader,Matrix4f projectionMatrix){
		cube=loader.loadtoVAO(VERTICES, 3);
		texture=loader.loadCubeMap(TEXTURE_FILES_MORN);
		nighttexture=loader.loadCubeMap(TEXTURE_FILES_NIGHT);
		shader=new SkyboxShader();
		shader.start();
		shader.connectTexture();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
public void render(Camera camera,float r,float g,float b){
shader.start();
shader.loadViewMatrix(camera);
shader.loadFogColor(r, g, b);
GL30.glBindVertexArray(cube.getVaoID());
GL20.glEnableVertexAttribArray(0);
bindTextures();
GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
GL20.glDisableVertexAttribArray(0);
GL30.glBindVertexArray(0);
shader.stop();
}
private void bindTextures(){
	time+=DisplayManager.getTimeinSeconds()*1000;
	time%=24000;
	int texture1,texture2;
	float blendFactor;
	if(time>=0 && time<5000){
		texture1=nighttexture;
		texture2=nighttexture;
		blendFactor=(time-0)/(5000-0);
	}else if(time>=5000 && time<8000){
		texture1=nighttexture;
		texture2=texture;
		blendFactor=(time-5000)/(8000-5000);
	}else if(time>=8000 && time<21000){
		texture1=texture;
		texture2=texture;
		blendFactor=(time-8000)/(21000-8000);
	}else{
		texture1=texture;
		texture2=nighttexture;
		blendFactor=(time-21000)/(24000-21000);
	}
	GL13.glActiveTexture(GL13.GL_TEXTURE0);
	GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
	GL13.glActiveTexture(GL13.GL_TEXTURE1);
	GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
	shader.loadBlendFactor(blendFactor);
    }
}
