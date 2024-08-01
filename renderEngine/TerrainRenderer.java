package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import model.RawModel;
import shaders.TerrainShader;
import terrains.Terrain;
import texture.ModelTexture;
import texture.TerrainTexture;
import texture.TerrainTexturePack;
import toolbox.Maths;

public class TerrainRenderer {

	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		this.shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		this.shader.stop();
	}
	
	public void render(List<Terrain> land) {
		for(Terrain terrain:land) {
			preparedTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTerrain();
		}
	}
	
	public void render(Terrain terrain) {
			preparedTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTerrain();
		
	}
	
	 private void preparedTerrain(Terrain land) {
	    	RawModel rawModel = land.getModel();
			GL30.glBindVertexArray(rawModel.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			bindTexture(land);
			shader.loadShineVariables(1000, 10);
		}
	 
	 private void bindTexture(Terrain terrain) {
		TerrainTexturePack pack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getBackgroundTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getrTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getgTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getbTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
	 }
	    
	 private void unbindTerrain() {
	    	GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
			GL30.glBindVertexArray(0);
	 }
	    
	 private void loadModelMatrix(Terrain land) {
	    	Matrix4f transformationMatrix = Maths.createTransformationMatrix(
	    			new Vector3f(land.getX(),0,land.getZ()), 0, 0, 0, 1);
			shader.loadTransformationMatrix(transformationMatrix);
	 }
	
}
