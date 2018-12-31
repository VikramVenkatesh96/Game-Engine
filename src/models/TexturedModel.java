package models;

import textures.ModelTexture;

public class TexturedModel {
private RawModel rawmodel;
private ModelTexture modeltexture;
public TexturedModel(RawModel rawmodel,ModelTexture modeltexture){
	this.rawmodel=rawmodel;
	this.modeltexture=modeltexture;
}
public RawModel getRawmodel() {
	return rawmodel;
}
public ModelTexture getModeltexture() {
	return modeltexture;
}



}
