package entities.player;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import engineRunner.Game;
import entities.Entity;
import model.TextureModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Player extends Entity {

	private static final float RUN_SPEED = 30;
	private static final float TURN_SPEED = 80;
	private static final float GRAVITY = -30;
	private static final float JUMP_POWER = 20;
	
	
	public boolean isInAir = false;
	
	private float currentSpeed = 0;
	public float currentTurn = 0;
	private float upwardSpeed = 0;
	
	public Player(TextureModel model, Vector3f position, float rx, float ry, float rz, float scale) {
		super(model, position, rx, ry, rz, scale, "Player 1");
		try {
			String posXStr = (String) Game.file.configEnT.get("Player 1 turn");

			this.ry = posXStr != null ? Float.parseFloat(posXStr) : getRy();
			this.isDead = (boolean) Game.file.configEnT.get("Player 1 isDead");
		}catch (NullPointerException e) {
			this.ry = ry;
			this.isDead = false;
		}
	}

	public void move(List<Terrain> lands) {
		checkInputs();
        if (!isDead()) {
        	super.increaseRotaion(0, currentTurn * DisplayManager.getFrameTimeSeconds(), 0);
            float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
            float dx = (float) (distance * Math.sin(Math.toRadians(super.getRy())));
            float dz = (float) (distance * Math.cos(Math.toRadians(super.getRy())));

            Vector3f newPosition = new Vector3f(super.getPosition().x + dx, super.getPosition().y, super.getPosition().z + dz);
            Terrain land = getTerrainForPosition(newPosition, lands);
            

            if (land != null && isWithinBounds(newPosition, land)) {
                super.increasePosition(dx, 0, dz);
            }
            
            upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
            super.increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);

            if (land != null) {
                float terrainHeight = land.getHeightofTerrain(super.getPosition().x, super.getPosition().z);
                if (super.getPosition().y < terrainHeight) {
                    upwardSpeed = 0;
                    isInAir = false;
                    super.getPosition().y = terrainHeight;
                }
            }
            super.setRx(0);
        } else {
            float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
            float dx = (float) (distance * Math.sin(Math.toRadians(super.getRy())));
            float dz = (float) (distance * Math.cos(Math.toRadians(super.getRy())));

            Vector3f newPosition = new Vector3f(super.getPosition().x + dx, super.getPosition().y, super.getPosition().z + dz);
            Terrain land = getTerrainForPosition(newPosition, lands);
            if (land != null) {
                float terrainHeight = land.getHeightofTerrain(super.getPosition().x, super.getPosition().z);
                super.setRx(90);
                super.setRy(180);
                super.getPosition().y = terrainHeight;
            }
        }
	}
	private void Jump() {
		if(!isInAir) {
		this.upwardSpeed = JUMP_POWER;
		isInAir = true;
		}
	}
	
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				if(isInAir) {
				this.currentSpeed = RUN_SPEED* 1.5f;
				}else {
					this.currentSpeed = RUN_SPEED* 2.5f;
				}
			}else {
			this.currentSpeed = RUN_SPEED;
			}
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		}else {
			this.currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurn = -TURN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurn = TURN_SPEED;
		}else {
			this.currentTurn = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			Jump();
		}
		
	}
	
	public void getHeartData() {
		setHearts(5);
		if(Keyboard.isKeyDown(Keyboard.KEY_F)) {
				hearts++;	
		}
	}
	
	public void getPlayerPosition() {
		if(Keyboard.isKeyDown(Keyboard.KEY_N)) {
			System.out.println("x: " + this.position.x + " y: " + this.position.y + " z: " + this.position.z);
		}
	}
	
	private boolean isWithinBounds(Vector3f position, Terrain terrain) {
		 return position.x >= Game.globalMinX && position.x <= Game.globalMaxX && position.z >= Game.globalMinZ && position.z <= Game.globalMaxZ;
    }

    private Terrain getTerrainForPosition(Vector3f position, List<Terrain> terrains) {
        for (Terrain terrain : terrains) {
            if (terrain.isPositionOnTerrain(position)) {
                return terrain;
            }
        }
        return null;
    }
	
}
