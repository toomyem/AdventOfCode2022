import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.stream.Stream;

class Exp extends ArrayList<Object> {

    Exp() {
        super();
    }

    Exp(int v) {
        super();
        add(v);
    }

    Exp(Exp e) {
        super();
        add(e);
    }
}
record Pair(Exp exp1, Exp exp2) {}

public class Main {
    public static void main(String[] args) throws Exception {
        new Main().run(args.length > 0 ? args[0] : "input.txt");
    }

    void run(String file) throws Exception {
        try (Scanner in = new Scanner(new File(file))) {

            List<Pair> pairs = new ArrayList<>();
            Exp exp1 = null;
            while (in.hasNext()) {
                String line = in.nextLine();
                if (line.isBlank()) continue;
                if (exp1 == null) {
                    exp1 = parse(line);
                } else {
                    Exp exp2 = parse(line);
                    pairs.add(new Pair(exp1, exp2));
                    exp1 = null;
                }
            }

            int sum1 = solve1(pairs);
            System.out.println("Part 1: " + sum1);

            int sum2 = solve2(pairs.stream().flatMap(p -> Stream.of(p.exp1(), p.exp2())).toList());
            System.out.println("Part 2: " + sum2);
        }
    }

    private int solve1(List<Pair> pairs) {
        int sum = 0;
        for (int i = 0; i < pairs.size(); i++) {
            Pair pair = pairs.get(i);
            if (compare(pair.exp1(), pair.exp2()) == -1) {
                sum += (i + 1);
            }
        }
        return sum;
    }

    private int solve2(List<Exp> list) {
        List<Exp> exps = new ArrayList<>(list);
        Exp exp2 = new Exp(new Exp(2));
        Exp exp6 = new Exp(new Exp(6));
        exps.add(exp2);
        exps.add(exp6);
        exps.sort(this::compare);

        int mul = 1;
        int i = 1;
        for (Exp exp : exps) {
            if (exp == exp2 || exp == exp6) {
                mul *= i;
            }
            i += 1;
        }
        return mul;
    }

    int compare(Exp exp1, Exp exp2) {
        int i = 0;
        while (i < exp1.size() || i < exp2.size()) {
            Object v1 = i < exp1.size() ? exp1.get(i) : null;
            Object v2 = i < exp2.size() ? exp2.get(i) : null;
            if (v1 == null) return -1;
            if (v2 == null) return 1;
            if (v1 instanceof Integer e1 && v2 instanceof Integer e2) {
                if (e1 < e2) return -1;
                if (e1 > e2) return 1;
            } else if (v1 instanceof Integer e1 && v2 instanceof Exp e2) {
                int c = compare(new Exp(e1), e2);
                if (c != 0) return c;
            } else if (v1 instanceof Exp e1 && v2 instanceof Integer e2) {
                int c = compare(e1, new Exp(e2));
                if (c != 0) return c;
            } else if (v1 instanceof Exp e1 && v2 instanceof Exp e2) {
                int c = compare(e1, e2);
                if (c != 0) return c;
            }
            i += 1;
        }
        return 0;
    }

    Exp parse(String line) {
        StringTokenizer st = new StringTokenizer(line, "[],", true);
        List<Exp> stack = new ArrayList<>();
        Exp exp = new Exp();
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (tok.equals("[")) {
                Exp exp2 = new Exp();
                exp.add(exp2);
                stack.add(0, exp);
                exp = exp2;
            } else if (tok.equals("]")) {
                exp = stack.remove(0);
            } else if (!tok.equals(",")) {
                exp.add(Integer.parseInt(tok));
            }
        }
        return (Exp) exp.get(0);
    }
}
