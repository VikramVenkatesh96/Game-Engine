package renderengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class OBJloader {
 public static RawModel loadObjModel(String filename,Loader loader){
	 FileReader fr=null;
	 try {
		fr=new FileReader(new File("res/models/"+filename+".obj"));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
   BufferedReader reader=new BufferedReader(fr);
   String line;
   List<Vector3f> vertices= new ArrayList<Vector3f>();
   List<Vector3f> normals= new ArrayList<Vector3f>();
   List<Vector2f> textures=new ArrayList<Vector2f>();
   List<Integer> indices=new ArrayList<Integer>();
   float[] verticearray=null;
   float[] normalarray=null;
   int[] indicearray=null;
   float[] texarray=null;
   try{
	while(true){
		line=reader.readLine();
		String[] currentline=line.split(" ");
	    if(line.startsWith("v ")){
	    	Vector3f vertex=new Vector3f(Float.parseFloat(currentline[1]),Float.parseFloat(currentline[2]),Float.parseFloat(currentline[3]));
	        vertices.add(vertex);
	    }else if(line.startsWith("vt ")){
	    	Vector2f texcoord=new Vector2f(Float.parseFloat(currentline[1]),Float.parseFloat(currentline[2]));
	        textures.add(texcoord);
	    }else if(line.startsWith("vn ")){
	    	Vector3f normal=new Vector3f(Float.parseFloat(currentline[1]),Float.parseFloat(currentline[2]),Float.parseFloat(currentline[3]));
	        normals.add(normal);
	    }else if(line.startsWith("f ")){
	    	texarray=new float[vertices.size()*2];
	    	normalarray= new float[vertices.size()*3];
	        break;
	    }
	}
   while(line!=null){
	   if(!line.startsWith("f ")){
		   line=reader.readLine();
		   continue;
	   }
      String[] currentline=line.split(" ");
      String[] vertex1=currentline[1].split("/");
      String[] vertex2=currentline[2].split("/");
      String[] vertex3=currentline[3].split("/");
      processVertex(vertex1,indices,textures,normals,texarray,normalarray);
      processVertex(vertex2,indices,textures,normals,texarray,normalarray);
      processVertex(vertex3,indices,textures,normals,texarray,normalarray);
      line=reader.readLine();
   }
   reader.close();
   }catch(Exception e){
	   e.printStackTrace();
   }
 verticearray=new float[vertices.size()*3];
 indicearray=new int[indices.size()];
 int vertexpointer=0;
 for(Vector3f vertex:vertices){
	 verticearray[vertexpointer++]=vertex.x;
	 verticearray[vertexpointer++]=vertex.y;
	 verticearray[vertexpointer++]=vertex.z;
 }
 for(int i=0;i<indices.size();++i){
	 indicearray[i]=indices.get(i);
 }
 return(loader.loadtoVAO(verticearray, indicearray, texarray,normalarray));
 }
private static void processVertex(String[] vertexdata,List<Integer> indices,List<Vector2f>textures,List<Vector3f> normals,float[] texarray,float[]normalarray){
	int currentvertexpointer=Integer.parseInt(vertexdata[0])-1;
	indices.add(currentvertexpointer);
	Vector2f currentex=textures.get(Integer.parseInt(vertexdata[1])-1);
   texarray[currentvertexpointer*2]=currentex.x;
   texarray[currentvertexpointer*2+1]=1-currentex.y;
   Vector3f currentnormal=normals.get(Integer.parseInt(vertexdata[2])-1);
   normalarray[currentvertexpointer*2]=currentnormal.x;
   normalarray[currentvertexpointer*2+1]=currentnormal.y;
   normalarray[currentvertexpointer*2+2]=currentnormal.z;

}
}
