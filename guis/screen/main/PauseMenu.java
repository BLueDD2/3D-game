package guis.screen.main;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import engineRunner.Game;
import fontRendering.TextMaster;
import guis.GuiTexture;
import guis.screen.bttn.GuiButton;
import renderEngine.Loader;

public class PauseMenu {

    private GuiButton resumeBttn;
    private GuiButton menuBttn;
    private GuiButton settingsBttn;

    public PauseMenu(Loader loader, List<GuiTexture> guis) {
    	resumeBttn = new GuiButton(loader.loadTexture("bttn/buttons"), 4, 2, new Vector2f(0f,0.30f), new Vector2f(0.2f, 0.07f), loader, "Resume", new Vector2f(0.40f,0.335f));
		settingsBttn = new GuiButton(loader.loadTexture("bttn/buttons"), 4, 2, new Vector2f(0f,0.13f), new Vector2f(0.2f, 0.07f), loader, "Settings", new Vector2f(0.40f,0.422f));
		menuBttn = new GuiButton(loader.loadTexture("bttn/buttons"), 4, 2, new Vector2f(0f,-0.088f), new Vector2f(0.2f, 0.07f), loader, "Main Menu", new Vector2f(0.40f,0.53f));
		
		guis.add(resumeBttn);
		guis.add(settingsBttn);
		guis.add(menuBttn);
		TextMaster.removeText(resumeBttn.text);
        TextMaster.removeText(settingsBttn.text);
        TextMaster.removeText(menuBttn.text);
	}
    
    private boolean isTextLoaded = false;
	
	 public void render(boolean willRender) {
	        if (willRender) {
	            if (!isTextLoaded) {
	                TextMaster.loadText(resumeBttn.text);
	                TextMaster.loadText(settingsBttn.text);
	                TextMaster.loadText(menuBttn.text);
	                isTextLoaded = true; // Set flag to true after loading text
	            }
	            resumeBttn.setState(resumeBttn.STATE_NORMAL);
	            settingsBttn.setState(settingsBttn.STATE_NORMAL);
	            menuBttn.setState(menuBttn.STATE_NORMAL);
	            Action();
	        } else {
	            if (isTextLoaded) {
	                // Remove text only if it was previously loaded
	                TextMaster.removeText(resumeBttn.text);
	                TextMaster.removeText(settingsBttn.text);
	                TextMaster.removeText(menuBttn.text);
	                isTextLoaded = false; // Reset flag after removing text
	            }
	            resumeBttn.setState(resumeBttn.STATE_NONE);
	            settingsBttn.setState(settingsBttn.STATE_NONE);
	            menuBttn.setState(menuBttn.STATE_NONE);
	        }
	    }

    
    public void Action() {
        resumeBttn.Actions();
        menuBttn.Actions();
        settingsBttn.Actions();

        if (resumeBttn.getState() == GuiButton.STATE_CLICKED) {
            Game.setGameState(GameState.IN_GAME);
        }

        if (settingsBttn.getState() == GuiButton.STATE_CLICKED) {
            Game.setGameState(GameState.SETTINGS);
        }

        if (menuBttn.getState() == GuiButton.STATE_CLICKED) {
        	Game.mainMenu = new MainMenu(Game.loaders,Game. guiss);
        	//Main.mainMenu.addBackground();
            Game.setGameState(GameState.MAIN_MENU);
        }
    }

    // Add other necessary methods and initializations
}