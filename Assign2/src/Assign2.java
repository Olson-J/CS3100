/** 
 * Assignment 2 for CS3100
 * @author Julie Olson
 * a02363064
 */

import java.lang.Runtime;
 
 public class Assign2 {
     /**
      * This class contains code to provide information about the users'
      * computer system, operating system, and JVM platform through a 
      * series of user provided command line arguments.
      */
     public static void main(String[] args) {
         /**
          * This method checks that the user input contains at least one 
          * argument. If so, it will call the checkArgs method to validate
          * the argument(s). If not, the program will do nothing.
          */
         if (args.length >= 1){
             checkArgs(args, 0);
         }
     }
 
     static void checkArgs(String[] args, int zero) {
         /**
          * this method will check the given argument(s) for valid 
          * information requests. If more than one category was requested, 
          * address the first request and then re-process the remaining 
          * arguments.
          */
          Runtime run = Runtime.getRuntime();
         switch (args[zero]) {
             // check that the first argument is a valid command
             case "-cpu":
                 try {
                     // find and report the number of physical and logical CPUs available
                     int num = run.availableProcessors();
                     System.out.println("Processors available\t: " + num);
                 } 
                 catch (NumberFormatException e) {
                     break;
                 }
                 break;
            case "-mem":
                try {
                    // find and report the available free, total, and max memory
                    long free = run.freeMemory();
                    long total = run.totalMemory();
                    long max = run.maxMemory();

                    System.out.println("Free Memory available\t: " + free);
                    System.out.println("Total Memory available\t: " + total);
                    System.out.println("Max Memory available\t: " + max);
                } 
                catch (NumberFormatException e) {
                    break;
                }
                break;
            case "-dirs":
                try {
                    // report the processors working directory and user's home directory
                    
                    String work = System.getProperty("user.dir");
                    String home = System.getProperty("user.home");
                    
                    System.out.println("Working Directory\t: " + work);
                    System.out.println("User Home Directory\t: " + home);
                } 
                catch (NumberFormatException e) {
                    break;
                }
                break;
                case "-os":
                try {
                    // report the name and version number of the user's operating system
                    String name = System.getProperty("os.name");
                    String version = System.getProperty("sun.arch.data.model"); 
                    
                    
                    System.out.println("OS Name\t\t\t: " + name);
                    System.out.println("OS Version\t\t: " + version);
                } 
                catch (NumberFormatException e) {
                    break;
                }
                break;
            case "-java":
                try {
                    // report the java vendor, runtime name, version, 
                    // VM version and VM name
                    String vendor = System.getProperty("java.vendor");
                    String version = System.getProperty("java.version");
                    String runtime = System.getProperty("java.runtime.name");
                    String vmVersion = System.getProperty("java.vm.version");
                    String vmName = System.getProperty("java.vm.name");
                    
                    System.out.println("Java Vendor\t\t: " + vendor);
                    System.out.println("Java Runtime\t\t: " + runtime);
                    System.out.println("Java Version\t\t: " + version);
                    System.out.println("Java VM Version\t\t: " + vmVersion);
                    System.out.println("Java VM Name\t\t: " + vmName);
                } 
                catch (NumberFormatException e) {
                    break;
                }
                break;
            default:
                break;
         }
 
         // check for additional arguments
         if (args.length > 1 && args.length > zero + 1) {
             checkArgs(args, zero + 1);
         }
        }
    }