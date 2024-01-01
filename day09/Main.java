import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

enum Dir {
    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

    final int dx;
    final int dy;

    Dir(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    static Dir from(String value) {
        for (Dir dir : values()) {
            if (dir.name().startsWith(value)) {
                return dir;
            }
        }
        return null;
    }
}

record Move(Dir dir, int steps) {}

class Pos {
    int x;
    int y;

    Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Pos(Pos p) {
        x = p.x;
        y = p.y;
    }

    void move(Dir dir) {
        x += dir.dx;
        y += dir.dy;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pos pos)) return false;
        return x == pos.x && y == pos.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

class Rope {
    final Pos[] nodes;

    Rope(int len) {
        nodes = new Pos[len];
        for (int i = 0; i < len; i++) {
            nodes[i] = new Pos(0, 0);
        }
    }

    void pull(Dir dir) {
        nodes[0].move(dir);
        for (int i = 1; i < nodes.length; i++) {
            adjust(nodes[i], nodes[i-1]);
        }
    }

    void adjust(Pos p, Pos prev) {
        if (Math.abs(prev.x - p.x) > 1 || Math.abs(prev.y - p.y) > 1) {
            p.x += sign(prev.x - p.x);
            p.y += sign(prev.y - p.y);
        }
    }

    Pos tail() {
        return nodes[nodes.length - 1];
    }

    int sign(int v) {
        return Integer.compare(v, 0);
    }

    @Override
    public String toString() {
        return Arrays.stream(nodes).map(Pos::toString).collect(Collectors.joining("/"));
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        new Main().run(args.length > 0 ? args[0] : "input.txt");
    }

    void run(String file) throws Exception {
        try (Scanner in = new Scanner(new File(file))) {
            List<Move> moves = new ArrayList<>();
            while (in.hasNext()) {
                String line = in.nextLine();
                if (line.isBlank()) break;
                String[] parts = line.split(" ");
                Dir dir = Dir.from(parts[0]);
                int steps = Integer.parseInt(parts[1]);
                moves.add(new Move(dir, steps));
            }

            int sum1 = solve(2, moves);
            System.out.println("Part 1: " + sum1);

            int sum2 = solve(10, moves);
            System.out.println("Part 2: " + sum2);
        }
    }

    private int solve(int len, List<Move> moves) {
        Set<Pos> tail = new HashSet<>();
        Rope rope = new Rope(len);
        for (Move move : moves) {
            for (int i = 0; i < move.steps(); i++) {
                rope.pull(move.dir());
                tail.add(new Pos(rope.tail()));
            }
        }
        return tail.size();
    }
}
