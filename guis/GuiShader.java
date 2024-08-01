package guis;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import shaders.ShadersProgram;

public class GuiShader extends ShadersProgram{
	
	private static final String VERTEX_FILE = "src/guis/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/guis/guiFragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_numberOfRows;
    private int location_offset;
	
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
	}
	
	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	public void loadOffset(float x, float y) {
		super.loadVector2D(location_offset, new Vector2f(x, y));;
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	
	

}
