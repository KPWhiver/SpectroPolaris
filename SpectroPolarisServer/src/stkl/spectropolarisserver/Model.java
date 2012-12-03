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
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		for(GameCharacter character : d_characters)
			character.draw(g2d);
	}
}
