//import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//import javafx;
//The order is random order due to multiple times of editing
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import javafx.geometry.HPos;
import javafx.geometry.Pos;
//import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Homework 08.
 * Jordle Game Design.
 * @author Xi Chen
 * @version 1.0
 */
public class Jordle extends Application {
    private ArrayList<String> wordList = new ArrayList<>();
    private Text instructionOnPanel = new Text();
    private String answerMessage;
    private int attempts = 0;
    private int bound = 4;

    /**
     * main() method.
     * @param args representing arguments that are running
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Random rand = new Random();
        int pos = rand.nextInt(Words.list.size());
        answerMessage = Words.list.get(pos).trim().toUpperCase();
        System.out.println("The correct word is: " + answerMessage);
        primaryStage.setTitle("Play Jordle");
        Text jordle = new Text("Jordle");
        instructionOnPanel.setText("\tGuess a word!");
        jordle.setStyle("-fx-font: 36 arial;");
        VBox root = new VBox();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(8);
        gridPane.setVgap(8);
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                Rectangle rec = new Rectangle();
                rec.setWidth(30);
                rec.setHeight(30);
                rec.setFill(Color.WHITE);
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);
                gridPane.getChildren().addAll(rec);
            }
        }
        HBox hbox = new HBox();
        Button instructions = new Button("Instructions");
        Button restart = new Button("Restart:<");

        hbox.setTranslateY(80);
        hbox.setSpacing(25);
        hbox.getChildren().addAll(instructionOnPanel, restart, instructions);
        gridPane.setTranslateX(60);
        gridPane.setTranslateY(25);
        root.getChildren().addAll(jordle, gridPane, hbox);
        root.setAlignment(Pos.CENTER);
        instructions.setOnAction(e -> {
            Stage stage = new Stage();
            VBox vbox = new VBox();
            vbox.getChildren().addAll(new Text("Welcome to Jordle!\nThis is a game of word.\n"
                    + "\n"
                    + "You will have six tries to guess a 5-letter word.\n"
                    + "\n"
                    + "For each guess you make, the box behind the letter will appear in 3 colors.\n"
                    + "Green: correct letter & correct position.\n"
                    + "Yellow: correct letter but WRONG position.\n"
                    + "Grey: wrong letter & wrong position.\n"
                    + "\n"
                    + "You are SMART! "
                    + "Just do it!"));
            stage.setScene(new Scene(vbox, 400, 400));
            stage.setTitle("Instruction");
            stage.show();
        });

        restart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                bound = 4;
                for (int index = wordList.size() - 1; index >= 0; index--) {
                    int rowindex = index / 5;
                    int columnindex = index % 5;
                    ObservableList<Node> childrens = gridPane.getChildren();
                    for (Node char1 : childrens) {
                        if (char1 instanceof Text && GridPane.getRowIndex(char1) == rowindex
                            && GridPane.getColumnIndex(char1) == columnindex) {
                            Text text = (Text) char1;
                            gridPane.getChildren().remove(text);
                            break;
                        }
                    }
                    for (Node char2 : childrens) {
                        if (char2 instanceof Rectangle && GridPane.getRowIndex(char2) == rowindex
                                && GridPane.getColumnIndex(char2) == columnindex) {
                            Rectangle rec = (Rectangle) char2;
                            rec.setFill(Color.WHITE);
                            break;
                        }
                    }
                    wordList.remove(index);
                }
                instructionOnPanel.setText("Guess a word");
                int pos = rand.nextInt(Words.list.size());
                answerMessage = Words.list.get(pos).trim().toUpperCase();
                System.out.println("The correct word is: " + answerMessage);
            }
        });
        Scene scene = new Scene(root, 300, 450);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            String input = key.getCode().toString();
            Pattern p = Pattern.compile("[A-Z]");
            Matcher m = p.matcher(input);
            boolean b = m.matches();
            if (b || input.equals("ENTER") || input.equals("BACK_SPACE")) {
                System.out.println(key.getCode());
                if (key.getCode() == KeyCode.ENTER) {
                    if (wordList.size() == bound + 1) {
                        int numberOfCorrect = 0;
                        bound += 5;
                        for (int i = wordList.size() - 5; i <  wordList.size(); i++) {
                            if (answerMessage.toUpperCase().contains(wordList.get(i))) {
                                int rowindex = i / 5;
                                int columnindex = i % 5;
                                ObservableList<Node> childrens = gridPane.getChildren();
                                for (Node char3 : childrens) {
                                    if (char3 instanceof Rectangle && GridPane.getRowIndex(char3) == rowindex
                                            && GridPane.getColumnIndex(char3) == columnindex) {
                                        Rectangle rec = (Rectangle) char3;
                                        rec.setFill(Color.YELLOW);
                                        break;
                                    }
                                }
                            } else {
                                int rowindex = i / 5;
                                int columnindex = i % 5;
                                ObservableList<Node> childrens = gridPane.getChildren();
                                for (Node char4 : childrens) {
                                    if (char4 instanceof Rectangle && GridPane.getRowIndex(char4) == rowindex
                                        && GridPane.getColumnIndex(char4) == columnindex) {
                                        Rectangle rec = (Rectangle) char4;
                                        rec.setFill(Color.GRAY);
                                        break;
                                    }
                                }
                            }
                            if (wordList.get(i).equals(answerMessage.toUpperCase().substring(i % 5, i % 5 + 1))) {
                                int rowindex = i / 5;
                                int columnindex = i % 5;
                                ObservableList<Node> childrens = gridPane.getChildren();
                                for (Node char5 : childrens) {
                                    if (char5 instanceof Rectangle && GridPane.getRowIndex(char5) == rowindex
                                            && GridPane.getColumnIndex(char5) == columnindex) {
                                        Rectangle rec = (Rectangle) char5; // use what you want to remove
                                        rec.setFill(Color.GREEN);
                                        break;
                                    }
                                }
                                numberOfCorrect++;
                            }
                        }
                        attempts++;
                        if (numberOfCorrect == 5) {
                            instructionOnPanel.setText("Congrats! You did it!");
                        } else {
                            if (attempts > 5) {
                                instructionOnPanel.setText("Sorry, the correct word is " + answerMessage);
                                Alert stop = new Alert(AlertType.INFORMATION, "Game over! Good tries!");
                                stop.show();
                            }
                        }
                    } else {
                        Alert alert = new Alert(AlertType.WARNING, "You must input five characters");
                        alert.show();
                    }
                } else if (key.getCode() == KeyCode.BACK_SPACE && wordList.size() > bound - 4) {
                    int index = wordList.size() - 1;
                    int rowindex = index / 5;
                    int columnindex = index % 5;
                    ObservableList<Node> childrens = gridPane.getChildren();
                    for (Node char6 : childrens) {
                        if (char6 instanceof Text && GridPane.getRowIndex(char6) == rowindex
                                && GridPane.getColumnIndex(char6) == columnindex) {
                            Text text = (Text) char6;
                            gridPane.getChildren().remove(text);
                            break;
                        }
                    }
                    wordList.remove(index);
                } else {
                    key.getCode();
                    if (key.getCode() != KeyCode.BACK_SPACE && wordList.size() <= bound) {
                        wordList.add(key.getCode().toString());
                        int index = wordList.size() - 1;
                        int rowindex = index / 5;
                        int columnindex = index % 5;
                        Text newText = new Text(wordList.get(index));
                        GridPane.setRowIndex(newText, rowindex);
                        GridPane.setColumnIndex(newText, columnindex);
                        gridPane.getChildren().addAll(newText);
                    }
                }
            }
        });
        primaryStage.setScene(scene);
        //addBouncyBall(scene);
        primaryStage.show();
    }

//    private void addBouncyBall(Scene scene) {
//        final Circle ball = new Circle(100, 100, 20);
//
//        RadialGradient gradient1 = new RadialGradient(0,
//            .1,
//            100,
//            100,
//            20,
//            false,
//            CycleMethod.NO_CYCLE,
//            new Stop(0, Color.RED),
//            new Stop(1, Color.BLACK));
//
//        ball.setFill(gradient1);
//
//        final Group root = (Group) scene.getRoot();
//        root.getChildren().add(ball);
//    }
}
