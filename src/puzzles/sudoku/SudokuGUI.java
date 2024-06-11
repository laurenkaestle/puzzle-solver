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

    public SudokuGUI(Stage stage) {
        // make initial sudoku board and set stage
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

    public void center(BorderPane bp) {
        GridPane grid = new GridPane();
        // rows/cols to set in gp, where to put lines
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

    public void right(BorderPane bp) {
        // buttons to fill in grid
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

    public void bottom(BorderPane bp) {
        // buttons to reset or solve
        Button reset = new Button("Reset Puzzle");
        Button solve = new Button("Solve Puzzle");
        // set actions with functions from model
        reset.setOnAction(event -> this.model.reset());
        solve.setOnAction(event -> this.model.solve());
        FlowPane fp = new FlowPane(reset, solve);
        bp.setBottom(fp);
    }

    @Override
    public void update(SudokuModel model, String str) {
        BorderPane bp = new BorderPane();
        center(bp);
        bottom(bp);
        right(bp);
        // set top with "str" message label
        bp.setTop(new Label(str));
        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.show();
    }
}
