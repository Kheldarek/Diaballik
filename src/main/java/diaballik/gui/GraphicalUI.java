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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private TextField AITextField = new TextField("Depth(1-2)");
    private Label madeMoveLabel = new Label();
    private Label currentPlayerLabel = new Label();
    private Button moveButton = new Button();
    private Button nextTurnButton = new Button();
    private Button quitButton = new Button();//not used/initialized
    private Button AIButton = new Button();

    private Image bluePlayerImg = new Image("file:playerBlue.png");
    private Image bluePlayerBallImg = new Image("file:playerBlueBall.png");
    private Image greenPlayerImg = new Image("file:playerGreen.png");
    private Image greenPlayerBallImg = new Image("file:playerGreenBall.png");
    //private ImageView bluePlayer = new ImageView(bluePlayerImg);
    private ImageView bluePlayerBall = new ImageView(bluePlayerBallImg);
    //private ImageView greenPlayer = new ImageView(greenPlayerImg);
    private ImageView greenPlayerBall = new ImageView(greenPlayerBallImg);
    private ImageView[] bluePlayers = new ImageView[7];
    private ImageView[] greenPlayers = new ImageView[7];

    private String previousLeftField;
    private String previousRightField;

    private Parent createContent(Stage primaryStage){
        Pane root = new Pane();
        root.setPrefSize(850, 725);

        drawGUI(root);
        return root;
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
    private void addTextFieldPioter(Pane root){
//TextField AITextField = new TextField();
        AITextField.setMaxSize(80, 20);
        AITextField.setLayoutX(740);
        AITextField.setLayoutY(300);
        root.getChildren().add(AITextField);
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
	                    changePlayerIfNoPossibleMoves();
	                    drawGUI(root);
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
	                drawGUI(root);
	                currentPlayerLabel.setText("Current player: " + diaballik.getCurrentPlayerName());
                    previousLeftField = null;
                    previousRightField = null;
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
    private void addButtonPioter(Pane root){
//Button AIButton = new Button();
        AIButton.setText("CLICK MEEE");
        AIButton.setLayoutX(740);
        AIButton.setLayoutY(330);
        AIButton.setOnAction(event -> {
            diaballik.aiPlayer.depth = Integer.parseInt(AITextField.getText());
		});
        root.getChildren().add(AIButton);
    }

    private class GameField extends StackPane {
        public Rectangle fieldPatch;
        public Text fieldName;
        public GameField(){
            fieldPatch = new Rectangle(100, 100);
            fieldName = new Text();

            fieldPatch.setFill(Color.GAINSBORO);
            fieldPatch.setStroke(Color.BLACK);
            fieldPatch.setStrokeWidth(3);

            setAlignment(Pos.CENTER);
            getChildren().addAll(fieldPatch, fieldName);

            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    /* set 'selected' status */
                    coordinatesLeft.setText(fieldName.getText());
                    fieldName.setStyle("-fx-font-weight: bold");
                    //fieldPatch.setStrokeWidth(4);
                    fieldPatch.setFill(Color.DARKGREY);

                    /* set 'clear' status if applies */
                    if(previousLeftField != null && fieldName.getText() != previousLeftField){
                        GameField tempField = (GameField) getScene().lookup("#" + previousLeftField);
                        tempField.fieldName.setStyle("<font-weight>: regular");
                        //tempField.fieldPatch.setStrokeWidth(3);
                        tempField.fieldPatch.setFill(Color.GAINSBORO);
                    }
                    previousLeftField = getId();
                }
                if (event.getButton() == MouseButton.SECONDARY) {
                    coordinatesRight.setText(fieldName.getText());
                    fieldName.setStyle("-fx-font-weight: bold");
                    //fieldPatch.setStrokeWidth(4);
                    fieldPatch.setFill(Color.DARKGREY);
                    if(previousRightField != null && fieldName.getText() != previousRightField){
                        //GameField tempField = new GameField();
                        //tempField.fieldName = (Text) getScene().lookup("#" + previousRightField);
                        GameField tempField = (GameField) getScene().lookup("#" + previousRightField);
                        tempField.fieldName.setStyle("<font-weight>: regular");
                        //tempField.fieldPatch.setStrokeWidth(3);
                        tempField.fieldPatch.setFill(Color.GAINSBORO);
                    }
                    //previousRightField = fieldName.getId();
                    previousRightField = getId();
                }
            });
        }
    }
    private void initTeams(){
        for (int i=0; i<7; i++){
            bluePlayers[i] = new ImageView(bluePlayerImg);
            greenPlayers[i] = new ImageView(greenPlayerImg);

            bluePlayers[i].setFitHeight(80);
            bluePlayers[i].setPreserveRatio(true);
            bluePlayers[i].setSmooth(true);
            bluePlayers[i].setCache(true);

            greenPlayers[i].setFitHeight(80);
            greenPlayers[i].setPreserveRatio(true);
            greenPlayers[i].setSmooth(true);
            greenPlayers[i].setCache(true);
        }
        bluePlayerBall.setFitHeight(80);
        bluePlayerBall.setPreserveRatio(true);
        bluePlayerBall.setSmooth(true);
        bluePlayerBall.setCache(true);

        greenPlayerBall.setFitHeight(80);
        greenPlayerBall.setPreserveRatio(true);
        greenPlayerBall.setSmooth(true);
        greenPlayerBall.setCache(true);
        diaballik.gui = this;
    }
    private void drawGUI(Pane root){
        root.getChildren().clear();

        for(int i=0; i<7; i++){
            for(int j=0; j<7; j++){
                GameField field = new GameField();

                initTeams();

                //Color-setting, player-adding
                Board board = diaballik.getBoard();
                Coordinate position = new Coordinate(i, j);
                if(board.getField(position) == Field.EMPTY){
                    //field.fieldPatch.setFill(Color.ANTIQUEWHITE);
                }
                if(board.getField(position) == Field.PLAYER_1_BALL){
                    //field.fieldPatch.setFill(Color.DARKGREEN);
                    field.getChildren().add(greenPlayerBall);
                }
                if(board.getField(position) == Field.PLAYER_1_PIECE){
                    //field.fieldPatch.setFill(Color.LIGHTGREEN);
                    field.getChildren().add(greenPlayers[i]);
                }
                if(board.getField(position) == Field.PLAYER_2_BALL){
                    //field.fieldPatch.setFill(Color.DARKSLATEBLUE);
                    field.getChildren().add(bluePlayerBall);
                }
                if(board.getField(position) == Field.PLAYER_2_PIECE){
                    //field.fieldPatch.setFill(Color.CORNFLOWERBLUE);
                    field.getChildren().add(bluePlayers[i]);
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

        addTextFieldCoordinatesLeft(root);
        addTextFieldCoordinatesRight(root);
        addTextFieldPioter(root);
        addLabelMadeMove(root);
        addLabelCurrentPlayer(root);
        addButtonMove(root);
        addButtonNextTurn(root);
        addButtonPioter(root);
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
   public void endIfGameWon() {
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

