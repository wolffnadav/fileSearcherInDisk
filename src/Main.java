// Nadav Wolff
// 204098610

import java.util.List;
import java.util.ArrayList;
import java.io.*;


public class Main {

    public static void main(String[] args) throws IOException {

        List<String> lines = getLinesFromFile();
        System.out.println("Number of lines found: " + lines.size());
        System.out.println("Starting to process");

        long startTimeWithoutThreads = System.currentTimeMillis();
        workWithoutThreads(lines);
        long elapsedTimeWithoutThreads = (System.currentTimeMillis() - startTimeWithoutThreads);
        System.out.println("Execution time: " + elapsedTimeWithoutThreads);


        long startTimeWithThreads = System.currentTimeMillis();
        workWithThreads(lines);
        long elapsedTimeWithThreads = (System.currentTimeMillis() - startTimeWithThreads);
        System.out.println("Execution time: " + elapsedTimeWithThreads);

    }

    private static void workWithThreads(List<String> lines) {
        ArrayList<Thread> workers = new ArrayList<>();

        //Get the number of available cores
        int x = Runtime.getRuntime().availableProcessors();
        //Assuming X is the number of cores - Partition the data into x data sets
        //Create X threads that will execute the Worker class
        int y = lines.size() / x;
        for (int i = 1; i <= x; i++) {
            List<String> line = lines.subList(y * (i - 1), (y * i) - 1);
            Thread t = new Thread(new Worker(line));
            workers.add(t);
            t.start();
        }
        //Wait for all threads to finish
        for (Thread thread : workers) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException");
            }
        }

    }

    private static void workWithoutThreads(List<String> lines) {
        Worker worker = new Worker(lines);
        worker.run();
    }

    //Your code:
    //Read the shakespeare file provided from C:\Temp\Shakespeare.txt
    //and return an ArrayList<String> that contains each line read from the file.
    private static List<String> getLinesFromFile() throws IOException {
//        String pathname = "/Users/wolffnadav/IdeaProjects/test/src/Shakespeare.txt";
      String pathname =  "‎C:\\\\Temp\\\\Shakespeare.txt";
        List<String> lines = new ArrayList<>();
        try {
            FileReader file = new FileReader(pathname);
            BufferedReader line = new BufferedReader(file);
            String lineThis;
            while ((lineThis = line.readLine()) != null) {
                lines.add(lineThis);
            }
            file.close();
            line.close();
        } catch (IOException e) {
            System.out.println(e.toString());
            System.out.println("Could not find file ");
        }
        return lines;
    }
}
