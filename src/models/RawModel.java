package models;

public class RawModel {
private int vaoID;
public int getVaoID() {
	return vaoID;
}
public int getVertexCount() {
	return vertexCount;
}
private int vertexCount;
public RawModel(int vaoID,int vertexCount){
	this.vaoID=vaoID;
	this.vertexCount=vertexCount;
}

}
