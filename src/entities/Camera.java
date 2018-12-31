package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
private float distanceFromPlayer=50;
private float angleAroundPlayer=0;
private Vector3f position=new Vector3f(0,3,10);
private float pitch,yaw,roll;
private Player player;
public Camera(Player player){
	this.player=player;
}
public void move(){
	calculateZoom();
	calculateAngleAroundPlayer();
	calculatePitch();
    float HorizontalDistance=calculateHorizontalDistance();
    float VerticalDistance=calculateVerticalDistance();
    calculateCameraPos( HorizontalDistance,VerticalDistance);

}
private void calculateCameraPos(float horizontal,float vertical){
	float theta=player.getRy()+angleAroundPlayer;
	float Xoffset=(float) (horizontal*Math.sin(Math.toRadians(theta)));
	float Zoffset=(float) (horizontal*Math.cos(Math.toRadians(theta)));
	position.x=player.getPosition().x-Xoffset;
	position.z=player.getPosition().z-Zoffset;
	position.y=player.getPosition().y+vertical;
	this.yaw= 180-(player.getRy()+angleAroundPlayer);
}
public Vector3f getPosition() {
	return position;
}
public float getPitch() {
	return pitch;
}
public float getYaw() {
	return yaw;
}
public float getRoll() {
	return roll;
}
private float calculateHorizontalDistance(){
	return (float) (distanceFromPlayer*Math.cos(Math.toRadians(pitch)));
}
private float calculateVerticalDistance(){
	return (float) (distanceFromPlayer*Math.sin(Math.toRadians(pitch)));
}
private void calculateZoom(){
	float zoomLevel=Mouse.getDWheel()*0.1f;
	if(distanceFromPlayer - zoomLevel > 5f && distanceFromPlayer - zoomLevel < 200f)
	distanceFromPlayer-= zoomLevel;
}
private void calculatePitch(){
	float pitchChange=Mouse.getDY()*0.2f;
	if (pitch - pitchChange > 1 && pitch - pitchChange < 85)
	pitch-=pitchChange;
}
private void calculateAngleAroundPlayer(){
	float AngleChange=Mouse.getDX()*0.3f;
	angleAroundPlayer-=AngleChange;
}
}
