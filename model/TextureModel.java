package model;

import texture.ModelTexture;

public class TextureModel {

	private RawModel rawM;
	private ModelTexture mText;
	
	public TextureModel(RawModel model, ModelTexture texture) {
		this.rawM = model;
		this.mText = texture;
	}

	public RawModel getRawModel() {
		return rawM;
	}
	
	public ModelTexture getModelTexture() {
		return mText;
	}
}
