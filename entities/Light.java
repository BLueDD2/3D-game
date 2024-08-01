package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {

	public Vector3f position;
	public Vector3f color;
	public Vector3f attenuation;
	
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
		attenuation = new Vector3f(1,0,0);
	}
	
	public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}

	public Vector3f getPosition() {
		return position;
	}


	public Vector3f getAttenuation() {
		return attenuation;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	
	
	
}
