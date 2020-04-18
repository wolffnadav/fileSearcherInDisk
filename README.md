# fileSearcherInDisk

I was creating a multithreaded search utility.
The utility will allow searching for files that contain some given pattern, under some given root directory.
Files that contain this pattern will be copied to some specified directory.

A.The class SynchronizedQueue
This class should allow multithreaded enqueue/dequeue operations.

B. The class Scouter that implements Runnable.
This class is responsible to list all directories that exist under the given root directory.
It enqueues all directories into the directory queue. There is always only one scouter thread in the system.
      
C. The class Searcher that implements Runnable.
This class reads a directory from the directory queue and lists all files in this directory.
Then, it checks each file to see if the file name contains the pattern given.
Files that contain the pattern are enqueued to the results queue (to be copied).

D. The class Copier implements Runnable.
This class reads a file from the results queue (the queue of files that contains the pattern),
and copies it into the specified destination directory.

E. The class DiskSearcher.
This is the main class of the application. 
This class contains a main method that starts the search process according to the given command lines.
Usage of the main method from command line goes as follows:
> java DiskSearcher 
<filename-pattern> <root directory> <destination directory> <# of searchers> <# of copiers>

For example:
> java DiskSearcher solution Users/OS_Exercises Users/temp 10 5
This will run the search application to look for files with the string “solution” inside them, in the directory 
Users/OS_Exercises and all of its subdirectories.
Any matched file will be copied to Users/temp. The application will use 10 searcher threads and 5 copier threads.
Specifically, it should:
- Start a single scouter thread
- Start a group of searcher threads (number of searchers as specified in arguments)
- Start a group of copier threads (number of copiers as specified in arguments)
- Wait for scouter to finish
- Wait for searcher and copier threads to finish
