import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) throws Exception {
    new Main().run();
  }

  void run() throws Exception {
    try (Scanner in = new Scanner(new File("input.txt"))) {

      int sum1 = 0;
      int sum2 = 0;
      List<String> group = new ArrayList<>();
      
      while (in.hasNext()) {
        String line = in.nextLine();
        if (line.isBlank()) continue;
        int i = line.length();
        String part1 = line.substring(0, i / 2);
        String part2 = line.substring(i / 2);

        Set<Character> set1 = split(part1);
        Set<Character> set2 = split(part2);
        Set<Character> common = calcCommon(set1, set2);

        sum1 += sumPriorities(common);

        group.add(line);
        if (group.size() == 3) {
          common = group.stream().map(s -> split(s)).reduce((s1, s2) -> calcCommon(s1, s2)).get();
          sum2 += sumPriorities(common);
          group.clear();
        }
      }

      System.out.println("Part 1: " + sum1);
      System.out.println("Part 2: " + sum2);
    }
  }

  Set<Character> split(String s) {
    return new HashSet<>(Arrays.asList(s.split("")).stream().map(a -> a.charAt(0)).collect(Collectors.toSet()));
  }
  
  Set<Character> calcCommon(Set<Character> set1, Set<Character> set2) {
    return set1.stream().filter(e -> set2.contains(e)).collect(Collectors.toSet());
  }

  int priority(char ch) {
    if (ch >= 'a' && ch <= 'z') return ch - 'a' + 1;
    if (ch >= 'A' && ch <= 'Z') return ch - 'A' + 27;
    return 0;
  }

  int sumPriorities(Set<Character> set) {
    return set.stream().mapToInt(this::priority).sum();
  }
}

