package puzzles;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import puzzles.sudoku.SudokuGUI;

public class MainGUI extends Application {

    public enum GameState { EDITING, SOLVING, SOLVED, IMPOSSIBLE }
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        BorderPane bp = new BorderPane();
        bp.setTop(new Label("Choose which game to solve:"));
        Button sudoku = new Button("Sudoku");
        Button kakuro = new Button("Kakuro");
        sudoku.setOnAction(event -> new SudokuGUI(this.stage));
        FlowPane buttons = new FlowPane(sudoku, kakuro);
        buttons.setAlignment(Pos.CENTER);
        buttons.setHgap(5);
        bp.setCenter(buttons);
        Scene menu = new Scene(bp);
        stage.setScene(menu);
        stage.show();
    }
}
