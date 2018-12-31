package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class Entity {
 private TexturedModel texmodel;
 private Vector3f position;
 private float rx,ry,rz,scale;
public Entity(TexturedModel texmodel, Vector3f position, float rx, float ry, float rz, float scale) {
	this.texmodel = texmodel;
	this.position = position;
	this.rx = rx;
	this.ry = ry;
	this.rz = rz;
	this.scale = scale;
}

public void increasePosition(float dx,float dy,float dz){
	position.x+=dx;
	position.y+=dy;
	position.z+=dz;

}

public void increaseRotation(float dx,float dy,float dz){
	rx+=dx;
	ry+=dy;
	rz+=dz;
}

public TexturedModel getTexmodel() {
	return texmodel;
}
public void setTexmodel(TexturedModel texmodel) {
	this.texmodel = texmodel;
}
public Vector3f getPosition() {
	return position;
}
public void setPosition(Vector3f position) {
	this.position = position;
}
public float getRx() {
	return rx;
}
public void setRx(float rx) {
	this.rx = rx;
}
public float getRy() {
	return ry;
}
public void setRy(float ry) {
	this.ry = ry;
}
public float getRz() {
	return rz;
}
public void setRz(float rz) {
	this.rz = rz;
}
public float getScale() {
	return scale;
}
public void setScale(float scale) {
	this.scale = scale;
}


}
