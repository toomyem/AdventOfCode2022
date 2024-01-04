import java.io.File;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

record Pos(int x, int y) {
    Pos down() {
        return new Pos(x, y + 1);
    }

    Pos downLeft() {
        return new Pos(x - 1, y + 1);
    }

    Pos downRight() {
        return new Pos(x + 1, y + 1);
    }
}

class Board extends HashMap<Pos, Character> {
    int floor = -1;

    void enableFloor() {
        floor = downRight().y() + 2;
    }

    void addRocks(Pos[] segments) {
        Pos prev = null;
        for (Pos pos : segments) {
            if (prev != null) {
                int len = Math.abs(pos.x() - prev.x()) + Math.abs(pos.y() - prev.y());
                int dx = sign(pos.x() - prev.x());
                int dy = sign(pos.y() - prev.y());
                for (int i = 0; i <= len; i++) {
                    Pos p = new Pos(prev.x() + dx * i, prev.y() + dy * i);
                    put(p, '#');
                }
            }
            prev = pos;
        }
    }

    int sign(int v) {
        return Integer.compare(v, 0);
    }

    Pos upLeft() {
        IntSummaryStatistics xStat = keySet().stream().mapToInt(Pos::x).summaryStatistics();
        IntSummaryStatistics yStat = keySet().stream().mapToInt(Pos::y).summaryStatistics();
        return new Pos(xStat.getMin(), yStat.getMin());
    }

    Pos downRight() {
        IntSummaryStatistics xStat = keySet().stream().mapToInt(Pos::x).summaryStatistics();
        IntSummaryStatistics yStat = keySet().stream().mapToInt(Pos::y).summaryStatistics();
        return new Pos(xStat.getMax(), yStat.getMax());
    }

    @Override
    public String toString() {
        String result = "";
        Pos p1 = upLeft();
        Pos p2 = downRight();
        for (int y = 0; y <= p2.y() + (floor > -1 ? 2 : 0); y++) {
            result += String.format("%3d ", y);
            for (int x = p1.x(); x <= p2.x(); x++) {
                char ch = get(new Pos(x, y));
                if (x == 500 && y == 0) {
                    ch = '+';
                }
                result += ch;
            }
            result += "\n";
        }
        return result;
    }

    char get(Pos pos) {
        if (pos.y() == floor) return '#';
        return getOrDefault(pos, '.');
    }

    public void cleanSand() {
        List<Pos> toRemove = entrySet().stream().filter(e -> e.getValue() == '*').map(Entry::getKey).toList();
        for (Pos pos : toRemove) {
            remove(pos);
        }
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        new Main().run(args.length > 0 ? args[0] : "input.txt");
    }

    void run(String file) throws Exception {
        try (Scanner in = new Scanner((new File(file)))) {
            Board board = new Board();
            while (in.hasNext()) {
                String line = in.nextLine();
                if (line.isBlank()) break;
                Pos[] segments = parseLine(line);
                board.addRocks(segments);
            }

            int steps = 0;
            while (simulate(board)) {
                steps += 1;
            }
            System.out.println("Part 1: " + steps);

            board.cleanSand();
            board.enableFloor();
            steps = 0;
            while (simulate(board)) {
                steps += 1;
            }
            System.out.println("Part 2: " + steps);
        }
    }

    boolean simulate(Board board) {
        int bottom = board.downRight().y() + (board.floor > -1 ? 2 : 0);
        Pos pos = new Pos(500, 0);
        if (board.get(pos) == '*') return false;
        while (pos.y() <= bottom) {
            Pos next = null;
            for (Pos p : new Pos[]{pos.down(), pos.downLeft(), pos.downRight()}) {
                char ch = board.get(p);
                if (ch == '.') {
                    next = p;
                    break;
                }
            }
            if (next == null) {
                break;
            }
            pos = next;
        }
        board.put(pos, '*');
        return pos.y() <= bottom;
    }

    Pos[] parseLine(String line) {
        String[] parts = line.split(" -> ");
        Pos[] result = new Pos[parts.length];
        for (int i = 0; i < result.length; i++) {
            String[] c = parts[i].split(",");
            result[i] = new Pos(Integer.parseInt(c[0]), Integer.parseInt(c[1]));
        }
        return result;
    }
}