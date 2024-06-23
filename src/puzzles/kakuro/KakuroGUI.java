package puzzles.kakuro;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import puzzles.Observer;

public class KakuroGUI implements Observer<KakuroModel, String> {
    private final Stage stage;
    private int rows = 0;
    private int cols = 0;
    private KakuroModel model;
    public KakuroGUI(Stage stage) {
        this.stage = stage;
        BorderPane bp = new BorderPane();
        changeDimensions(bp);
        this.stage.setScene(new Scene(bp));
        this.stage.show();
    }

    public void changeDimensions(BorderPane bp) {
        bp.setTop(new Label("Select the dimensions for the puzzle."));
        // will offer 3x3 to 10x10 square puzzles FOR NOW, can edit this later
        GridPane grid = new GridPane();
        int number = 3;
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 4; j++) {
                Button b = new Button(Integer.toString(number));
                int current = number;
                b.setOnAction(event -> {
                    this.rows = current;
                    this.cols = current;
                });
                grid.add(b, j, i);
                number++;
            }
        }
        bp.setCenter(grid);
        Button submit = new Button("Submit Dimensions");
        submit.setOnAction(event -> {
            if(this.rows > 0 && this.cols > 0) {
                this.model = new KakuroModel(rows, cols);
            }
        });
        bp.setBottom(submit);
    }

    @Override
    public void update(KakuroModel model, String str) {}
}
