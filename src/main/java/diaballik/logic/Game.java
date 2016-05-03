package diaballik.logic;

import diaballik.logic.Board;
import diaballik.logic.board.Coordinate;
import diaballik.logic.board.IllegalMovementException;
import diaballik.logic.board.MovementType;

public class Game {
	private boolean gameRunningFlag = true;
	private int movementCounter = 0;
    private boolean thrownBall = false;
    private PlayerType currentPlayer = PlayerType.PLAYER_1;
    private Board board;
    private Rulebook rules;
    
    public Game(Board board, Rulebook rules) {
    	this.board = board;
    	this.rules = rules;
    }
    
    public String getCurrentPlayerName() {
		return currentPlayer.toString();
	}
    
    public Board getBoard() {
    	return board;
    }
    
    public boolean isGameRunning() {
    	return gameRunningFlag;
    }

    public void executeMove(Coordinate from, Coordinate to) throws IllegalMovementException {
    	 MovementType currentMove = rules.getMovementType(board, from, to, currentPlayer);
    	 if (MovementType.ILLEGAL_MOVEMENT.equals(currentMove)) {
    	 	throw new IllegalMovementException();
    	 }
    	 else if(MovementType.BALL_THROW.equals(currentMove) && !thrownBall) {
    	 	thrownBall = true;
    	 }
    	 else if(MovementType.PIECE_MOVEMENT.equals(currentMove) && movementCounter < 2) {
    	 	movementCounter++;
    	 }
    	execute(from, to);
	}
	
    private void execute(Coordinate from, Coordinate to) {
		board.actualize(from, to);
	}

	public boolean checkIsAnyMovePossible() {
    	return (movementCounter < 2 || !thrownBall);
    }

    public void changePlayer() {
		if(currentPlayer.equals(PlayerType.PLAYER_1)) {
			currentPlayer = PlayerType.PLAYER_2;
		}
		else {
			currentPlayer = PlayerType.PLAYER_1;
		}
		resetCounters();
	}

	private void resetCounters() {
		movementCounter = 0;
		thrownBall = false;
	}
	
    public boolean checkIsMatchEndingNow() {
    	return rules.checkGameIsOver(currentPlayer, board);
	}
	 
    public void endGame() {
		gameRunningFlag = false;
	}
}