package diaballik.logic.rule;

import diaballik.logic.Board;
import diaballik.logic.PlayerType;
import diaballik.logic.board.Coordinate;

public interface Rule {
	boolean test(Board board, Coordinate from, Coordinate to, PlayerType currentPlayer);
}
