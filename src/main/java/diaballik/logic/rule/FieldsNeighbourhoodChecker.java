package diaballik.logic.rule;

import diaballik.logic.Board;
import diaballik.logic.PlayerType;
import diaballik.logic.board.Coordinate;

public class FieldsNeighbourhoodChecker implements Rule {

	@Override
	public boolean test(Board board, Coordinate from, Coordinate to, PlayerType currentPlayer) {
		return from.isNeighbourOf(to);
	}
}
