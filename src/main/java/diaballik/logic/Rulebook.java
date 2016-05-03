package diaballik.logic;

import java.util.ArrayList;
import java.util.List;

import diaballik.logic.board.Coordinate;
import diaballik.logic.board.Field;
import diaballik.logic.board.MovementType;
import diaballik.logic.rule.*;

public class Rulebook {
	private List<Rule> throwRules;
	private List<Rule> normalRules;
	
	public Rulebook() {
		throwRules = new ArrayList<>(2);
		throwRules.add(new BallThrowingPossibilityChecker());
		normalRules = new ArrayList<>(2);
		normalRules.add(new FieldsNeighbourhoodChecker());
	}
	
	public Rulebook(List<Rule> throwRules, List<Rule> normalRules) {
		this.throwRules = throwRules;
		this.normalRules = normalRules;
	}

	public MovementType getMovementType(Board board, Coordinate from, Coordinate to, PlayerType currentPlayer) {
		MovementType result = getMoveType(board, from, to, currentPlayer);
		MovementType rulesCheckingResult = MovementType.ILLEGAL_MOVEMENT;
		if(result.equals(MovementType.PIECE_MOVEMENT)) {
			rulesCheckingResult = checkRules(board, from, to, currentPlayer, normalRules);
		}
		if(result.equals(MovementType.BALL_THROW)) {
			rulesCheckingResult = checkRules(board, from, to, currentPlayer, throwRules);
		}
		if(rulesCheckingResult == null) {
			return result;
		}
		return rulesCheckingResult;
	}

	private MovementType getMoveType(Board board, Coordinate from, Coordinate to, PlayerType currentPlayer) {
		if(new NormalMoveTypeChecker().test(board, from, to, currentPlayer)) {
			return MovementType.PIECE_MOVEMENT;
		}
		if(new BallThrowingMovementTypeChecker().test(board, from, to, currentPlayer)) {
			return MovementType.BALL_THROW;
		}
		return MovementType.ILLEGAL_MOVEMENT;
	}
	
	private MovementType checkRules(Board board, Coordinate from, Coordinate to, PlayerType currentPlayer, List<Rule> rules) {
		for(Rule rule : rules) {
			if(!rule.test(board, from, to, currentPlayer)) {
				return MovementType.ILLEGAL_MOVEMENT;
			}
		}
		return null;
	}

	public boolean checkGameIsOver(PlayerType currentPlayer, Board board) {
		if(currentPlayer.equals(PlayerType.PLAYER_1)) {
			for(int i = 0; i < 7; i++) {
				if(Field.PLAYER_1_BALL.equals(board.getField(new Coordinate(6, i)))) {
					return true;
				}
			}
		}
		else {
			for(int i = 0; i < 7; i++) {
				if(Field.PLAYER_2_BALL.equals(board.getField(new Coordinate(0, i)))) {
					return true;
				}
			}
		}
		return false;
	}
}
