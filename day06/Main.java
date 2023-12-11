import java.io.File;
import java.util.Scanner;


public class Main {
  public static void main(String[] args) throws Exception {
    new Main().run();
  }

  void run() throws Exception {
    try (Scanner in = new Scanner(new File("input.txt"))) {

      while (in.hasNext()) {
        String line = in.nextLine();
        System.out.println("Part 1: " + marker(line, 4));
        System.out.println("Part 2: " + marker(line, 14));
      }
    }
  }

  int marker(String line, int n) {
    for (int i = n; i < line.length(); i++) {
      String m = line.substring(i - n, i);
      if (unique(m)) return i;
    }
    return 0;
  }

  boolean unique(String m) {
    for (int i = 0; i < m.length(); i++) {
      for (int j = i + 1; j < m.length(); j++) {
        if (m.charAt(i) == m.charAt(j)) return false;
      }
    }
    return true;
  }
}

