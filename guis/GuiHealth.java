package guis;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import engineRunner.Game;
import entities.player.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import renderEngine.Loader;


public class GuiHealth extends GuiTexture{

	public static int health;
	private static long lastFKeyPressTime = 0;
    private static final long COOLDOWN_PERIOD = 300;

    private Player player;
   
    private FontType font;
    private GUIText text;
    
    public GuiHealth(int texture, Vector2f position, Vector2f scale, Player player, Loader loader) {
        super(texture, position, scale);
        try {
        	health = (int) Game.file.configEnT.get("Player 1 hearts");
        }catch (NullPointerException e) {
        	getImageXandY(player);
		}
        this.player = player;
        this.font = new FontType(loader.loadTexture("font/arial"), new File("src/assets/textures/font/arial.fnt"));
        this.text  = new GUIText("Health: ", 1.5f, font, new Vector2f(0.01f,0.05f), 1, false);
        this.text.setColour(1, 1, 1);
        this.setPosition(new Vector2f(position.x + 0.12f,position.y));
    }
    
    public void decreaseHealth() {
        if (health < 5) {
            health++;
            updateHealth();
            System.out.println("Health decreased to: " + health); // Debug output
        } else {
            System.out.println("Health is already at minimum."); // Debug output
        }
        
        if(health == 5) {
        	player.setDead(true);
        }else {
        	player.setDead(false);
        }
    }

    public void updateHealth() {
        textureIndex = health;
        System.out.println("Texture index updated to: " + textureIndex); 
    }
    
    public void getImageXandY(Player player) {
        player.getHeartData();
        health = player.getHearts();
        
    }

    public void checkInputs() {
        if (Keyboard.isKeyDown(Keyboard.KEY_F) && canPressFKey()) {
            System.out.println("F key pressed"); // Debug output
            decreaseHealth();
            lastFKeyPressTime = System.currentTimeMillis();
        }
    }

    private boolean canPressFKey() {
        return (System.currentTimeMillis() - lastFKeyPressTime) >= COOLDOWN_PERIOD;
    }

}
