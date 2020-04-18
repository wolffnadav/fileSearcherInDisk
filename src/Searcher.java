// Nadav Wolff
// 204098610

import java.io.File;

public class Searcher implements Runnable {

    private String pattern;
    private SynchronizedQueue<File> directoryQueue;
    private SynchronizedQueue<File> resultsQueue;

    /**
     * Constructor. Initializes the searcher thread.
     *
     * @param pattern        - Pattern to look for
     * @param directoryQueue - A queue with directories to search in (as listed by the scouter)
     * @param resultsQueue   - A queue for files found (to be copied by a copier)
     */
    public Searcher(String pattern, SynchronizedQueue directoryQueue,
                    SynchronizedQueue resultsQueue) {
        this.pattern = pattern;
        this.directoryQueue = directoryQueue;
        this.resultsQueue = resultsQueue;
    }

    /**
     * Runs the searcher thread.
     * Thread will fetch a directory to search in from the directory queue,
     * then search all files inside it (but will not recursively search subdirectories!).
     * Files that are found to contain the pattern are enqueued to the results queue.
     * This method begins by registering to the results queue as a producer and when
     * finishes, it unregisters from it.
     */
    public void run() {
        File file;
        File[] paths;
        // for each directory in directoryQueue array
        while (this.directoryQueue.getSize() > 0) {
            // returns pathnames for files and directory
            file = this.directoryQueue.dequeue();
            if (file != null) {
                paths = file.listFiles();
                for (int j = 0; j < paths.length; j++) {
                    if (paths[j].isFile()) {
                        if ((paths[j].toString().contains(this.pattern))) {
                            this.resultsQueue.enqueue(paths[j]);
                        }
                    }
                }
            }
        }
    }


}
