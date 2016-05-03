package diaballik.logic.rule;

import java.util.function.Function;

import diaballik.logic.Board;
import diaballik.logic.PlayerType;
import diaballik.logic.board.Coordinate;
import diaballik.logic.board.Field;

public class BallThrowingPossibilityChecker implements Rule {

	@Override
	public boolean test(Board board, Coordinate from, Coordinate to, PlayerType currentPlayer) {
		if(from.equals(to)) {
			return false;
		}
		if(from.getColumn() == to.getColumn()) {
			return checkIfSameColumn(board, from, to, currentPlayer);
		}
		if(from.getRow() == to.getRow()) {
			return checkIfSameRow(board, from, to, currentPlayer);
		}
		return checkDiagonals(board, from, to, currentPlayer);
	}

	private boolean checkIfSameColumn(Board board, Coordinate from, Coordinate to, PlayerType currentPlayer) {
		if(from.getRow() > to.getRow()) {
			return checkPosibillity(board, from, to, currentPlayer, (temp) -> {return temp.changeRow(-1);});
		}
		else {
			return checkPosibillity(board, from, to, currentPlayer, (temp) -> {return temp.changeRow(1);});
		}
	}

	private boolean checkIfSameRow(Board board, Coordinate from, Coordinate to, PlayerType currentPlayer) {
		if(from.getColumn() > to.getColumn()) {
			return checkPosibillity(board, from, to, currentPlayer, (temp) -> {return temp.changeColumn(-1);});
		}
		else {
			return checkPosibillity(board, from, to, currentPlayer, (temp) -> {return temp.changeColumn(1);});
		}
	}
	
	private boolean checkDiagonals(Board board, Coordinate from, Coordinate to, PlayerType currentPlayer) {
		if(!from.isOnDiagonal(to)) {
			return false;
		}
		if(from.getRow() > to.getRow() && from.getColumn() > to.getColumn()) {
			return checkPosibillity(board, from, to, currentPlayer, (temp) -> {return temp.changeRow(-1).changeColumn(-1);});
		}
		if(from.getRow() > to.getRow() && from.getColumn() < to.getColumn()) {
			return checkPosibillity(board, from, to, currentPlayer, (temp) -> {return temp.changeRow(-1).changeColumn(1);});
		}
		if(from.getRow() < to.getRow() && from.getColumn() > to.getColumn()) {
			return checkPosibillity(board, from, to, currentPlayer, (temp) -> {return temp.changeRow(1).changeColumn(-1);});
		}
		if(from.getRow() < to.getRow() && from.getColumn() < to.getColumn()) {
			return checkPosibillity(board, from, to, currentPlayer, (temp) -> {return temp.changeRow(1).changeColumn(1);});
		}
		return false;
	}

	private boolean checkPosibillity(Board board, Coordinate from, Coordinate to, PlayerType currentPlayer, Function<Coordinate, Coordinate> incrementer) {
		Coordinate temp = from;
		while(!temp.equals(to)) {
			if(PlayerType.PLAYER_1.equals(currentPlayer)) {
				if(Field.PLAYER_2_PIECE.equals((board.getField(temp))) || Field.PLAYER_2_BALL.equals((board.getField(temp)))) {
					return false;
				}
			}
			else {
				if(Field.PLAYER_1_PIECE.equals((board.getField(temp))) || Field.PLAYER_1_BALL.equals((board.getField(temp)))) {
					return false;
				}
			}
			temp = incrementer.apply(temp);
		}
		return true;
	}
}
