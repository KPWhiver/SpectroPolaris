import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;


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
	
	public void draw(Graphics2D g2d) {
		
		for(GameCharacter character : d_characters)
			character.draw(g2d);
	}

	public void removeGameCharacter(GameCharacter character) {
		d_characters.remove(character);
	}
}
