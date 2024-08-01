package texture;

import renderEngine.Loader;

public class ModelTexture {

	private int textureID;
	
	private float shineDamper = 1000;
	private float reflectivity = 0.5f;
	private int normalMap;
	
	public Loader loader = new Loader();
	
	private boolean hasTransparency;
	private boolean useFakeLighting;
	
	private int numberOfRows = 1;
	
	public ModelTexture(int id) {
		this.textureID = id;
	}
	
	public int getID() {
		return this.textureID;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

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

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}
	
	public Loader getLoader() {
		return loader;
	}
	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
	}
	public int getNormalMap() {
		return normalMap;
	}
}
