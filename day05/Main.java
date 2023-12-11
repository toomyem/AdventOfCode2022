import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;


public class Main {
  public static void main(String[] args) throws Exception {
    new Main().run();
  }

  void run() throws Exception {
    try (Scanner in = new Scanner(new File("input.txt"))) {

      Map<Integer, String> stacks = new HashMap<>();
      
      while (in.hasNext()) {
        String line = in.nextLine();
        if (!line.contains("[")) break;
        for (int i = 1; i < line.length(); i += 4) {
          char ch = line.charAt(i);
          if (ch >= 'A' && ch <= 'Z') {
            int s = (i + 3) / 4;
            stacks.put(s, stacks.getOrDefault(s, "") + ch);
          }
        }
      }

      Pattern p = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
      
      Map<Integer, String> stacks2 = new HashMap<>(stacks);
      
      while (in.hasNext()) {
        String line = in.nextLine();
        Matcher m = p.matcher(line);
        if (!m.find()) continue;
        int n = Integer.parseInt(m.group(1));
        int from = Integer.parseInt(m.group(2));
        int to = Integer.parseInt(m.group(3));
        move1(stacks, n, from, to);
        move2(stacks2, n, from, to);
      }
      
      System.out.println("Part 1: " + answer(stacks));
      System.out.println("Part 2: " + answer(stacks2));
    }
  }

  String answer(Map<Integer, String> stacks) {
      return IntStream.range(1, 10).mapToObj(i -> stacks.getOrDefault(i, "")).filter(s -> !s.isEmpty()).map(s -> "" + s.charAt(0)).reduce("", (a, b) -> a + b);
  }
  
  void move1(Map<Integer, String> stacks, int n, int from, int to) {
    for (int i = 0; i < n; i++) {
      String s = stacks.get(from);
      stacks.put(from, s.substring(1));
      stacks.put(to, s.substring(0, 1) + stacks.get(to));
    }
  }

  void move2(Map<Integer, String> stacks, int n, int from, int to) {
    String s = stacks.get(from);
    stacks.put(from, s.substring(n));
    stacks.put(to, s.substring(0, n) + stacks.get(to));
  }
}

