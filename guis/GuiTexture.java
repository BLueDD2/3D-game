package guis;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {

	private int texture;
	private Vector2f position;
	private Vector2f scale;
	public int textureIndex;
	
	public GuiTexture(int texture, int index, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.textureIndex = index;
		this.position = position;
		this.scale = scale;
	}
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
	private int numberOfRows = 1;
	
	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
	
	public float getTextureXOffset() {
		int column = textureIndex % numberOfRows;
		return (float) (column/(float)numberOfRows);
	}
	
	public float getTextureYOffset() {
		int row = textureIndex / numberOfRows;
		return (float) (row/(float)numberOfRows);
	}
	
	public int getTexture() {
		return texture;
	}
	public void setTexture(int texture) {
		this.texture = texture;
	}
	public Vector2f getPosition() {
		return position;
	}
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	public Vector2f getScale() {
		return scale;
	}
	public void setScale(Vector2f scale) {
		this.scale = scale;
	}
}
