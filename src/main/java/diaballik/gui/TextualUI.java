package diaballik.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import diaballik.ApplicationBuilder;
import diaballik.logic.Board;
import diaballik.logic.Game;
import diaballik.logic.Rulebook;
import diaballik.logic.board.Coordinate;
import diaballik.logic.board.IllegalMovementException;
import diaballik.logic.board.parser.CoordinateParser;
import diaballik.logic.board.parser.IncorrectInputException;
import diaballik.logic.rule.Rule;

public class TextualUI {
	private Game diaballik;
	
    public void initializeUI() {
        loadGame();
        try(Scanner inputStream = new Scanner(System.in)) {
	        while(diaballik.isGameRunning()) {
	        	run(inputStream);
	        }
    	}
    }
    
    private void loadGame() {
		System.out.println("Diaballik\n"); 
		diaballik = ApplicationBuilder.createGame();
	}
    
	private void run(Scanner inputStream) {
		displayActualState();
		String[] command = readPlayerCommand(inputStream);
		executeCommand(command);
		changePlayerIfNoPossibleMoves();
		endIfGameWon();
	}

	private void displayActualState() {
		System.out.println("Current player: " + diaballik.getCurrentPlayerName() + "\n");
		System.out.println(diaballik.getBoard());
	}
	
	private String[] readPlayerCommand(Scanner inputStream) {
		String[] command = inputStream.nextLine().split(" ");
		return command;
	}
    
	private void executeCommand(String[] command) {
		switch(command[0]) {
			case "quit" : {
				diaballik.endGame();
				System.out.println("Bye, bye!\n");
				break;
			}
			case "move" : {
				if(command.length > 1) {
					String[] coordinates = command[1].split("-");
					try {
						makeMove(coordinates);
						System.out.println("Move: " + command[1] + "\n");
					} catch (IllegalMovementException e) {
						System.err.println("Illegal movement!");
					} catch(ArrayIndexOutOfBoundsException | IncorrectInputException e) {
						System.err.println("Incorrect query!");
					}
				}
				else {
					System.out.println("Incorrect query!\n");
				}
				break;
			}
			case "next_turn" : {
				diaballik.changePlayer();
				break;
			}
			default : {
				System.out.println("Unknown command!\n");
			}
		}
	}

	private void makeMove(String[] coordinates) throws IncorrectInputException, IllegalMovementException {
		Coordinate from = CoordinateParser.parse(coordinates[0]);
		Coordinate to = CoordinateParser.parse(coordinates[1]);
		diaballik.executeMove(from, to);
	}
	
	private void changePlayerIfNoPossibleMoves() {
		if(!diaballik.checkIsAnyMovePossible()) {
			diaballik.changePlayer();		
		}
	}
	
	private void endIfGameWon() {
		if(diaballik.checkIsMatchEndingNow()){
			System.out.println(diaballik.getBoard());
			System.out.println(diaballik.getCurrentPlayerName() + " won game!");
			diaballik.endGame();
		}
	}
}
