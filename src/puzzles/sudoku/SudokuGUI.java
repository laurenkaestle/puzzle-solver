package puzzles.sudoku;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import puzzles.Observer;

public class SudokuGUI implements Observer<SudokuModel, String> {
    private final Stage stage;
    private static int selectedRow = 0;
    private static int selectedCol = 0;
    private final SudokuModel model;

    /**
     * creates the initial board and sets the stage
     * @param stage     displays board, necessary buttons
     */
    public SudokuGUI(Stage stage) {
        this.stage = stage;
        this.model = new SudokuModel();
        this.model.addObserver(this);
        BorderPane bp = new BorderPane();
        center(bp);
        bottom(bp);
        right(bp);
        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * fills in the center of the stage with a grid pane representing
     * the main sudoku grid with squares corresponding to values in
     * the model
     * @param bp    the border pane for the scene that will be displayed
     */
    public void center(BorderPane bp) {
        GridPane grid = new GridPane();
        int row = 0, col = 0, rowLine = 0, colLine = 0;
        for(int i = 0; i < 9; i++) {
            if(rowLine == 3) {
                grid.add(new Separator(Orientation.VERTICAL), row, col);
                rowLine = 0;
                row++;
            }
            for(int j = 0; j < 9; j++) {
                Button b = new Button();
                if(model.getVal(i, j) > 0) {
                    b.setText(Integer.toString(model.getVal(i, j)));
                }
                final int r = i, c = j;
                b.setOnAction(event -> {
                    selectedRow = r;
                    selectedCol = c;
                });
                grid.add(b, row, col);
                col++;
                colLine++;
                if(colLine == 3) {
                    grid.add(new Separator(Orientation.HORIZONTAL), row, col);
                    colLine = 0;
                    col++;
                }
            }
            row++;
            rowLine++;
            col = 0;
        }
        bp.setCenter(grid);
    }

    /**
     * fills in the right side of the border pane with the buttons used to fill
     * the grid with the numbers 1 through 9
     * @param bp    the border pane for the scene that will be displayed
     */
    public void right(BorderPane bp) {
        GridPane grid = new GridPane();
        int num = 1;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                Button b = new Button(Integer.toString(num));
                final int n = num;
                b.setOnAction(event -> model.setVal(selectedRow, selectedCol, n));
                grid.add(b, j, i);
                num++;
            }
        }
        bp.setRight(grid);
    }

    /**
     * fills in the bottom of the border pane with the buttons used to solve or
     * reset the puzzle
     * @param bp    the border pane for the scene that will be displayed
     */
    public void bottom(BorderPane bp) {
        Button reset = new Button("Reset Puzzle");
        Button solve = new Button("Solve Puzzle");
        reset.setOnAction(event -> this.model.reset());
        solve.setOnAction(event -> this.model.solve());
        FlowPane fp = new FlowPane(reset, solve);
        bp.setBottom(fp);
    }

    /**
     * displays a scene with the updated grid and a message on the
     * top of the screen about the action that triggered this update
     * @param model     the current state of the model
     * @param str       a message to display on the screen
     */
    @Override
    public void update(SudokuModel model, String str) {
        BorderPane bp = new BorderPane();
        center(bp);
        bottom(bp);
        right(bp);
        bp.setTop(new Label(str));
        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.show();
    }
}
