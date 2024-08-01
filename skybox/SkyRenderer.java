package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import model.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;

public class SkyRenderer {

private static final float SIZE = 600f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static String[] TEXTURE_FILES = {"day/right", "day/left", "day/top", "day/bottom", "day/back", "day/front"};
	private static String[] NIGHT_TEXTURE_FILES = {"night/right", "night/left", "night/top", "night/bottom", "night/back", "night/front"};
	private static String[] MID_TEXTURE_FILES = {"mid/right", "mid/left", "mid/top", "mid/bottom", "mid/back", "mid/front"};
	
	private RawModel cube;
	private int dayTexture;
	private SkyboxShader shader;
	private int nightTexture;
	private int midTexture;
	private float time;
	
	public SkyRenderer(Loader loader, Matrix4f projectionMatrix) {
		cube = loader.loadToVAO(VERTICES, 3);
		dayTexture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		midTexture = loader.loadCubeMap(MID_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTexture();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Camera camera, float r, float g, float b) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColor(r, g, b);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void bindTextures(){
		time += DisplayManager.getFrameTimeSeconds();
		time %= 20000;
		int texture1;
		int texture2;
		float blendFactor;
		Vector3f skyColor;  // Define a Vector3f to hold the sky color

		Vector3f day = new Vector3f(0.457f, 0.57f, 0.621f);
		Vector3f mid = new Vector3f(1.0f, 0.60f, 0.20f);
		Vector3f night = new Vector3f(0.0f, 0.0f, 0.0f);
		
		if (time >= 0 && time < 3000) {
		    texture1 = dayTexture;
		    texture2 = midTexture;
		    blendFactor = (time - 0) / (3000 - 0);
		    skyColor = interpolateColor(day, mid, blendFactor);
		} else if (time >= 3000 && time < 4000) {
		    texture1 = midTexture;
		    texture2 = midTexture;
		    blendFactor = (time - 3000) / (4000 - 3000);
		    skyColor = mid;
		} else if (time >= 4000 && time < 5000) {
		    texture1 = midTexture;
		    texture2 = nightTexture;
		    blendFactor = (time - 4000) / (5000 - 4000);
		    skyColor = interpolateColor(mid, night, blendFactor);
		} else if (time >= 5000 && time < 6000) {
		    texture1 = nightTexture;
		    texture2 = nightTexture;
		    blendFactor = (time - 5000) / (6000 - 5000);
		    skyColor = night;
		} else if (time >= 6000 && time < 12000) {
		    texture1 = nightTexture;
		    texture2 = midTexture;
		    blendFactor = (time - 6000) / (12000 - 6000);
		    skyColor = interpolateColor(night, mid, blendFactor);
		} else if (time >= 12000 && time < 17000) {
		    texture1 = midTexture;
		    texture2 = midTexture;
		    blendFactor = (time - 12000) / (17000 - 12000);
		    skyColor = mid;
		} else if (time >= 17000 && time < 21000) {
		    texture1 = midTexture;
		    texture2 = dayTexture;
		    blendFactor = (time - 17000) / (21000 - 17000);
		    skyColor = interpolateColor(mid, day, blendFactor);
		} else {
		    texture1 = dayTexture;
		    texture2 = dayTexture;
		    blendFactor = (time - 21000) / (24000 - 21000);
		    skyColor = day;
		}

		MasterRenderer.setFogColor(skyColor);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}
	
	
	private Vector3f interpolateColor(Vector3f color1, Vector3f color2, float blendFactor) {
	    float r = color1.x * (1 - blendFactor) + color2.x * blendFactor;
	    float g = color1.y * (1 - blendFactor) + color2.y * blendFactor;
	    float b = color1.z * (1 - blendFactor) + color2.z * blendFactor;
	    return new Vector3f(r, g, b);
	}
}
