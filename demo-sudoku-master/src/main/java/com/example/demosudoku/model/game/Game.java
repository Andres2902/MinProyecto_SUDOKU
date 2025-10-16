package com.example.demosudoku.model.game;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import java.util.List;

/**
 * Represents the concrete implementation of the Sudoku game logic.
 * This class is responsible for setting up the game board UI and handling user input.
 */
public class Game extends GameAbstract {
    /**
     * Constructs a new Game instance.
     *
     * @param boardGridpane The GridPane from the view where the Sudoku board will be rendered.
     */
    public Game(GridPane boardGridpane) {
        super(boardGridpane);
    }

    /**
     * Starts the game by generating a board, creating UI components (TextFields) for each cell,
     * and adding them to the GridPane. It also sets properties for each cell, such as editability.
     */
    @Override
    public void startGame() {
        for (int i = 0; i < board.getBoard().size(); i++) {
            for (int j = 0; j < board.getBoard().get(i).size(); j++) {
                int number = board.getBoard().get(i).get(j);
                System.out.print(number + " ");

                TextField textField = new TextField();
                textField.setAlignment(Pos.CENTER);
                textField.setText(String.valueOf(number));
                if (number != 0) {
                    textField.setEditable(false);
                    textField.setText(String.valueOf(number));
                    textField.setStyle(
                            "-fx-background-color: #d9e2ec;" +
                                    "-fx-border-color: #FACF0F;" +
                                    "-fx-border-width: 1;" +
                                    "-fx-text-fill: #142850;");
                } else{
                    textField.setText("");
                }
                handleNumberField(textField, i, j);
                boardGridpane.add(textField, j, i);
            }
            System.out.println();
        }
    }

    /**
     * Attaches a key released event handler to a TextField cell. When the key is released,
     * it validates the number entered by the user against the Sudoku rules.
     *
     * @param txt The TextField to which the handler will be attached.
     * @param row The row index of the cell in the board.
     * @param col The column index of the cell in the board.
     */
    private void handleNumberField(TextField txt, int row, int col) {
        txt.setOnKeyReleased(event -> {
            String input = txt.getText().trim();

            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                board.clearNumber(row, col);
                txt.setStyle("-fx-border-color: #00A8CC;");
                txt.clear();
                return;
            }

            // Aceptar solo números del 1 al 6
            if (input.matches("[1-6]")) {
                int value = Integer.parseInt(input);
                if (board.isValid(row, col, value)) {
                    board.setNumber(row, col, value);
                    txt.setStyle("-fx-border-color: #21891A; -fx-text-fill: #142850;");

                    if (board.isComplete()) {
                        System.out.println("¡Sudoku completado correctamente!");

                        com.example.demosudoku.utils.AlertBox alert = new com.example.demosudoku.utils.AlertBox();
                        alert.showAlert("¡Felicidades!", "Has completado correctamente el Sudoku.", javafx.scene.control.Alert.AlertType.INFORMATION);
                    }
                } else {
                    txt.setStyle("-fx-border-color: red; -fx-text-fill: red;");
                }
            } else {
                // Cualquier otra entrada se limpia
                txt.clear();
                board.clearNumber(row, col);
            }
        });


    }

    public String suggestNumber() {
        int[] empty = board.findEmptyCell();
        if (empty == null) {
            return "No hay celdas vacías disponibles.";
        }

        List<Integer> possibles = board.getPossibleNumbers(empty[0], empty[1]);
        if (possibles.isEmpty()) {
            return "No hay números válidos para la celda (" + (empty[0] + 1) + "," + (empty[1] + 1) + ").";
        }

        int suggestion = possibles.get(0);
        return "Sugerencia: puedes colocar el número " + suggestion +
                " en la celda (" + (empty[0] + 1) + "," + (empty[1] + 1) + ").";
    }

}
