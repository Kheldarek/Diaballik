package diaballik;

import diaballik.logic.Board;
import diaballik.logic.Game;
import diaballik.logic.Rulebook;

public class ApplicationBuilder {

	public static Game createGame() {
        return new Game(new Board(), new Rulebook());
	}

}
