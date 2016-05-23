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
    TextField coordinatesLeft = new TextField("a1");
    TextField coordinatesRight = new TextField("a2");
    //TextField coordinatesText = new TextField("a1-a2");
    Label madeMoveLabel = new Label();
    Label currentPlayerLabel = new Label();
    Button moveButton = new Button();
    Button nextTurnButton = new Button();
    //Button quitButton = new Button();

    private Parent createContent(Stage primaryStage){
        Pane root = new Pane();
        root.setPrefSize(850, 750);

        //Text text = new Text(Integer.toString(i));
        //root.getChildren().add(text);

        drawFields(root);

        //TextField coordinatesLeft = new TextField("a1");
        coordinatesLeft.setMaxSize(30, 20);
        coordinatesLeft.setLayoutX(735);
        coordinatesLeft.setLayoutY(50);
        root.getChildren().add(coordinatesLeft);

        //TextField coordinatesRight = new TextField("a2");
        coordinatesRight.setMaxSize(30, 20);
        coordinatesRight.setLayoutX(775);
        coordinatesRight.setLayoutY(50);
        root.getChildren().add(coordinatesRight);

        /*TextField coordinatesText = new TextField("a1-a2");
        coordinatesText.setMaxSize(100, 20);
        coordinatesText.setLayoutX(725);
        coordinatesText.setLayoutY(10);
        root.getChildren().add(coordinatesText);*/

        //Label madeMoveLabel = new Label();
        madeMoveLabel.setText("");
        madeMoveLabel.setLayoutX(735);
        madeMoveLabel.setLayoutY(110);
        madeMoveLabel.setMaxWidth(100);
        madeMoveLabel.setWrapText(true);
        root.getChildren().add(madeMoveLabel);

        //Label currentPlayerLabel = new Label();
        currentPlayerLabel.setText("Current player: PLAYER_1");
        currentPlayerLabel.setLayoutX(735);
        currentPlayerLabel.setLayoutY(135);
        currentPlayerLabel.setMaxWidth(100);
        currentPlayerLabel.setWrapText(true);
        root.getChildren().add(currentPlayerLabel);

        //Button moveButton = new Button();
        moveButton.setText("Make a move");
        moveButton.setLayoutX(730);
        moveButton.setLayoutY(80);
        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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
        });
        root.getChildren().add(moveButton);

        //Button nextTurnButton = new Button();
        nextTurnButton.setText("End turn");
        nextTurnButton.setLayoutX(735);
        nextTurnButton.setLayoutY(175);
        nextTurnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                diaballik.changePlayer();
                drawFields(root);
                currentPlayerLabel.setText("Current player: " + diaballik.getCurrentPlayerName());
            }
        });
        root.getChildren().add(nextTurnButton);

        /*Button quitButton = new Button();
        quitButton.setText("Quit game");
        quitButton.setLayoutX(735);
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
        root.getChildren().add(quitButton);*/

        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent(primaryStage)));
        primaryStage.setTitle("Diaballik");
        TextualUI textualUI = new TextualUI();
        textualUI.loadGame();
        primaryStage.show();
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
                }
                if (event.getButton() == MouseButton.SECONDARY) {
                    coordinatesRight.setText(fieldName.getText());
                    fieldName.setStyle("-fx-font-weight: bold");
                }
            });
        }
    }

    private void drawFields(Pane root){
        for(int i=0; i<7; i++){
            for(int j=0; j<7; j++){
                GameField field = new GameField();

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
                if (j==0) field.fieldName.setText("a" + Integer.toString(i+1));
                if (j==1) field.fieldName.setText("b" + Integer.toString(i+1));
                if (j==2) field.fieldName.setText("c" + Integer.toString(i+1));
                if (j==3) field.fieldName.setText("d" + Integer.toString(i+1));
                if (j==4) field.fieldName.setText("e" + Integer.toString(i+1));
                if (j==5) field.fieldName.setText("f" + Integer.toString(i+1));
                if (j==6) field.fieldName.setText("g" + Integer.toString(i+1));

                field.setTranslateX(j*100);
                field.setTranslateY(i*100);

                root.getChildren().add(field);
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
            //System.out.println(diaballik.getBoard());
            System.out.println(diaballik.getCurrentPlayerName() + " won game!");
            madeMoveLabel.setText(diaballik.getCurrentPlayerName() + " won game!"); //to be changed to a pop-up
            diaballik.endGame();
        }
    }

    public void initializeUI(){
        launch();
    }
}

