package textures;

import java.nio.ByteBuffer;

public class TextureData {
private int Width;
private int Height;
private ByteBuffer buffer;
public int getWidth() {
	return Width;
}
public int getHeight() {
	return Height;
}
public ByteBuffer getBuffer() {
	return buffer;
}
public TextureData(int width, int height, ByteBuffer buffer) {
	Width = width;
	Height = height;
	this.buffer = buffer;
}

}
