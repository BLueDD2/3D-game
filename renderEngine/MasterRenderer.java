package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import model.TextureModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyRenderer;
import terrains.Terrain;

public class MasterRenderer {
	
	private static float FOV = 70;
	private static float NEAR_PLANE = 0.1f;
	private static float FAR_PLANE = 1000;
	
	public static Vector3f fogColor;
	
	//0.13f,0.29f,0.41f
	
	private static Matrix4f projectionMatrix;
	
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer entityRender;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private Map<TextureModel, List<Entity>> entities = new HashMap<TextureModel, List<Entity>>();
	private Map<TextureModel, List<Entity>> enTNormal = new HashMap<TextureModel, List<Entity>>();
	private List<Terrain>terrains = new ArrayList<Terrain>();
	
	private SkyRenderer skyRenderer;
	
	public MasterRenderer(Loader loader) {
		createProjectionMatrix();
		entityRender = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyRenderer = new SkyRenderer(loader, projectionMatrix);
		
		fogColor = new Vector3f(0.457f, 0.57f, 0.621f);
	}
	
	public void render(List<Light> sun, Camera cam) {
		prepare();
		shader.start();
		shader.loadSkyColor(fogColor.x, fogColor.y, fogColor.z);
		shader.loadLights(sun);
		shader.loadViewMatrix(cam);
		entityRender.render(entities);
		skyRenderer.render(cam, fogColor.x, fogColor.y, fogColor.z);
		shader.stop();
		terrainShader.start();
		terrainShader.loadSkyColor(fogColor.x, fogColor.y, fogColor.z);
		terrainShader.loadLights(sun);
		terrainShader.loadViewMatrix(cam);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		terrains.clear();
		entities.clear();
	}
	
	public static void setFogColor(Vector3f color) {
        fogColor = color;
    }
	
	public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights,
			Camera camera) {
		for (Terrain terrain : terrains) {
			processTerrain(terrain);
		}
		for (Entity entity : entities) {
			processEntity(entity);
		}
		render(lights, camera);
	}
	
	public void processTerrain(Terrain land) {
		terrains.add(land);
	}
	
	public static Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	public void processEntity(Entity enT) {
		TextureModel entityModel = enT.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch != null) {
			batch.add(enT);
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(enT);
			entities.put(entityModel, newBatch);
		}
		
	}
	
	public void processNormalEntity(Entity enT) {
		TextureModel entityModel = enT.getModel();
		List<Entity> batch = enTNormal.get(entityModel);
		if(batch != null) {
			batch.add(enT);
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(enT);
			enTNormal.put(entityModel, newBatch);
		}
		
	}
	
	
    public void prepare() {
    	GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(fogColor.x, fogColor.y, fogColor.z, 1);
    }
    
    public static void enableCulling() {
    	GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BACK);
    }
	
    public static void disableCulling() {
    	GL11.glDisable(GL11.GL_CULL_FACE);
    }
    
	private void createProjectionMatrix(){
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	public static void updateProjectionMatrix(int width, int height) {
		float aspectRatio = (float) width / (float) height;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
        // Update the shader's projection matrix uniform if necessary
        // shader.loadProjectionMatrix(projectionMatrix);
    }
    
}
