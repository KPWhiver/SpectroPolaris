package stkl.spectropolarisclient;

import java.util.ArrayList;

import android.graphics.Canvas;

public class Model {
	private ArrayList<GameCharacter> d_characters;
	
	public Model() {
		d_characters = new ArrayList<GameCharacter>();
	}
	
	public void addGameCharacter(GameCharacter character) {
		d_characters.add(character);
	}
	
	public void step() {
		for(GameCharacter character : d_characters)
			character.step();
	}
	
	public void draw(Canvas canvas) {
		for(GameCharacter character : d_characters)
			character.draw(canvas);
	}
}
