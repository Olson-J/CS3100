/*
 * Assignment 3 for cs3100
 * This program uses all available cores on the machine
 * to find and display the first 1000 digits of pi to the
 * right of the decimal
 * @author Julie Olson
 */

package assign3;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.math.BigDecimal;

public class Assign3 {
    public static void main(String[] args) {
        /** This main function is in charge of timing and running
         * the program as a whole. The timer will be started, then
         * the queue, result table, and threads will be created.
         * After all threads have completed their work, the timer 
         * will be stopped and the results will be printed to the
         * screen.
         */

        long startTime = System.currentTimeMillis();
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
        ResultTable table = new ResultTable();
        TaskQueue taskQueue = new TaskQueue(queue);
        
        // Create tasks and add them to the taskQueue
        taskQueue.createTasks();
        
        // find number of cores
        int cores = Runtime.getRuntime().availableProcessors();
        
        // Add end-of-processing tasks to the queue
        for (int i = 0; i < cores; i++) {
            taskQueue.endProcessing();
        }
        
        // make as many threads as there are cores
        WorkerThread[] workers = new WorkerThread[cores];
        for (int i = 0; i < cores; i++) {
            workers[i] = new WorkerThread(queue, table);
            workers[i].start();
        }

        // wait for all worker threads to finish 
        for (WorkerThread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();

        // Calculate the elapsed time
        long elapsedTime = endTime - startTime;
        
        // print the results
        System.out.println();
        table.printValues();
        System.out.println();
        System.out.println("Calculations took: " + elapsedTime + " milliseconds");
    }
}


class WorkerThread extends Thread {
    /* This class creates the threads that will be 
    finding and storing a specified digit of pi.  */

    private final BlockingQueue<Task> taskQueue;
    private final ResultTable resultTable;
    private static final AtomicInteger spacer = new AtomicInteger(0);
    private static final AtomicInteger counter = new AtomicInteger(0);
    private Bpp bpp = new Bpp();

    // constructor
    public WorkerThread(BlockingQueue<Task> taskQueue, ResultTable resultTable) {
        this.taskQueue = taskQueue;
        this.resultTable = resultTable;
    }

    public void run() {
        try {
            while (true) {
                // Attempt to retrieve a task from the queue, blocking if the queue is empty
                Task task = taskQueue.take();

                // Check for a special task indicating the end of processing
                if (task == TaskQueue.END_OF_PROCESSING_TASK) {
                    break;  // Exit the loop when the special task is encountered
                }
                
                // find the value of the specified digit of pi
                BigDecimal computedResult = BigDecimal.valueOf(bpp.getDecimal(task.getDigitPosition()));

                // Store the result in the hash table
                resultTable.store(task.getDigitPosition(), computedResult);

                // print "." for every 10 digits found
                if (counter.incrementAndGet() % 10 == 0) {
                    if (spacer.incrementAndGet() % 20 == 0) {
                        System.out.println();
                    }
                    System.out.print(".");
                }
                System.out.flush();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }
}

class Task {
    /* This class creates the tasks, which
     * keep track of the position of the digit
     * they are to find
     */
    private final int digitPosition;

    public Task(int digitPosition) {
        this.digitPosition = digitPosition;
    }

    public long getDigitPosition() {
        long position = (long) digitPosition;
        return position;
    }
}


class TaskQueue {
    /* This class manages the queue of tasks to be completed.
     * It allows tasks to be added to the queue, and contains 
     * a flag to signify the endof processing.
     * 1000 tasks are created, shuffled, and added to the queue.
      */
    private final ReentrantLock lock = new ReentrantLock();
    public final static Task END_OF_PROCESSING_TASK = new assign3.Task(-1);
    private final BlockingQueue<Task> queue;

    // constructor
    public TaskQueue(BlockingQueue<Task> queue) {
        this.queue = queue;
    }

    public void addTask(Task task) {
        lock.lock();
        try {
            queue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt(); // Restore interrupted status
        } finally {
            lock.unlock();
        }
    }

    public void endProcessing() {
        // Add a special task to signal the end of processing
        lock.lock();
        try {
            addTask(END_OF_PROCESSING_TASK);
        } finally {
            lock.unlock();
        }
    }
    

    public void createTasks() {
        List<Task> values = new ArrayList<>();
        // make 1000 tasks
        for (int i = 0; i <= 1000; i++) {
            values.add(new Task(i));
        }

        // Shuffle the list of tasks
        Collections.shuffle(values);

        // add shuffled tasks to the queue
        for (Task task : values) {
            addTask(task);
        }
        endProcessing();
    }
}

class ResultTable {
    /* This class creates a table for the digit calculation
     * results to be stored in, and allows digits to be added
     * and later printed all at once.
     */
    private final Map<Long, BigDecimal> results;
    private final Lock lock;

    // constructor
    public ResultTable() {
        this.results = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    // add digit to the table
    public void store(Long position, BigDecimal value) {
        lock.lock();
        try {
            results.put(position, value);
        } finally {
            lock.unlock();
        }
    }

    // print all values present in the table
    public void printValues() {
        lock.lock();
        int flag = 0;
        try {
            for (BigDecimal value : results.values()) {
                if (flag == 0) {
                    System.out.print("3.");
                    flag++;
                } else {
                    System.out.print(value);
                }
            }
        } finally {
            lock.unlock();
        }
    }
}

class Bpp {
    /**
     * Prints the nth number of pi followed by the next 8 numbers in base 10.
     * This program is based on Bellard's work.
     * @author feltocraig
     * 
     * NOTE: some edits have been made to feltocraig's original code
     * to better suit the goal of this program
     */
   

   /**
    * Returns the nth digit of pi followed by the next 8 numbers
    * @param n - nth number of pi to return
    * @return returns an integer value containing 8 digits after n
    */
   public int getDecimal(long n) {
        // cases for the first three digits that can't be found using the function
        if (n == 0) {
            return 3;
        } else if (n == 1) {
            return 1;
        } else if (n == 2) {
            return 4;
        }

       long av, a, vmax, N, num, den, k, kq, kq2, t, v, s, i;
       double sum;

       N = (long) ((n + 20) * Math.log(10) / Math.log(2));

       sum = 0;

       for (a = 3; a <= (2 * N); a = nextPrime(a)) {

           vmax = (long) (Math.log(2 * N) / Math.log(a));
           av = 1;
           for (i = 0; i < vmax; i++)
               av = av * a;

           s = 0;
           num = 1;
           den = 1;
           v = 0;
           kq = 1;
           kq2 = 1;

           for (k = 1; k <= N; k++) {

               t = k;
               if (kq >= a) {
                   do {
                       t = t / a;
                       v--;
                   } while ((t % a) == 0);
                   kq = 0;
               }
               kq++;
               num = mulMod(num, t, av);

               t = (2 * k - 1);
               if (kq2 >= a) {
                   if (kq2 == a) {
                       do {
                           t = t / a;
                           v++;
                       } while ((t % a) == 0);
                   }
                   kq2 -= a;
               }
               den = mulMod(den, t, av);
               kq2 += 2;

               if (v > 0) {
                   t = modInverse(den, av);
                   t = mulMod(t, num, av);
                   t = mulMod(t, k, av);
                   for (i = v; i < vmax; i++)
                       t = mulMod(t, a, av);
                   s += t;
                   if (s >= av)
                       s -= av;
               }

           }

           t = powMod(10, n - 1, av);
           s = mulMod(s, t, av);
           sum = (sum + (double) s / (double) av) % 1;
       }
       // was originally multiplied by 1e9, changed to 10 since we only need one digit for this project
       return (int) (sum * 10);
   }

   private long mulMod(long a, long b, long m) {
       return (long) (a * b) % m;
   }

   private long modInverse(long a, long n) {
       long i = n, v = 0, d = 1;
       while (a > 0) {
           long t = i / a, x = a;
           a = i % x;
           i = x;
           x = d;
           d = v - t * x;
           v = x;
       }
       v %= n;
       if (v < 0)
           v = (v + n) % n;
       return v;
   }

   private long powMod(long a, long b, long m) {
       long tempo;
       if (b == 0)
           tempo = 1;
       else if (b == 1)
           tempo = a;

       else {
           long temp = powMod(a, b / 2, m);
           if (b % 2 == 0)
               tempo = (temp * temp) % m;
           else
               tempo = ((temp * temp) % m) * a % m;
       }
       return tempo;
   }

   private boolean isPrime(long n) {
       if (n == 2 || n == 3)
           return true;
       if (n % 2 == 0 || n % 3 == 0 || n < 2)
           return false;

       long sqrt = (long) Math.sqrt(n) + 1;

       for (long i = 6; i <= sqrt; i += 6) {
           if (n % (i - 1) == 0)
               return false;
           else if (n % (i + 1) == 0)
               return false;
       }
       return true;
   }

   private long nextPrime(long n) {
       if (n < 2)
           return 2;
       if (n == 9223372036854775783L) {
           System.err.println("Next prime number exceeds Long.MAX_VALUE: " + Long.MAX_VALUE);
           return -1;
       }
       for (long i = n + 1;; i++)
           if (isPrime(i))
               return i;
   }
}
