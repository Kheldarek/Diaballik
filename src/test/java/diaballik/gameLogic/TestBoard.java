package diaballik.gameLogic;

import org.junit.Test;

import diaballik.gameLogic.Board;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class TestBoard {
	@Test
	public void isBoardCreatedCorrectly() {
		Board realBoard = init();
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				if(i == 0 && j == 3) {
					assertEquals(FieldContent.FIRST_BALL, realBoard.getField(new Coordinate(i,j)).getState());
				}
				else if(i == 0) {
					assertEquals(FieldContent.FIRST_PLAYER, realBoard.getField(new Coordinate(i,j)).getState());
				}
				else if(i == 6 && j == 3) {
					assertEquals(FieldContent.SECOND_BALL, realBoard.getField(new Coordinate(i,j)).getState());
				}
				else if(i == 6) {
					assertEquals(FieldContent.SECOND_PLAYER, realBoard.getField(new Coordinate(i,j)).getState());
				}
				else {
					assertEquals(FieldContent.EMPTY, realBoard.getField(new Coordinate(i,j)).getState());
				}
			}
		}
	}
	
	@Test
	public void isBoardActualizingCorrectly() {
		Board realBoard = init();
		realBoard.actualizeBoard(new Coordinate(2, 2), FieldContent.FIRST_PLAYER);
		assertEquals(FieldContent.FIRST_PLAYER, realBoard.getField(new Coordinate(2, 2)).getState());
		realBoard.actualizeBoard(new Coordinate(2, 2), FieldContent.SECOND_BALL);
		assertEquals(FieldContent.SECOND_BALL, realBoard.getField(new Coordinate(2, 2)).getState());
	}
	
	private Board init() {
		Field[][] board = new Field[7][7];
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				board[i][j] = new Field(new Coordinate(i, j),new ArrayList<Coordinate>(0), FieldContent.EMPTY);
			}
		}
		return new Board(board);
	}
}
