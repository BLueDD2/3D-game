package guis.screen.main;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import engineRunner.Game;
import fontRendering.TextMaster;
import guis.GuiTexture;
import guis.screen.bttn.GuiButton;
import renderEngine.Loader;

public class MainMenu {

	public boolean close;
	
    private GuiButton startButton;
    private GuiButton settingsButton;
    private GuiButton exitButton;

    private GuiTexture background;
    
    private Loader loader;
    
    private List<GuiTexture> guis;

    public MainMenu(Loader loader, List<GuiTexture> guis) {
        this.loader = loader;

        this.guis = guis;
        
        if (loader == null || guis == null) {
            throw new IllegalArgumentException("Loader or GUI list cannot be null");
        }

        // Load the background texture and create a GuiTexture for it
        int backgroundTexture = loader.loadTexture("background");
        if (backgroundTexture == 0) {
            System.err.println("Error: Background texture failed to load.");
        }
        background = new GuiTexture(backgroundTexture, new Vector2f(1f, 0f), new Vector2f(2f, 2.5f));
        
        startButton = new GuiButton(loader.loadTexture("bttn/buttons"), 4, 2, new Vector2f(0f, 0.3f), new Vector2f(0.2f, 0.07f), loader, "Start", new Vector2f(0.4f, 0.335f));
        settingsButton = new GuiButton(loader.loadTexture("bttn/buttons"), 4, 2, new Vector2f(0f, 0.13f), new Vector2f(0.2f, 0.07f), loader, "Settings", new Vector2f(0.4f, 0.422f));
        exitButton = new GuiButton(loader.loadTexture("bttn/buttons"), 4, 2, new Vector2f(0f, -0.088f), new Vector2f(0.2f, 0.07f), loader, "Exit", new Vector2f(0.4f, 0.53f));
        
        guis.add(background);
        guis.add(startButton);
        guis.add(settingsButton);
        guis.add(exitButton);
        
        TextMaster.removeText(settingsButton.text);
        TextMaster.removeText(startButton.text);
        TextMaster.removeText(exitButton.text);
        System.out.println("MainMenu initialized with background texture ID: " + backgroundTexture);
    }
    
    private boolean isTextLoaded = false;

    public void render(boolean willRender) {
        if (willRender) {
        	if(!isTextLoaded) {
        		System.out.println("Rendering MainMenu...");
	            TextMaster.loadText(startButton.text);
	            TextMaster.loadText(settingsButton.text);
	            TextMaster.loadText(exitButton.text);
	            isTextLoaded = true;
        	}
            startButton.setState(GuiButton.STATE_NORMAL);
            settingsButton.setState(GuiButton.STATE_NORMAL);
            exitButton.setState(GuiButton.STATE_NORMAL);
            handleActions();

        } else {
        	this.guis.remove(background);
        	if(isTextLoaded) {
	        	TextMaster.removeText(startButton.text);
	        	TextMaster.removeText(settingsButton.text);
	        	TextMaster.removeText(exitButton.text);
	        	 isTextLoaded = false;
        	}
            startButton.setState(GuiButton.STATE_NONE);
            settingsButton.setState(GuiButton.STATE_NONE);
            exitButton.setState(GuiButton.STATE_NONE);
        }
    }

    private void handleActions() {
        startButton.Actions();
        settingsButton.Actions();
        exitButton.Actions();

        if (startButton.getState() == GuiButton.STATE_CLICKED) {
            Game.setGameState(GameState.IN_GAME);
            System.out.println("Start button clicked. Transitioning to IN_GAME state.");
        }
        
        if (startButton.getState() == GuiButton.STATE_HOVERED) {
        	
        }
        
        if (settingsButton.getState() == GuiButton.STATE_CLICKED) {
        	Game.setGameState(GameState.SETTINGS);
        }
        if (exitButton.getState() == GuiButton.STATE_CLICKED) {
        	Game.closeMethod();
        }
    }
    
    public void addBackground() {
    	guis.add(background);
	}
}