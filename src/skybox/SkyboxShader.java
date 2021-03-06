package skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import renderengine.DisplayManager;
import toolbox.Maths;

public class SkyboxShader extends shader.ShaderProgram{

	private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.txt";
	private static final float ROTATE_SPEED=1f;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColor;
	private int location_blendFactor;
	private int location_CubeMap;
	private int location_CubeMap1;
	private float rotation=0;
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadFogColor(float r,float g,float b){
		super.loadVector(location_fogColor, new Vector3f(r,g,b));
	}
	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30=0;
		matrix.m31=0;
		matrix.m32=0;
	    rotation+=ROTATE_SPEED*DisplayManager.getTimeinSeconds();
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
	    super.loadMatrix(location_viewMatrix, matrix);
   
	}
	
	public void loadBlendFactor(float blendFactor){
		super.loadFloat(location_blendFactor, blendFactor);
	}
	public void connectTexture(){
		super.loadInt(location_CubeMap, 0);
		super.loadInt(location_CubeMap1, 1);

	}
	@Override
	protected void getAllUniformLocation() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	    location_fogColor=super.getUniformLocation("fogColor");
	    location_CubeMap=super.getUniformLocation("cubeMap");
	    location_CubeMap1=super.getUniformLocation("cubeMap1");
	    location_blendFactor=super.getUniformLocation("blendFactor");
	}
   
	@Override
	protected void bindAttribs() {
		super.bindAttributes(0, "position");
	}

	

}
