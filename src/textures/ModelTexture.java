package textures;

public class ModelTexture {
  private int texID;
  private float shineDamper=1;
  private float reflectivity=0;
  public float getShineDamper() {
	return shineDamper;
}
public void setShineDamper(float shineDamper) {
	this.shineDamper = shineDamper;
}
public float getReflectivity() {
	return reflectivity;
}
public void setReflectivity(float reflectivity) {
	this.reflectivity = reflectivity;
}
public ModelTexture(int texID){
	  this.texID=texID;
  }
public int texId(){
	return texID;
	
}
}
