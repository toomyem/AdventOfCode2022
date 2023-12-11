import java.io.File;
import java.util.Scanner;

record Assignment(int a, int b) {
  Assignment(String s) {
    this(Integer.parseInt(s.split("-")[0]), Integer.parseInt(s.split("-")[1]));
  }

  boolean contains(Assignment o) {
    return o.a >= a && o.b <= b;
  }

  boolean overlap(Assignment o) {
    return b >= o.a && a <= o.b;
  }
}

public class Main {
  public static void main(String[] args) throws Exception {
    new Main().run();
  }

  void run() throws Exception {
    try (Scanner in = new Scanner(new File("input.txt"))) {

      int sum1 = 0;
      int sum2 = 0;
      
      while (in.hasNext()) {
        String line = in.nextLine();
        String[] parts = line.split(",");
        Assignment a1 = new Assignment(parts[0]);
        Assignment a2 = new Assignment(parts[1]);
        System.out.println(a1 + ", " + a2);
        if (a1.contains(a2) || a2.contains(a1)) sum1 += 1;
        if (a1.overlap(a2)) sum2 += 1;
      }

      System.out.println("Part 1: " + sum1);
      System.out.println("Part 2: " + sum2);
    }
  }
}

