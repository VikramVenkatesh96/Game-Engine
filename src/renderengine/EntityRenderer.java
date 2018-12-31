package renderengine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shader.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

public class EntityRenderer {

private StaticShader shader;

	public EntityRenderer(StaticShader shader,Matrix4f projectionMatrix){
		this.shader=shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	

	public void render(Map<TexturedModel,List<Entity>> entities){
		for(TexturedModel model:entities.keySet()){
			prepareTexturedModel(model);
			List<Entity> batch=entities.get(model);
		  for(Entity entity:batch){
			  prepareInstance(entity);
			  GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawmodel().getVertexCount(), GL11.GL_UNSIGNED_INT,0);
		  }
		 unbindTexturedModel(model);
		}
	}
	private void prepareTexturedModel(TexturedModel texmodel){
		RawModel model=texmodel.getRawmodel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture=texmodel.getModeltexture();
		shader.loadShine(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texmodel.getModeltexture().texId());
	}
	private void unbindTexturedModel(TexturedModel model){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	private void prepareInstance(Entity entity){
		Matrix4f transformation=Maths.createTransformationMatrix(entity.getPosition(), entity.getRx(),entity.getRy(), entity.getRz(), entity.getScale());
		shader.loadTransformationMatrix(transformation);
	}


}
