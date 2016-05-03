package diaballik.logic.rule;

import diaballik.logic.Board;
import diaballik.logic.PlayerType;
import diaballik.logic.board.Coordinate;
import diaballik.logic.board.Field;

public class BallThrowingMovementTypeChecker implements Rule {

	@Override
	public boolean test(Board board, Coordinate from, Coordinate to, PlayerType currentPlayer) {
		if(PlayerType.PLAYER_1.equals(currentPlayer)) {
			if(Field.PLAYER_1_BALL.equals(board.getField(from)) && Field.PLAYER_1_PIECE.equals(board.getField(to))) {
				return true;
			}
			else if(Field.PLAYER_1_BALL.equals(board.getField(to)) && Field.PLAYER_1_PIECE.equals(board.getField(from))) {
				return true;
			}
		}
		else {
			if(Field.PLAYER_2_BALL.equals(board.getField(from)) && Field.PLAYER_2_PIECE.equals(board.getField(to))) {
				return true;
			}
			else if(Field.PLAYER_2_BALL.equals(board.getField(to)) && Field.PLAYER_2_PIECE.equals(board.getField(from))) {
				return true;
			}
		}
		return false;
	}

}
