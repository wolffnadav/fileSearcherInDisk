// Nadav Wolff
// 204098610

import java.io.File;
import java.io.FileNotFoundException;


public class DiskSearcher {
    public static final int DIRECTORY_QUEUE_CAPACITY = 50;
    public static final int RESULTS_QUEUE_CAPACITY = 50;

    public static void main(String[] args) {

        if (args.length != 5) {
            System.out.println("nop");
            System.exit(1);
        }
        File root = new File(args[1]);
        if (root.isDirectory() && !root.exists()) {
            System.out.println("nop again");
            System.exit(1);
        }
        File dest = new File(args[2]);
        if (dest.isDirectory() && !dest.exists()) {
            System.out.println("nop again2");
            System.exit(1);
        }
        int seracherNum = Integer.parseInt(args[3]);
        int copierNum = Integer.parseInt(args[4]);
        if ((copierNum < 1) || (seracherNum < 1)) {
            System.err.println("nop again3");
            System.exit(1);
        }
        SynchronizedQueue directoryQueue = new SynchronizedQueue(DIRECTORY_QUEUE_CAPACITY);
        SynchronizedQueue resultsQueue = new SynchronizedQueue(RESULTS_QUEUE_CAPACITY);



        Scouter scouter = new Scouter(directoryQueue, root);
        scouter.run();

        Thread[] thSearch = new Thread[seracherNum];
        for (int i = 0; i < seracherNum; i++) {
            thSearch[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    Searcher search = new Searcher(args[0], directoryQueue, resultsQueue);
                    search.run();
                }
            });
            thSearch[i].start();
        }

        Thread[] thCopy = new Thread[copierNum];
        for (int i = 0; i < copierNum; i++) {
            thCopy[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    Copier copy = new Copier(dest, resultsQueue);
                    copy.run();
                }
            });
            thCopy[i].start();
        }
    }
}