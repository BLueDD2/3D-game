package engineRunner;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.player.Player;
import fontRendering.TextMaster;
import guis.GuiHealth;
import guis.GuiHealth2;
import guis.GuiRenderer;
import guis.GuiTexture;
import guis.screen.GUIRespawn;
import guis.screen.main.GameState;
import guis.screen.main.MainMenu;
import guis.screen.main.PauseMenu;
import model.RawModel;
import model.TextureModel;
import obj.ModelData;
import obj.OBJLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import texture.ModelTexture;
import texture.TerrainTexture;
import texture.TerrainTexturePack;
import toolbox.MousePicker;
import toolbox.file.JSONFile;

public class Game {

	public static JSONFile file = new JSONFile();
	
	public static float globalMinX;
	public static float globalMaxX;
	public static float globalMinZ;
	public static float globalMaxZ;
	
	public static GuiHealth health;
	
	public static GuiHealth2 health2;
	
	public static MainMenu mainMenu;
	 
	public static PauseMenu pauseMenu;
	 
	public static GameState gameState = GameState.MAIN_MENU;
	
	public static Loader loaders;
	public static List<GuiTexture> guiss;
    private static boolean pauseKeyPressed = false;
	
    public static boolean isMenuVisible = false;
    
    public static Player playerMain;
    public static List<Entity> natureEnTs;
    
    public static MasterRenderer renderers;
    public static GuiRenderer guiRenderers;
    
    public static Camera camera;
    
	public static void main(String[] args) {
		file.loadFile();
		DisplayManager.createDisplay();
		
		
		ModelData dataGrass = OBJLoader.loadOBJ("grassModel");
		ModelData dataFern = OBJLoader.loadOBJ("fern");
		ModelData dataPolyTree = OBJLoader.loadOBJ("lowPolyTree");
		ModelData dataPine = OBJLoader.loadOBJ("pine");
		ModelData dataPlayer = OBJLoader.loadOBJ("person");
		ModelData dataNPC = OBJLoader.loadOBJ("person");
		
		Loader loader = new Loader();
		loaders = loader;
		TextMaster.init(loader);
		
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		
		//********TERRAIN STUFF BRUH********
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture backgroundTexture_Red = new TerrainTexture(loader.loadTexture("grass_red"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture gTexture2 = new TerrainTexture(loader.loadTexture("grassFlowers_red"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack tPack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		//TerrainTexturePack tPack2 = new TerrainTexturePack(backgroundTexture_Red, rTexture, gTexture2, bTexture);
		
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//***********************************
		
		RawModel grass = loader.loadToVAO(dataGrass.getVertices(), dataGrass.getTextureCoords(), dataGrass.getNormals(), dataGrass.getIndices());
		RawModel fern = loader.loadToVAO(dataFern.getVertices(), dataFern.getTextureCoords(), dataFern.getNormals(), dataFern.getIndices());
		RawModel polyTree = loader.loadToVAO(dataPolyTree.getVertices(), dataPolyTree.getTextureCoords(), dataPolyTree.getNormals(), dataPolyTree.getIndices());
		RawModel pine = loader.loadToVAO(dataPine.getVertices(), dataPine.getTextureCoords(), dataPine.getNormals(), dataPine.getIndices());
		RawModel npc = loader.loadToVAO(dataNPC.getVertices(), dataNPC.getTextureCoords(), dataNPC.getNormals(), dataNPC.getIndices());
		
		TextureModel grassModel = new TextureModel(grass,new ModelTexture(loader.loadTexture("grassTexture")));
		TextureModel fernModel = new TextureModel(fern,fernTextureAtlas);
		TextureModel polyTreeModel = new TextureModel(polyTree,new ModelTexture(loader.loadTexture("lowPolyTree1")));
		TextureModel pineModel = new TextureModel(pine,new ModelTexture(loader.loadTexture("pine")));
		TextureModel npcModel = new TextureModel(npc,new ModelTexture(loader.loadTexture("npcTexture")));
		
		grassModel.getModelTexture().setHasTransparency(true);
		grassModel.getModelTexture().setUseFakeLighting(true);
		fernModel.getModelTexture().setHasTransparency(true);
		polyTreeModel.getModelTexture().setHasTransparency(true);		
		
		 List<Terrain> terrainList = new ArrayList<Terrain>();
		for (int i = 0; i < 2; i++) {
		   terrainList.add(new Terrain(1, -i, loader, tPack, blendMap, "heightMap", "1"));
		   terrainList.add(new Terrain(-1, -i, loader, new TerrainTexturePack(backgroundTexture_Red, rTexture, gTexture2, bTexture), blendMap, "heightMap", "2"));
		   calculateGlobalBoundaries(terrainList);
		}
	
		//******PLAYER*****
		
		RawModel playerRaw = loader.loadToVAO(dataPlayer.getVertices(), dataPlayer.getTextureCoords(), dataPlayer.getNormals(), dataPlayer.getIndices());
		TextureModel playerModel = new TextureModel(playerRaw,new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(playerModel, new Vector3f(-397.23422f,-10.897987f,-525.5713f), 0, 0, 0, 1);	
		playerMain = player;
		//*****************
		
		    
		List<Entity> natureEnT = new ArrayList<Entity>();
		natureEnTs = natureEnT;
		Random random = new Random(676452);
		for(int i=0;i<300;i++){
			if(i %20 == 0) {
			natureEnT.add(new Entity(polyTreeModel, new Vector3f(random.nextFloat() * 500 - 400, 0, random.nextFloat() * -600),0,random.nextFloat()*360,0,0.99f, "polyTreeModel " + natureEnT.size()));
			natureEnT.add(new Entity(pineModel, new Vector3f(random.nextFloat() * 500 - 400, 0, random.nextFloat() * -600),0,random.nextFloat()*360,0,0.99f, "polyTreeModel " + natureEnT.size()));
			for(int j=0;j<2;j++) {
			natureEnT.add(new Entity(pineModel, new Vector3f(random.nextFloat() * 500 - 400, 0, (random.nextFloat() * -600)*-j),0,random.nextFloat()*360,0,0.99f, "polyTreeModel " + natureEnT.size()));
			natureEnT.add(new Entity(polyTreeModel, new Vector3f(random.nextFloat() * 500 - 400, 0, (random.nextFloat() * -600)*-j),0,random.nextFloat()*360,0,0.99f, "polyTreeModel " + natureEnT.size()));
				}
			}
		}
		for(int i=0;i<100;i++) {
				if(i % 25 == 0) {
			natureEnT.add(new Entity(grassModel, new Vector3f(random.nextFloat() * 500 - 400, 0, random.nextFloat() * -600),0,random.nextFloat()*360,0,2f, "grassModel " + natureEnT.size()));
			for(int j=0;j<2;j++)
				natureEnT.add(new Entity(grassModel, new Vector3f(random.nextFloat() * 500 - 400, 0, (random.nextFloat() * -600)*-j),0,random.nextFloat()*360,0,0.99f, "grassModel " + natureEnT.size()));
			}
		}
		for(int i=0;i<20;i++) {
				if(i % 15 == 0) {
			natureEnT.add(new Entity(npcModel, new Vector3f(random.nextFloat() * 500 - 400, 0, (random.nextFloat() * -600)),0,0,0,0.99f, "npcModel " + natureEnT.size()));
			for(int j=0;j<2;j++)
			natureEnT.add(new Entity(npcModel, new Vector3f(random.nextFloat() * 500 - 400, 0, (random.nextFloat() * -600)*-j),0,0,0,0.99f, "npcModel " + natureEnT.size()));
			}
				
		}
		
		for(int i=0;i<100;i++) {
			if(i % 30 == 0) {
			natureEnT.add(new Entity(fernModel,random.nextInt(4), new Vector3f(random.nextFloat() * 500 - 400, 0, (random.nextFloat() * -600)),0,0,0,0.99f, "npcModel " + natureEnT.size()));
			for(int j=0;j<2;j++)
			natureEnT.add(new Entity(fernModel,random.nextInt(4), new Vector3f(random.nextFloat() * 500 - 400, 0, (random.nextFloat() * -600)*-j),0,0,0,0.99f, "npcModel " + natureEnT.size()));
			}
			
		}
		
		 for (Entity entity : natureEnT) {
	       entity.updatePositionBasedOnTerrain(terrainList);
	     }
		
		Camera cam = new Camera(player);
		camera = cam;
		
		MasterRenderer renderer = new MasterRenderer(loader);
		renderers = renderer;
		
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0,20000,-7000),new Vector3f(0.6f,0.6f,0.6f)));
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		guiRenderers = guiRenderer;
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		guiss = guis;
		
		MousePicker picker = new MousePicker(cam, renderer.getProjectionMatrix(), getTerrain(terrainList));
		GUIRespawn guiRes = new GUIRespawn(0, new Vector2f(-0.5f, 0), new Vector2f(0, 0), loader, player, guis);

		guis.add(guiRes);
		
		pauseMenu = new PauseMenu(loader, guis);
		
		mainMenu = new MainMenu(loader, guis);
		
		health2 = new GuiHealth2( new Vector2f(-0.9f,0.84f), new Vector2f(0.05f,0.08f), player, loader);
		
		while (!Display.isCloseRequested()) {
			
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                if (!pauseKeyPressed) {
                    if (gameState == GameState.IN_GAME) {
                        setGameState(GameState.PAUSE_MENU);
                    } else if (gameState == GameState.PAUSE_MENU) {
                        setGameState(GameState.IN_GAME);
                    }
                    pauseKeyPressed = true;
                }
            } else {
                pauseKeyPressed = false;
            }
			
			 if(gameState == GameState.MAIN_MENU) {
            	  mainMenu.render(true);
            	  pauseMenu.render(false);
            	  isHealthLoad = false;
            	  TextMaster.removeText(health2.text);
			 } else if(gameState == GameState.IN_GAME) {
				 if(!isHealthLoad) {
				TextMaster.loadText(health2.text);
				isHealthLoad = true;
			}
				guiRes.render(player.isDead());
				mainMenu.render(false);
				pauseMenu.render(false);
            	health2.checkInputs();
            	player.move(terrainList);
            	player.getPlayerPosition();
        		cam.move();
        		picker.update();
        		renderer.renderScene(natureEnT, terrainList, lights, cam);
        		renderer.processEntity(player);
        		
             }else if(gameState == GameState.SETTINGS) {
                // Render settings menu
             }else if(gameState == GameState.CREDITS) {
                // Render credits screen
            }else if(gameState == GameState.PAUSE_MENU) {
            	isHealthLoad = false;
            	TextMaster.removeText(health2.text);
            	pauseMenu.render(true);
            	mainMenu.render(false);
            	pauseMenu.Action();
            	guiRes.render(false);
            }
		guiRenderer.render(guis);
		TextMaster.render();
		DisplayManager.updateDisplay();
		}
		
		closeMethod();
		
	}
	
	static boolean isHealthLoad = false;
	
	public static void closeMethod() {
		file.saveFile(natureEnTs);
		file.saveFile(playerMain);
		file.saveFile(camera);
		
		guiRenderers.cleanUp();
		renderers.cleanUp();
		loaders.cleanUp();
		TextMaster.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	private static void calculateGlobalBoundaries(List<Terrain> terrains) {
	    Game.globalMinX = Float.MAX_VALUE;
	    Game.globalMaxX = Float.MIN_VALUE;
	    Game.globalMinZ = Float.MAX_VALUE;
	    Game.globalMaxZ = Float.MIN_VALUE;
	
	    for (Terrain terrain : terrains) {
	        if (terrain.getMinX() < Game.globalMinX) Game.globalMinX = terrain.getMinX();
	        if (terrain.getMaxX() > Game.globalMaxX) Game.globalMaxX = terrain.getMaxX();
	        if (terrain.getMinZ() < Game.globalMinZ) Game.globalMinZ = terrain.getMinZ();
	        if (terrain.getMaxZ() > Game.globalMaxZ) Game.globalMaxZ = terrain.getMaxZ();
	    }
    }

	public static Terrain getTerrainForPosition(Vector3f position, List<Terrain> terrains) {
	    for (Terrain terrain : terrains) {
	        if (terrain.isPositionOnTerrain(position)) {
	            return terrain;
	        }
	    }
	    return null;
	}
	
	public static void setGameState(GameState state) {
        gameState = state;
    }
	
	public static Terrain getTerrain(List<Terrain> terrains) {
	    for (Terrain terrain : terrains) {
	            return terrain;
	    }
	    return null;
	}
}
