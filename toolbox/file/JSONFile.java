package toolbox.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import engineRunner.Game;
import entities.Camera;
import entities.Entity;
import entities.player.Player;
import toolbox.config.Configuration;
import toolbox.config.ConfigurationAPI;

public class JSONFile {
	
	private File GameFolder = new File("Game");
	private File positionFolder = new File("Game/position");
	
	public Configuration configEnT, configCam;
	public Configuration configToSaveEntities = ConfigurationAPI.newConfiguration(new File("Game/position/Entities.json"));
	public Configuration configToSaveCamera = ConfigurationAPI.newConfiguration(new File("Game/position/Camera.json"));
	
	public void saveFile(List<Entity> enT) {
		if(!GameFolder.exists()) {
			GameFolder.mkdirs();
		}
		if(!positionFolder.exists()) {
			positionFolder.mkdirs();
		}
		System.out.println("Saving Entities Config....");
		
		for(Entity entity:enT) {
			configToSaveEntities.set(entity.getID() + " x", (float)entity.getPosition().getX() + "f");
			configToSaveEntities.set(entity.getID() + " y", (float)entity.getPosition().getY() + "f");
			configToSaveEntities.set(entity.getID() + " z", (float)entity.getPosition().getZ() + "f");
		}
		
		try {
			configToSaveEntities.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void saveFile(Player player) {
		if(!GameFolder.exists()) {
			GameFolder.mkdirs();
		}
		if(!positionFolder.exists()) {
			positionFolder.mkdirs();
		}
		System.out.println("Saving Entities Config....");
		
		configToSaveEntities.set(player.getID() + " x", (float)player.getPosition().getX() + "f");
		configToSaveEntities.set(player.getID() + " y", (float)player.getPosition().getY() + "f");
		configToSaveEntities.set(player.getID() + " z", (float)player.getPosition().getZ() + "f");
		configToSaveEntities.set(player.getID() + " turn", (float)player.getRy() + "f");
		configToSaveEntities.set(player.getID() + " isDead", player.isDead());
		configToSaveEntities.set(player.getID() + " hearts", Game.health2.health);
		
		try {
			configToSaveEntities.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void saveFile(Camera camera) {
		if(!GameFolder.exists()) {
			GameFolder.mkdirs();
		}
		if(!positionFolder.exists()) {
			positionFolder.mkdirs();
		}
		System.out.println("Saving Entities Config....");
		
		configToSaveCamera.set("pitch", (float)camera.pitch + "f");
		configToSaveCamera.set("angle", (float)camera.angleAroundPlayer + "f");
		configToSaveCamera.set("distance", (float)camera.distanceFromPlayer + "f");
		
		try {
			configToSaveCamera.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void loadFile() {
		try {
			configEnT = ConfigurationAPI.loadExistingConfiguration(new File("Game/position/Entities.json"));
			configCam = ConfigurationAPI.loadExistingConfiguration(new File("Game/position/Camera.json"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}


