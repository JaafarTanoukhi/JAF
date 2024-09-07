import Compiler.Interface;
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
    private Interface inter = new Interface();

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
            if(xTurn){
                board[row][col].setText("X");
                fill(col + row * 3, "X");
            }
            else{
                board[row][col].setText("O");
                fill(col + row * 3, "O");
            }
            String winner = checkWin();
            if(winner != ""){
                showWinner(winner);
            }
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

    private String checkWin(){
        return (String) inter.getContext().find("GameState.winner");
    }

    private void fill(int rowNumber, String letter){
        System.out.println("play cell (rowNumber = " + rowNumber + ") fill"+ letter);
        inter.Query("play cell (rowNumber = " + rowNumber + ") fill"+ letter);
    }

    private void resetBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col].setText("");
            }
        }
        inter = new Interface();
        xTurn = true;
    }
}

