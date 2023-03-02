package game.display;

import game.display.Sprite;
import game.display.Spritesheet;

public class Font {
	public int charWidth, charHeight;
	protected Sprite[] characterSprites;
	
	public Font(Spritesheet sheet, int charWidth, int charHeight) {
		this.charWidth = charWidth;
		this.charHeight = charHeight;
		characterSprites = new Sprite[60];
		for (int row = 0; row < sheet.h / charHeight; ++row)
			for (int col = 0; col < sheet.w / charWidth; ++col)
				characterSprites[col + row * (sheet.w / charWidth)] = new Sprite(col * charWidth, row * charHeight, charWidth, charHeight, sheet);
	}
	
	public Sprite getSprite(char c) {
		return characterSprites[(int)c - 32];
	}
    
    public Sprite getSprite(String s, int gap) {
        Sprite result = new Sprite((charWidth + gap) * s.length(), charHeight);
        for (int i = 0; i < s.length(); ++i) {
            Sprite charSprite = getSprite(s.charAt(i));
            for (int row = 0; row < charHeight; ++row) {
                for (int col = 0; col < charWidth; ++col) {
                    result.pixels[col + i * (charWidth + gap) + row * result.w] = charSprite.pixels[col + row * charSprite.w];
                }
            }
        }
        return result;
    }
}
