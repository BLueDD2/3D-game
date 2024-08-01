package guis.screen.bttn;
import java.io.File;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import guis.GuiTexture;
import renderEngine.Loader;

public class GuiButton extends GuiTexture {

    private int atlasRows;
    private int atlasCols;
    private int currentState;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_HOVERED = 1;
    public static final int STATE_CLICKED = 2;
    public static final int STATE_DISABLED = 3;
    public static final int STATE_NONE = 5;

    private FontType font;
    public GUIText text;
    
    public float fsz = 1f;
    
    public GuiButton(int texture, int atlasRows, int atlasCols, Vector2f position, Vector2f scale, Loader loader, String text, Vector2f textpos) {
    	super(texture, position, scale);
        this.atlasRows = atlasRows;
        this.atlasCols = atlasCols;
        this.currentState = STATE_NORMAL;
        this.font = new FontType(loader.loadTexture("font/arial"), new File("src/assets/textures/font/arial.fnt"));
        this.text = new GUIText(text, fsz, font, textpos, 0.2f, true);
        
        setNumberOfRows(atlasRows);

    }
    public void setState(int state) {
        this.currentState = state;
        // Optionally update text position or appearance based on state
    }

    public float getAverageScale(Vector2f scale) {
        return (scale.x + scale.y) / 2.0f;
    }
    
    public int getState() {
        return currentState;
    }

    @Override
    public float getTextureXOffset() {
        int column = currentState % atlasCols;
        return (float) column / atlasCols;
    }

    public boolean isMouseOverButton() {
        Vector2f mousePos = getMousePosition();
        Vector2f buttonPos = this.getPosition();
        Vector2f buttonScale = this.getScale();

        return mousePos.x > buttonPos.x - buttonScale.x / 2 && mousePos.x < buttonPos.x + buttonScale.x / 2 &&
               mousePos.y > buttonPos.y - buttonScale.y / 2 && mousePos.y < buttonPos.y + buttonScale.y / 2;
    }
    public void Actions() {
    	 if (isMouseOverButton()) {
             if (isMouseClicked()) {
                 setState(GuiButton.STATE_CLICKED);
                 // Handle button click logic here
             } else {
                 setState(GuiButton.STATE_HOVERED);
                 text.setColour(0, 0, 0);
                 fsz = 1.5f;
             }
         } else {
             setState(GuiButton.STATE_NORMAL);
             text.setColour(1, 1, 1);
             fsz = 1f;
         }
	}
    
    public Vector2f getMousePosition() {
        float mouseX = (float) Mouse.getX() / Display.getWidth() * 2 - 1;
        float mouseY = (float) Mouse.getY() / Display.getHeight() * 2 - 1;
        return new Vector2f(mouseX, mouseY);
    }

    public boolean isMouseClicked() {
        return Mouse.isButtonDown(0); // 0 for left mouse button
    }
    

    @Override
    public float getTextureYOffset() {
        int row = currentState / atlasCols;
        return (float) row / atlasRows;
    }
}