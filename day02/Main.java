import java.io.File;
import java.util.Scanner;

enum Game {
  ROCK("AX", 1), PAPER("BY", 2), SCISSORS("CZ", 3);

  final String symbols;
  final int points;

  Game(String symbols, int points) {
    this.symbols = symbols;
    this.points = points;
  }

  static Game from(String value) {
    for (Game g : values()) {
      if (g.symbols.contains(value)) {
        return g;
      }
    }
    return null;
  }
}

enum Outcome {
  WIN("Z"), LOOSE("X"), DRAW("Y");

  final String symbol;

  Outcome(String value) {
    this.symbol = value;
  }
  
  static Outcome from(String value) {
    for (Outcome o : values()) {
      if (o.symbol.contains(value)) {
        return o;
      }
    }
    return null;
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
        String[] parts = line.split(" ");
        Game p1 = Game.from(parts[0]);
        Game p2 = Game.from(parts[1]);
        Outcome o = Outcome.from(parts[1]);
        sum1 += play(p1, p2);
        sum2 += play(p1, decide(p1, o));
      }

      System.out.println("Part 1: " + sum1);
      System.out.println("Part 2: " + sum2);
    }
  }

  int play(Game p1, Game p2) {
    int points = p2.points;

    if (p1 == p2) return points + 3;
    
    return points + switch (p2) {
      case ROCK -> p1 == Game.SCISSORS ? 6 : 0;
      case PAPER -> p1 == Game.ROCK ? 6 : 0;
      case SCISSORS -> p1 == Game.PAPER ? 6 : 0;
    };
  }

  Game decide(Game p1, Outcome o) {
    if (o == Outcome.DRAW) return p1;

    return switch (p1) {
      case ROCK -> o == Outcome.WIN ? Game.PAPER : Game.SCISSORS;
      case PAPER -> o == Outcome.WIN ? Game.SCISSORS : Game.ROCK;
      case SCISSORS -> o == Outcome.WIN ? Game.ROCK : Game.PAPER;
    };
  }
}

