package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderengine.DisplayManager;
import terrain.Terrain;

public class Player extends Entity {
private static float RUN_SPEED=30;
private static float TURN_SPEED=160;
private static float GRAVITY=-50;
private static float JUMP_POWER=15;

private float currentSpeed=0;
private float currentTurnSpeed=0;
private float upwardSpeed=0;
private boolean isinAir=false;
	public Player(TexturedModel texmodel, Vector3f position, float rx, float ry, float rz, float scale) {
		super(texmodel, position, rx, ry, rz, scale);
	}
 public void move(Terrain terrain){
	 checkInputs();
	 float distance=currentSpeed*DisplayManager.getTimeinSeconds();
	 float dx=distance*(float) Math.sin(Math.toRadians(super.getRy()));
	 float dz=distance*(float) Math.cos(Math.toRadians(super.getRy()));
	 super.increasePosition(dx,0,dz);
	 super.increaseRotation(0, currentTurnSpeed*DisplayManager.getTimeinSeconds(), 0);
     upwardSpeed+=GRAVITY*DisplayManager.getTimeinSeconds();
     super.increasePosition(0, upwardSpeed*DisplayManager.getTimeinSeconds(), 0);
   float terrainHeight=terrain.getHeightofTerrain(super.getPosition().x, super.getPosition().z);
     if(super.getPosition().y<terrainHeight){
    	upwardSpeed=0;
    super.getPosition().y=terrainHeight;
       isinAir=false;
       }
    }
private void jump(){
	if(!isinAir){
	upwardSpeed=JUMP_POWER;
	isinAir=true; 
	}
}
 private void checkInputs(){
	if(Keyboard.isKeyDown(Keyboard.KEY_W))
		if(!isinAir)
		this.currentSpeed=-RUN_SPEED;
		else
			this.currentSpeed=-(RUN_SPEED-JUMP_POWER/2);
		else if(Keyboard.isKeyDown(Keyboard.KEY_S))
			if(!isinAir)
				this.currentSpeed=RUN_SPEED;
				else
					this.currentSpeed=(RUN_SPEED-JUMP_POWER/2);
	else currentSpeed=0;
	if(Keyboard.isKeyDown(Keyboard.KEY_D))
		this.currentTurnSpeed=-TURN_SPEED;
	else if(Keyboard.isKeyDown(Keyboard.KEY_A))
		this.currentTurnSpeed=TURN_SPEED;
	else currentTurnSpeed=0;
	if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
              jump();
 }
}
