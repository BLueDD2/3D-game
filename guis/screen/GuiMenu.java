package guis.screen;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import engineRunner.Game;
import entities.player.Player;
import fontRendering.TextMaster;
import guis.GuiTexture;
import guis.screen.bttn.GuiButton;
import guis.screen.main.GameState;
import renderEngine.Loader;

public class GuiMenu extends GuiTexture{
	
	private GuiButton resumeBttn;
	private GuiButton exitBttn;
	private GuiButton settingsBttn;
	
	private Player player;
	
	private Loader loader;
	
	public GuiMenu(int texture, Vector2f position, Vector2f scale, Loader loader, Player player, List<GuiTexture> guis) {
		super(texture, 0, position, scale);
		this.player = player;
		this.loader = loader;
		
		resumeBttn = new GuiButton(loader.loadTexture("bttn/buttons"), 4, 2, new Vector2f(0f,0.30f), new Vector2f(0.2f, 0.07f), loader, "Resume", new Vector2f(0.40f,0.335f));
		settingsBttn = new GuiButton(loader.loadTexture("bttn/buttons"), 4, 2, new Vector2f(0f,0.13f), new Vector2f(0.2f, 0.07f), loader, "Settings", new Vector2f(0.40f,0.422f));
		exitBttn = new GuiButton(loader.loadTexture("bttn/buttons"), 4, 2, new Vector2f(0f,-0.088f), new Vector2f(0.2f, 0.07f), loader, "Main Menu", new Vector2f(0.40f,0.53f));
		guis.add(exitBttn);
		guis.add(resumeBttn);
		guis.add(settingsBttn);
		TextMaster.removeText(resumeBttn.text);
        TextMaster.removeText(settingsBttn.text);
        TextMaster.removeText(exitBttn.text);
	}
	
	private boolean isTextLoaded = false;
	
	 public void render(boolean willRender) {
	        if (willRender) {
	            if (!isTextLoaded) {
	                TextMaster.loadText(resumeBttn.text);
	                TextMaster.loadText(settingsBttn.text);
	                TextMaster.loadText(exitBttn.text);
	                isTextLoaded = true; // Set flag to true after loading text
	            }
	            resumeBttn.setState(resumeBttn.STATE_NORMAL);
	            settingsBttn.setState(settingsBttn.STATE_NORMAL);
	            exitBttn.setState(exitBttn.STATE_NORMAL);
	            Action();
	        } else {
	            if (isTextLoaded) {
	                // Remove text only if it was previously loaded
	                TextMaster.removeText(resumeBttn.text);
	                TextMaster.removeText(settingsBttn.text);
	                TextMaster.removeText(exitBttn.text);
	                isTextLoaded = false; // Reset flag after removing text
	            }
	            resumeBttn.setState(resumeBttn.STATE_NONE);
	            settingsBttn.setState(settingsBttn.STATE_NONE);
	            exitBttn.setState(exitBttn.STATE_NONE);
	        }
	    }

	
	public void Action() {
		resumeBttn.Actions();
		exitBttn.Actions();
		settingsBttn.Actions();
		
		if(resumeBttn.getState() == resumeBttn.STATE_CLICKED) {
			Game.isMenuVisible = false;
		}
		
		if(settingsBttn.getState() == settingsBttn.STATE_CLICKED) {
			Game.isMenuVisible = false;
		}
		
		if(exitBttn.getState() == exitBttn.STATE_CLICKED) {
			Game.setGameState(GameState.MAIN_MENU);
		}
		
	}

}
