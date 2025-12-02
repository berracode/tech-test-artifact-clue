package co.com.bancolombia.galatea;

public class Matrix {

    private final String[][] data;
    private final int rows;
    private final int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new String[rows][cols];
    }

    public void set(String item, int row, int col) {

        validateBounds(row, col);

        if (item != null && item.length() == 1) {
            data[row][col] = item;
        } else {
            data[row][col] = "_";
        }
    }

    public String get(int row, int col) {
        validateBounds(row, col);
        return data[row][col];
    }

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String item = data[i][j] == null ? "*" : data[i][j];
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }

    private void validateBounds(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Indice fuera del rango");
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public String[][] getData() {
        return data;
    }
}
