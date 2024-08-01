package guis.screen;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engineRunner.Game;
import entities.player.Player;
import fontRendering.TextMaster;
import guis.GuiTexture;
import guis.screen.bttn.GuiButton;
import renderEngine.Loader;

public class GUIRespawn extends GuiTexture {

	private GuiButton button;

	private Player player;
	
	private Loader loader;
	
	public GUIRespawn(int texture, Vector2f position, Vector2f scale, Loader loader, Player player, List<GuiTexture> guis) {
		super(texture, 0, position, scale);
		this.player = player;
		this.loader = loader;
		button = new GuiButton(loader.loadTexture("bttn/buttons"), 4, 2, new Vector2f(0f,0f), new Vector2f(0.2f, 0.07f), loader, "Respawn", new Vector2f(0.40f,0.485f));
		guis.add(button);
		TextMaster.removeText(button.text);
	}

	private boolean isTextLoaded = false;
	
	public void render(boolean willRender) {
		if(willRender) {
			if(!isTextLoaded) {
				TextMaster.loadText(button.text);
				isTextLoaded = true;
			}
			button.setState(button.STATE_NORMAL);
			Actions();
		}else {
			if(isTextLoaded) {
				TextMaster.removeText(button.text);
				isTextLoaded = false;
			}
			button.setState(button.STATE_NONE);
		}
	}
	
	 public void Actions() {
	    	button.Actions();
	    	if(button.getState() == button.STATE_CLICKED) {
	    		mouseClicked();
	    	}
		}
	    
	   public void mouseClicked() {
			player.setDead(false);
			Game.health2.health = 5;
			Game.health2.updateHealth();
			player.setPosition(new Vector3f(-397.23422f,-10.897987f,-525.5713f));
	}
	
}
