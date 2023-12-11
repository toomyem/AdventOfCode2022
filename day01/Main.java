import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws Exception {
    try (Scanner in = new Scanner(new File("input.txt"))) {

      int sum = 0;
      List<Integer> snacks = new ArrayList<>();
      while (in.hasNext()) {
        String line = in.nextLine();
        if (line.isBlank()) {
          snacks.add(sum);
          sum = 0;
        } else {
          sum += Integer.parseInt(line);
        }
      }

      int max = snacks.stream().mapToInt(i -> i).max().getAsInt();
      System.out.println("Part 1: " + max);

      sum = snacks.stream().sorted((a, b) -> b - a).mapToInt(i -> i).limit(3).sum();
      System.out.println("Part 2: " + sum);
    }
  }
}
