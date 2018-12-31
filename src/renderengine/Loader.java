package renderengine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import models.RawModel;
import textures.TextureData;

public class Loader {
private List<Integer>vaos=new ArrayList<Integer>();
private List<Integer>vbos=new ArrayList<Integer>();
private List<Integer>textures=new ArrayList<Integer>();

public RawModel loadtoVAO(float[] positions,int dimensions){
	int vaoID=createVAO();
	this.storedatainAttributeList(0, positions,dimensions );
	unbindVAO();
	return(new RawModel(vaoID,positions.length/dimensions));
}
	
  public RawModel loadtoVAO(float[] positions,int[] indices,float[] texcoords,float[] normals){
	int vaoID=createVAO();
	vaos.add(vaoID);
	bindIndicesBuffer(indices);
	storedatainAttributeList(0,positions,3);
	storedatainAttributeList(1,texcoords,2);
	storedatainAttributeList(2,normals,3);
	unbindVAO();
	return(new RawModel(vaoID,indices.length));
	
}

	public int loadTexture(String FileName){
		Texture texture=null;
		try {
			texture=TextureLoader.getTexture("PNG", new FileInputStream("res/textures/"+FileName+".png"));
		    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		   GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		   GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.2f);
				} catch (FileNotFoundException e) {
			System.out.printf("%s",FileName);
					e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	int texID=texture.getTextureID();
	textures.add(texID);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

	return texID;
	}
	
	private void bindIndicesBuffer(int[]indices){
		int vboID=GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,vboID);
	    IntBuffer buffer=storedatainIntBuffer(indices);
	    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	     
	}
private IntBuffer storedatainIntBuffer(int[] indices) {
		IntBuffer buffer=BufferUtils.createIntBuffer(indices.length);
		buffer.put(indices);
		buffer.flip();
		return buffer;
	}
public void cleanUp(){
	for(int vao:vaos)
		GL30.glDeleteVertexArrays(vao);
	for(int vbo:vbos)
	GL15.glDeleteBuffers(vbo);
   for(int texture:textures)
	   GL11.glDeleteTextures(texture);
}
private int createVAO(){
	int vaoID=GL30.glGenVertexArrays();
	GL30.glBindVertexArray(vaoID);
	return vaoID;
}
private void storedatainAttributeList(int attributeno,float[]data,int coordinate){
	int vboID= GL15.glGenBuffers();
	vbos.add(vboID);
	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
	FloatBuffer buffer=storedatainFloatBuffer(data);
	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    GL20.glVertexAttribPointer(attributeno,coordinate,GL11.GL_FLOAT,false,0,0);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    
}
private void unbindVAO(){
	GL30.glBindVertexArray(0);
}
public int loadCubeMap(String[] textureFiles){
	int texID=GL11.glGenTextures();
	GL13.glActiveTexture(GL13.GL_TEXTURE0);
	GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
	
	for(int i=0;i<textureFiles.length;++i){
		TextureData data=decodeTextureFile("res/textures/cubemaps/"+textureFiles[i]+".png");
	    GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, GL11.GL_RGBA,data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
	}
 GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_LINEAR);
 GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR);
textures.add(texID);
GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
return texID;
}
private TextureData decodeTextureFile(String fileName){
	int width=0;
	int height=0;
	ByteBuffer buffer=null;
	try{
		FileInputStream in= new FileInputStream(fileName);
		PNGDecoder decoder=new PNGDecoder(in);
		width=decoder.getWidth();
		height=decoder.getHeight();
		buffer=ByteBuffer.allocateDirect(4*width*height);
		decoder.decode(buffer, width*4, Format.RGBA);
		buffer.flip();
		in.close();
	}catch(Exception e){
		e.printStackTrace();
		System.err.println("Tried to load texture "+ fileName +", didn't work");
		System.exit(-1);
	}
return new TextureData(width,height,buffer);
}

private FloatBuffer storedatainFloatBuffer(float[]data){
	FloatBuffer buffer=BufferUtils.createFloatBuffer(data.length);
	buffer.put(data);
	buffer.flip();
	return buffer;
}
}