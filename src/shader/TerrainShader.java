package shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class TerrainShader extends ShaderProgram{
    private static final int MAX_LIGHTS=4;  
	 private static final String VERTEX_FILE="src/shader/TerrainVertexShader.txt";
     private static final String FRAGMENT_FILE="src/shader/TerrainFragmentShader.txt";
     private int location_transformationMatrix;
	 private int location_projectionMatrix;
	 private int location_viewMatrix;
	 private int location_lightPosition[];
	 private int location_lightColor[];
	private int location_reflectivity;
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_skyColour;
	private int location_background;
	private int location_r;
	private int location_g;
	private int location_b;
	private int location_blendMap;
	public TerrainShader(){
		super(VERTEX_FILE,FRAGMENT_FILE);
	}
	
	protected void bindAttribs() {
		super.bindAttributes(0, "position");
		super.bindAttributes(1,"texcoords");
	    super.bindAttributes(2,"normals");
	}

	protected void getAllUniformLocation() {
		location_transformationMatrix=super.getUniformLocation("transformationMatrix");
		location_projectionMatrix=super.getUniformLocation("projectionMatrix");
	    location_viewMatrix=super.getUniformLocation("viewMatrix");
	    location_reflectivity=super.getUniformLocation("reflectivity");
	    location_shineDamper=super.getUniformLocation("shineDamper");
	    location_skyColour=super.getUniformLocation("skyColor");
	    location_background=super.getUniformLocation("background");
	    location_r=super.getUniformLocation("r");
	    location_b=super.getUniformLocation("b");
	    location_g=super.getUniformLocation("g");
	    location_blendMap=super.getUniformLocation("blendMap");
	    location_lightPosition=new int[MAX_LIGHTS];
	    location_lightColor=new int[MAX_LIGHTS];
	    location_attenuation=new int[MAX_LIGHTS];
	    for(int i=0;i<MAX_LIGHTS;++i){
	    	 location_lightPosition[i]=super.getUniformLocation("lightPosition["+i+"]");
	    	 location_lightColor[i]=super.getUniformLocation("lightColor["+i+"]");
	    	 location_attenuation[i]=super.getUniformLocation("attenuation["+i+"]");
	    }
	}
  
	public void connectTextureUnits(){
		super.loadInt(location_background, 0);
		super.loadInt(location_r, 1);
		super.loadInt(location_b, 2);
		super.loadInt(location_g, 3);
		super.loadInt(location_blendMap, 4);
		}
	
	public void loadShine(float damper,float reflectivity){
	   super.loadFloat(location_shineDamper, damper);
	   super.loadFloat(location_reflectivity, reflectivity);
   }
	public void loadLight(List<Light> light){
    	for(int i=0;i<MAX_LIGHTS;++i){
		if(i<light.size()){
		super.loadVector(location_lightPosition[i], light.get(i).getPosition());
    	super.loadVector(location_lightColor[i], light.get(i).getColour());
    	super.loadVector(location_attenuation[i], light.get(i).getAttenuation());
		 }
		else{
			super.loadVector(location_lightPosition[i], new Vector3f(0,0,0));
	    	super.loadVector(location_lightColor[i], new Vector3f(0,0,0));
	    	super.loadVector(location_attenuation[i], new Vector3f(1,0,0));
		 }
    	}
    }
	public void loadTransformationMatrix(Matrix4f matrix){
    	super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
    	super.loadMatrix(location_projectionMatrix, matrix);
    }
    public void loadViewMatrix(Camera camera){
    	Matrix4f viewMatrix=Maths.createViewMatrix(camera);
    	super.loadMatrix(location_viewMatrix,viewMatrix );
    }

    public void loadSkyColor(Vector3f Skycolor){
 	   super.loadVector(location_skyColour, Skycolor);
    }

}
