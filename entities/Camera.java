package entities;


import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import engineRunner.Game;
import entities.player.Player;

public class Camera {

	public float distanceFromPlayer = 50;
	public float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0,5,0);
	public float pitch = 10;
	private float yaw;
	private float roll;
	
	private Player player;
	
	public Camera(Player player){
		this.player =player;
		
		try {
			String posXStr = (String) Game.file.configCam.get("pitch");
            String posYStr = (String) Game.file.configCam.get("angle");
            String posZStr = (String) Game.file.configCam.get("distance");

            this.pitch = posXStr != null ? Float.parseFloat(posXStr) : pitch;
            this.angleAroundPlayer = posYStr != null ? Float.parseFloat(posYStr) : angleAroundPlayer;
            this.distanceFromPlayer = posZStr != null ? Float.parseFloat(posZStr) : distanceFromPlayer;
		}catch (NullPointerException e) {
			this.pitch = 10;
			this.distanceFromPlayer = 50;
			this.angleAroundPlayer = 0;
		}
	}
	
	public void move(){
		changePerspective();
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRy() + angleAroundPlayer);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	public void invertPitch(){
		this.pitch = -pitch;
	}

	
	public void calculateCameraPosition(float hDistance, float vDistance) {
		float theta = player.getRy() + angleAroundPlayer;
		float offsetX = ((float) (hDistance * Math.sin(Math.toRadians(theta))));
		float offsetZ = ((float) (hDistance * Math.cos(Math.toRadians(theta))));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + vDistance;
	}
	
	public float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	public float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	public void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	public void calculatePitch() {
		if(Mouse.isButtonDown(0)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
	public void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
	
	
	public void changePerspective() {
		if(Keyboard.isKeyDown(Keyboard.KEY_F5)) {
			if(angleAroundPlayer == -180.0f) {
				angleAroundPlayer = 0.0f;
			}
			angleAroundPlayer = -180.0f;
		}
	}
}