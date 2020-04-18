
// Nadav Wolff
// 204098610

import java.io.*;

public class Copier implements Runnable {
    public static final int COPY_BUFFER_SIZE = 4096;

    private File destination;
    private SynchronizedQueue<File> resultsQueue;

    /**
     * Constructor. Initializes the worker
     * with a destination directory and a queue of files to copy.
     *
     * @param destination   - Destination directory
     * @param resultsQueue- Queue of files found, to be copied
     */
    Copier(File destination, SynchronizedQueue resultsQueue) {
        this.destination = destination;
        this.resultsQueue = resultsQueue;
    }

    /**
     * Runs the copier thread.
     * Thread will fetch files from queue and copy them,
     * one after each other, to the destination directory.
     * When the queue has no more files, the thread finishes.
     */
    public void run() {
        BufferedInputStream is;
        BufferedOutputStream os;
        byte[] buffer = new byte[COPY_BUFFER_SIZE];
        try {
            while (this.resultsQueue.getSize() > 0) {
                File file = this.resultsQueue.dequeue();
                String name = this.destination.toString() + "/" + file.getName();
                is = new BufferedInputStream(new FileInputStream(file));
                os = new BufferedOutputStream(new FileOutputStream(name));
                int read;
                while ((read = is.read(buffer)) > 0) {
                    os.write(buffer, 0, read);
                    os.flush();
                }
                is.close();
                os.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


