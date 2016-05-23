package diaballik.gui;

import diaballik.ApplicationBuilder;
import diaballik.logic.Board;
import diaballik.logic.Game;
import diaballik.logic.board.Coordinate;
import diaballik.logic.board.Field;
import diaballik.logic.board.IllegalMovementException;
import diaballik.logic.board.parser.CoordinateParser;
import diaballik.logic.board.parser.IncorrectInputException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GraphicalUI extends Application {

    private static Game diaballik = ApplicationBuilder.createGame();
    private TextField coordinatesLeft = new TextField("a1");
    private TextField coordinatesRight = new TextField("a2");
    private TextField coordinatesText = new TextField("a1-a2");//not used/initialized
    private Label madeMoveLabel = new Label();
    private Label currentPlayerLabel = new Label();
    private Button moveButton = new Button();
    private Button nextTurnButton = new Button();
    private Button quitButton = new Button();//not used/initialized

    private String previousLeftField;
    private String previousRightField;

    private Parent createContent(Stage primaryStage){
        Pane root = new Pane();
        root.setPrefSize(850, 725);

        drawFields(root);

        addTextFieldCoordinatesLeft(root);
        addTextFieldCoordinatesRight(root);
        //addTextFieldCoordinates(root);
        addLabelMadeMove(root);
        addLabelCurrentPlayer(root);
        addButtonMove(root);
        addButtonNextTurn(root);
        //addButtonQuit(root);

        return root;
    }

    private class GameField extends StackPane {
        public Rectangle border;
        public Text fieldName;
        public GameField(){
            border = new Rectangle(100, 100);
            fieldName = new Text();

            border.setFill(Color.DIMGREY);
            border.setStroke(Color.BROWN);
            border.setStrokeWidth(3);

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, fieldName);

            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    coordinatesLeft.setText(fieldName.getText());
                    fieldName.setStyle("-fx-font-weight: bold");
                    border.setStrokeWidth(4);
                    //border.setFill(Color.BLUE);
                    if(previousLeftField != null && fieldName.getText() != previousLeftField){
                        GameField tempField = (GameField) getScene().lookup("#" + previousLeftField);
                        tempField.fieldName.setStyle("<font-weight>: regular");
                        tempField.border.setStrokeWidth(3);
                        //tempField.border.setFill(Color.BLUE);
                    }
                    previousLeftField = getId();
                }
                if (event.getButton() == MouseButton.SECONDARY) {
                    coordinatesRight.setText(fieldName.getText());
                    fieldName.setStyle("-fx-font-weight: bold");
                    border.setStrokeWidth(4);
                    if(previousRightField != null && fieldName.getText() != previousRightField){
                        //GameField tempField = new GameField();
                        //tempField.fieldName = (Text) getScene().lookup("#" + previousRightField);
                        GameField tempField = (GameField) getScene().lookup("#" + previousRightField);
                        tempField.fieldName.setStyle("<font-weight>: regular");
                        tempField.border.setStrokeWidth(3);
                    }
                    //previousRightField = fieldName.getId();
                    previousRightField = getId();
                }
            });
        }
    }

    private void addTextFieldCoordinatesLeft(Pane root){
//TextField coordinatesLeft = new TextField("a1");
        coordinatesLeft.setMaxSize(30, 20);
        coordinatesLeft.setLayoutX(745);
        coordinatesLeft.setLayoutY(50);
        root.getChildren().add(coordinatesLeft);
    }
    private void addTextFieldCoordinatesRight(Pane root){
//TextField coordinatesRight = new TextField("a2");
        coordinatesRight.setMaxSize(30, 20);
        coordinatesRight.setLayoutX(785);
        coordinatesRight.setLayoutY(50);
        root.getChildren().add(coordinatesRight);
    }
    private void addTextFieldCoordinates(Pane root){
//TextField coordinatesText = new TextField("a1-a2");
        coordinatesText.setMaxSize(100, 20);
        coordinatesText.setLayoutX(730);
        coordinatesText.setLayoutY(10);
        root.getChildren().add(coordinatesText);
    }
    private void addLabelMadeMove(Pane root){
//Label madeMoveLabel = new Label();
        madeMoveLabel.setText("");
        madeMoveLabel.setLayoutX(740);
        madeMoveLabel.setLayoutY(110);
        madeMoveLabel.setMaxWidth(100);
        madeMoveLabel.setWrapText(true);
        root.getChildren().add(madeMoveLabel);
    }
    private void addLabelCurrentPlayer(Pane root){
//Label currentPlayerLabel = new Label();
        currentPlayerLabel.setText("Current player: PLAYER_1");
        currentPlayerLabel.setLayoutX(740);
        currentPlayerLabel.setLayoutY(135);
        currentPlayerLabel.setMaxWidth(100);
        currentPlayerLabel.setWrapText(true);
        root.getChildren().add(currentPlayerLabel);
    }
    private void addButtonMove(Pane root){
//Button moveButton = new Button();
        moveButton.setText("Make a move");
        moveButton.setLayoutX(735);
        moveButton.setLayoutY(80);
        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(diaballik.isGameRunning()) {
	                String[] coordsLR = new String[2];
	                coordsLR[0] = coordinatesLeft.getText();
	                coordsLR[1] = coordinatesRight.getText();
	                //String[] coordinates = coordinatesText.getText().toString().split("-");
	                try {
	                    makeMove(coordsLR);
	                    //System.out.println("Move: " + coordinatesText.getText().toString() + "\n");
	                    madeMoveLabel.setText("Move: " + coordinatesLeft.getText().toString() + "-" + coordinatesRight.getText().toString());
	                    drawFields(root);
	                    changePlayerIfNoPossibleMoves();
	                    drawFields(root);
	                    endIfGameWon();
	                    currentPlayerLabel.setText("Current player: " + diaballik.getCurrentPlayerName());
	                } catch (IllegalMovementException e) {
	                    System.err.println("Illegal movement!");
	                    madeMoveLabel.setText("Illegal movement!");
	                } catch(ArrayIndexOutOfBoundsException | IncorrectInputException e) {
	                    System.err.println("Incorrect query!");
	                    madeMoveLabel.setText("Incorrect fields!");
	                }
            	}
            }
        });
        root.getChildren().add(moveButton);
    }
    private void addButtonNextTurn(Pane root){
//Button nextTurnButton = new Button();
        nextTurnButton.setText("End turn");
        nextTurnButton.setLayoutX(740);
        nextTurnButton.setLayoutY(175);
        nextTurnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(diaballik.isGameRunning()) {
	                diaballik.changePlayer();
	                drawFields(root);
	                currentPlayerLabel.setText("Current player: " + diaballik.getCurrentPlayerName());
            	}
            }
        });
        root.getChildren().add(nextTurnButton);
    }
    private void addButtonQuit(Pane root){
//Button quitButton = new Button();
        quitButton.setText("Quit game");
        quitButton.setLayoutX(740);
        quitButton.setLayoutY(300);
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                diaballik.endGame();

                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(((Node)event.getSource()).getScene().getWindow());
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("Bye, bye!"));
                Scene dialogScene = new Scene(dialogVbox, 100, 100);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        });
        root.getChildren().add(quitButton);
    }

    private void drawFields(Pane root){
        for(int i=0; i<7; i++){
            for(int j=0; j<7; j++){
                GameField field = new GameField();

                //Color-setting
                Board board = diaballik.getBoard();
                Coordinate position = new Coordinate(i, j);
                if(board.getField(position) == Field.EMPTY){
                    field.border.setFill(Color.ANTIQUEWHITE);
                }
                if(board.getField(position) == Field.PLAYER_1_BALL){
                    field.border.setFill(Color.DARKGREEN);
                }
                if(board.getField(position) == Field.PLAYER_1_PIECE){
                    field.border.setFill(Color.LIGHTGREEN);
                }
                if(board.getField(position) == Field.PLAYER_2_BALL){
                    field.border.setFill(Color.DARKSLATEBLUE);
                }
                if(board.getField(position) == Field.PLAYER_2_PIECE){
                    field.border.setFill(Color.CORNFLOWERBLUE);
                }

                //ID setting & field name displaying
                if (j==0) {
                    field.fieldName.setText("a" + Integer.toString(i+1));
                    //field.fieldName.setId(field.fieldName.getText());
                    field.setId(field.fieldName.getText());
                }
                if (j==1) {
                    field.fieldName.setText("b" + Integer.toString(i+1));
                    field.setId(field.fieldName.getText());
                }
                if (j==2) {
                    field.fieldName.setText("c" + Integer.toString(i+1));
                    field.setId(field.fieldName.getText());
                }
                if (j==3) {
                    field.fieldName.setText("d" + Integer.toString(i+1));
                    field.setId(field.fieldName.getText());
                }
                if (j==4) {
                    field.fieldName.setText("e" + Integer.toString(i+1));
                    field.setId(field.fieldName.getText());
                }
                if (j==5) {
                    field.fieldName.setText("f" + Integer.toString(i+1));
                    field.setId(field.fieldName.getText());
                }
                if (j==6) {
                    field.fieldName.setText("g" + Integer.toString(i+1));
                    field.setId(field.fieldName.getText());
                }

                int customPadding = 10;
                field.setTranslateX(j*100+customPadding);
                field.setTranslateY(i*100+customPadding);

                root.getChildren().add(field);
            }
        }
    }

    private void loadGame() {
        //System.out.println("Diaballik\n");
        diaballik = ApplicationBuilder.createGame();
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
            //System.out.println(diaballik.getBoard());
            System.out.println(diaballik.getCurrentPlayerName() + " won the game!");
            madeMoveLabel.setText(diaballik.getCurrentPlayerName() + " won the game!"); //to be changed to a pop-up
            diaballik.endGame();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent(primaryStage)));
        primaryStage.setTitle("Diaballik");
        primaryStage.setResizable(false);
        loadGame();
        primaryStage.show();
    }

    public static void initializeUI(){
        launch();
    }
}

