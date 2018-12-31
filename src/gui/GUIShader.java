package gui;

import org.lwjgl.util.vector.Matrix4f;

import shader.ShaderProgram;

public class GUIShader extends ShaderProgram {
	   private static final String VERTEX_FILE = "src/gui/guiVertexShader.txt";
	    private static final String FRAGMENT_FILE = "src/gui/guiFragmentShader.txt";
	     
	    private int location_transformationMatrix;
	 
	    public GUIShader() {
	        super(VERTEX_FILE, FRAGMENT_FILE);
	    }
	     
	    public void loadTransformation(Matrix4f matrix){
	        super.loadMatrix(location_transformationMatrix, matrix);
	    }
	 
	    @Override
	    protected void getAllUniformLocation() {
	        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	    }
	 
	    @Override
	    protected void bindAttribs() {
	        super.bindAttributes(0, "position");
	    }
}
