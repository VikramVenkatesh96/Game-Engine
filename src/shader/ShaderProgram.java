package shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderProgram {
private int programID;
private int VertexShaderID;
private int FragmentShaderID;
private static FloatBuffer matrix=BufferUtils.createFloatBuffer(16);
public  ShaderProgram(String Vertexfile,String Fragmentfile){
	VertexShaderID=loadShader(Vertexfile,GL20.GL_VERTEX_SHADER);
	FragmentShaderID=loadShader(Fragmentfile,GL20.GL_FRAGMENT_SHADER);
    programID=GL20.glCreateProgram();
    GL20.glAttachShader(programID, VertexShaderID);
    GL20.glAttachShader(programID, FragmentShaderID);
    bindAttribs();
    GL20.glLinkProgram(programID);
    GL20.glValidateProgram(programID);
    getAllUniformLocation();
}

protected void loadInt(int location,int value){
	GL20.glUniform1i(location, value);
}
protected void loadFloat(int location,float value){
	GL20.glUniform1f(location, value);
}
protected void loadVector(int location,Vector3f value){
	GL20.glUniform3f(location, value.x, value.y, value.z);
}
protected void loadBool(int location,boolean value){
	float actual=0;
	if(value)
	 actual=1;
	GL20.glUniform1f(location, actual);
}
protected void loadMatrix(int location,Matrix4f value){
	value.store(matrix);
	matrix.flip();
	GL20.glUniformMatrix4(location, false, matrix);
}

public void start(){
	GL20.glUseProgram(programID);
}
public void stop(){
	GL20.glUseProgram(0);
}
public void cleanup(){
 stop();
 GL20.glDetachShader(programID, VertexShaderID);
 GL20.glDetachShader(programID, FragmentShaderID);
 GL20.glDeleteShader(VertexShaderID);
 GL20.glDeleteShader(FragmentShaderID);
 GL20.glDeleteProgram(programID);
 getAllUniformLocation();
}
protected abstract  void getAllUniformLocation();
protected int getUniformLocation(String variable){
	return GL20.glGetUniformLocation(programID, variable);
}
protected abstract void bindAttribs();
protected void bindAttributes(int Attribute,String variableName){
	GL20.glBindAttribLocation(programID, Attribute, variableName);
	
}
private static int loadShader(String file,int type ){
	StringBuilder ShaderSource=new StringBuilder();
	try{
		BufferedReader reader=new BufferedReader(new FileReader(file));
		String line;
		while((line=reader.readLine())!=null){
			ShaderSource.append(line).append("\n");
		}
	  reader.close();
	}catch(IOException e){
		System.out.println("File not Found!!");
		System.exit(-1);
	}
int shaderID=GL20.glCreateShader(type);
GL20.glShaderSource(shaderID, ShaderSource);
GL20.glCompileShader(shaderID);
if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE){
	System.err.println("Could not compile shader");
	System.exit(-1);
           }
return shaderID;  
}
}
