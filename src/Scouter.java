// Nadav Wolff
// 204098610

import java.io.File;

public class Scouter implements Runnable {

    private SynchronizedQueue<File> directoryQueue;
    private File root;

    /**
     * Construnctor. Initializes the scouter with a queue for the
     * directories to be searched and a root directory to start from.
     *
     * @param directoryQueue - A queue for directories to be searched
     * @param root           - Root directory to start from
     */
    public Scouter(SynchronizedQueue directoryQueue, File root) {
        this.directoryQueue = directoryQueue;
        this.root = root;
    }

    /**
     * Starts the scouter thread. Lists directories under root directory and adds them to queue,
     * then lists directories in the next level and enqueues them and so on.
     * This method begins by registering to the directory queue as a producer and when finishes,
     * it unregisters from it.
     */
    public void run() {
        this.directoryQueue.registerProducer();
        this.directoryQueue.enqueue(this.root);
        allDirectories(this.root);
        this.directoryQueue.unregisterProducer();
    }

    private void allDirectories(File file) {
        // returns pathnames for files and directory
        File[] paths = file.listFiles();
        // for each pathname in pathname array
        for (int i = 0; i < paths.length; i++) {
            if (paths[i].isDirectory()) {
                this.directoryQueue.enqueue(paths[i]);
                allDirectories(paths[i]);
            }
        }
    }

}
