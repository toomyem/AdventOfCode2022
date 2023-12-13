import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;


public class Main {
    public static void main(String[] args) throws Exception {
        new Main().run();
    }

    void run() throws Exception {
        try (Scanner in = new Scanner(new File("input.txt"))) {

            Drive drive = new Drive();
            while (in.hasNext()) {
                String line = in.nextLine();
                drive.parse(line);
            }

            AtomicInteger sum = new AtomicInteger(0);
            List<Integer> sizes = new ArrayList<>();
            drive.forEachFolder(f -> {
                if (f.size < 100000) {
                    sum.addAndGet(f.size);
                }
                sizes.add(f.size);
            });
            System.out.println("Part 1: " + sum);
            sizes.sort(Comparator.comparingInt(a -> a));

            int needed = 30000000;
            int free = drive.getFree();

            for (int size : sizes) {
                if (free + size > needed) {
                    System.out.println("Part 2: " + size);
                    break;
                }
            }
        }
    }
}

class Folder {
    final Map<String, Folder> folders = new HashMap<>();
    final Map<String, Integer> files = new HashMap<>();
    final Folder parent;
    int size = 0;

    Folder(Folder parent) {
        this.parent = parent;
    }
}

class Drive {

    private final Folder root = new Folder(null);
    private Folder cwd = root;

    int getFree() {
        return 70000000 - root.size;
    }

    void parse(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[1];

        if (parts[0].equals("$")) {
            if (cmd.equals("cd")) {
                if (parts[2].equals("/")) {
                    cwd = root;
                } else if (parts[2].equals("..")) {
                    cwd = cwd.parent;
                } else {
                    cwd = cwd.folders.get(parts[2]);
                }
            }
        } else {
            if (parts[0].equals("dir")) {
                cwd.folders.computeIfAbsent(parts[1], k -> new Folder(cwd));
            } else {
                int size = Integer.parseInt(parts[0]);
                cwd.files.put(parts[1], size);
                for (Folder f = cwd; f != null; f = f.parent) {
                    f.size += size;
                }
            }
        }
    }

    void forEachFolder(Consumer<Folder> op) {
        forEachFolder(op, root);
    }

    private void forEachFolder(Consumer<Folder> op, Folder f) {
        op.accept(f);
        f.folders.values().forEach(f2 -> forEachFolder(op, f2));
    }
}