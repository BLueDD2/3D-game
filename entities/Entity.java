package entities;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import engineRunner.Game;
import model.TextureModel;
import terrains.Terrain;

public class Entity {

	protected TextureModel model;
	protected Vector3f position;
	protected float rx,ry,rz;
	protected float scale;
	protected String ID;
	
	protected int hearts = 5;
	
	protected boolean isDead = false;
	
	private int textureIndex = 0;
	
	public Entity(TextureModel model, Vector3f position, float rx, float ry, float rz, float scale, String ID) { 
		this.model = model;
		this.position = position;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.scale = scale;
		this.ID = ID;
		try {
			String posXStr = (String) Game.file.configEnT.get(ID + " x");
            String posZStr = (String) Game.file.configEnT.get(ID + " z");
            String posYStr = (String) Game.file.configEnT.get(ID + " y");

            this.position.x = posXStr != null ? Float.parseFloat(posXStr) : position.x;
            this.position.z = posZStr != null ? Float.parseFloat(posZStr) : position.z;
            this.position.y = posYStr != null ? Float.parseFloat(posYStr) : position.y;
		}catch (NullPointerException e) {
			this.position.x = position.x;
			this.position.y = position.y;
			this.position.z = position.z;
		}
	}
	
	public Entity(TextureModel model,int index, Vector3f position,float rx, float ry, float rz, float scale, String ID) { 
		this.model = model;
		this.textureIndex = index;
		this.position = position;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.scale = scale;
		this.ID = ID;
		try {
			String posXStr = (String) Game.file.configEnT.get(ID + " x");
            String posZStr = (String) Game.file.configEnT.get(ID + " z");
            String posYStr = (String) Game.file.configEnT.get(ID + " y");

            this.position.x = posXStr != null ? Float.parseFloat(posXStr) : position.x;
            this.position.z = posZStr != null ? Float.parseFloat(posZStr) : position.z;
            this.position.y = posYStr != null ? Float.parseFloat(posYStr) : position.y;
		}catch (NullPointerException e) {
			this.position.x = position.x;
			this.position.y = position.y;
			this.position.z = position.z;
		}
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;

	}
	
	public void increaseRotaion(float dx, float dy, float dz) {
		this.rx+=dx;
		this.ry+=dy;
		this.rz+=dz;

	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	
	public int getHearts() {
		return hearts;
	}

	public void setHearts(int hearts) {
		this.hearts = hearts;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public float getTextureXOffset() {
		int column = textureIndex % model.getModelTexture().getNumberOfRows();
		return (float) column/(float)model.getModelTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset() {
		int row = textureIndex / model.getModelTexture().getNumberOfRows();
		return (float) row/(float)model.getModelTexture().getNumberOfRows();
	}
	
	public void updatePositionBasedOnTerrain(List<Terrain> terrains) {
		 Terrain terrain = Game.getTerrainForPosition(this.position, terrains);
		    if (terrain != null) {
		        float terrainHeight = terrain.getHeightofTerrain(this.position.x, this.position.z);
		        this.position.y = terrainHeight;
		
		    }
    }
	
	private boolean isWithinBounds(Vector3f position, Terrain terrain) {
        return position.x >= terrain.getMinX() && position.x <= terrain.getMaxX() &&
               position.z >= terrain.getMinZ() && position.z <= terrain.getMaxZ();
    }

    private void adjustPositionWithinBounds(Terrain terrain) {
        if (this.position.x < terrain.getMinX()) {
            this.position.x = terrain.getMinX();
        } else if (this.position.x > terrain.getMaxX()) {
            this.position.x = terrain.getMaxX();
        }
        if (this.position.z < terrain.getMinZ()) {
            this.position.z = terrain.getMinZ();
        } else if (this.position.z > terrain.getMaxZ()) {
            this.position.z = terrain.getMaxZ();
        }
    }
	
	public TextureModel getModel() {
		return model;
	}

	public String getID() {
		return ID;
	}
	
	public void setModel(TextureModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRx() {
		return rx;
	}

	public void setRx(float rx) {
		this.rx = rx;
	}

	public float getRy() {
		return ry;
	}

	public void setRy(float ry) {
		this.ry = ry;
	}

	public float getRz() {
		return rz;
	}

	public void setRz(float rz) {
		this.rz = rz;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	
}
