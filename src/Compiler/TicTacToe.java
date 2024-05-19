package Compiler;

// import java.io.IOException;
// import java.util.ArrayList;

// import Compiler.Token.Token;
// import Compiler.Util.FileDependencyResolver;
// import Compiler.Util.FileDependencyResolver.FileContent;

// public class tester {

//     public static void main(String[] args) throws Exception {
//         runLexer();
//     }

//     private static void runLexer() throws IOException {
//         FileDependencyResolver resolver = new FileDependencyResolver();
//         ArrayList<FileContent> contents = resolver.resolveDependencies("JAF\\src\\TEST\\game.jaf");
//         for (FileContent content : contents) {
//             Lexer lexer = new Lexer(content);
//             lexer.tokenize();
//             ArrayList<Token> tokens = lexer.getTokens();
//             ArrayList<String> errors = lexer.getErrors();
//             System.out.println("\u001B[31m");
//             for (String error : errors) {
//                 System.out.println(error);
//             }
//             System.out.println("\u001B[0m");

//             for (Token token : tokens) {
//                 System.out.println(token);
//             }
//         }
//     }
// }

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TicTacToe extends Application {

    private Button[][] board;
    private boolean xTurn = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tic Tac Toe");

        board = new Button[3][3];
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button btn = new Button("");
                btn.setMinSize(100, 100);
                btn.setStyle("-fx-font-size:30px;-fx-font-weight:bold");
                final int r = row;
                final int c = col;
                btn.setOnAction(e -> handleButtonClick(r, c));
                board[row][col] = btn;
                grid.add(btn, col, row);
            }
        }

        VBox vbox = new VBox(grid);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleButtonClick(int row, int col) {
        if (board[row][col].getText().isEmpty()) {
            board[row][col].setText(xTurn ? "X" : "O");
            xTurn = !xTurn;
        }
    }

    private void showWinner(String winner) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Winner is: " + winner);
        alert.showAndWait();
        resetBoard();
    }

    private void resetBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col].setText("");
            }
        }
        xTurn = true;
    }
}
