package diaballik;

import java.util.ArrayList;
import java.util.List;

import diaballik.logic.Board;
import diaballik.logic.Game;
import diaballik.logic.Rulebook;
import diaballik.logic.rule.Rule;

public class ApplicationBuilder {

	public static Game createGame() {
        return new Game(new Board(), new Rulebook());
	}

}
