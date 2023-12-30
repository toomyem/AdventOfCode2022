import java.io.File;
import java.util.*;

record Pos(int row, int col) {}
class Board extends HashMap<Pos, Integer> {}

public class Main {
    public static void main(String[] args) throws Exception {
        new Main().run(args.length > 0 ? args[0] : "input.txt");
    }

    void run(String file) throws Exception {
        try (Scanner in = new Scanner(new File(file))) {

            Board board = new Board();
            int row = 0;
            int col = 0;
            while (in.hasNext()) {
                String line = in.nextLine();
                if (line.isBlank()) break;
                for (col = 0; col < line.length(); col++) {
                    char ch = line.charAt(col);
                    board.put(new Pos(row, col), ch - '0');
                }
                row += 1;
            }

            int sum1 = calc1(board, row, col);
            System.out.println("Part 1: " + sum1);

            int sum2 = calc2(board, row, col);
            System.out.println("Part 2: " + sum2);
        }
    }

    private int calc1(Board board, int rows, int cols) {
        Set<Pos> visible = new HashSet<>();

        for (int col = 0; col < cols; col++) {
            int maxw = -1;
            for (int row = 0; row < rows; row++) {
                Pos pos = new Pos(row, col);
                int w = board.get(pos);
                if (w > maxw) {
                    visible.add(pos);
                    maxw = w;
                }
            }
        }

        for (int col = 0; col < cols; col++) {
            int maxw = -1;
            for (int row = rows - 1; row >= 0; row--) {
                Pos pos = new Pos(row, col);
                int w = board.get(pos);
                if (w > maxw) {
                    visible.add(pos);
                    maxw = w;
                }
            }
        }

        for (int row = 0; row < rows; row++) {
            int maxw = -1;
            for (int col = 0; col < cols; col++) {
                Pos pos = new Pos(row, col);
                int w = board.get(pos);
                if (w > maxw) {
                    visible.add(pos);
                    maxw = w;
                }
            }
        }

        for (int row = 0; row < rows; row++) {
            int maxw = -1;
            for (int col = cols - 1; col >= 0; col--) {
                Pos pos = new Pos(row, col);
                int w = board.get(pos);
                if (w > maxw) {
                    visible.add(pos);
                    maxw = w;
                }
            }
        }

        return visible.size();
    }

    private int calc2(Board board, int rows, int cols) {
        int max = 0;
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                Pos pos = new Pos(row, col);
                int right = see(board, pos, 1, 0);
                int left = see(board, pos, -1, 0);
                int down = see(board, pos, 0, 1);
                int up = see(board, pos, 0, -1);
                int mul = right * left * down * up;
                if (mul > max) {
                    max = mul;
                }
            }
        }

        return max;
    }

    private int see(Board board, Pos pos, int cx, int rx) {
        int h = board.get(pos);
        int i = 0;

        while (true) {
            pos = new Pos(pos.row() + rx, pos.col() + cx);
            Integer h2 = board.get(pos);
            if (h2 == null) break;
            if (h2 >= h) {
                i += 1;
                break;
            }
            i += 1;
        }

        return i;
    }
}