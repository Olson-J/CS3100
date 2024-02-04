/** 
 * Assignment 2 for CS3100
 * @author Julie Olson
 * a02363064
 */

 
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
         switch (args[zero]) {
             // check that the first argument is a valid command
             case "-cpu":
                 try {
                     // report the number of physical and logical CPUs available
                     findCPUs();
                 } 
                 catch (NumberFormatException e) {
                     break;
                 }
                 break;
            case "-mem":
                try {
                    // report the available free, total, and max memory
                    findMem();
                } 
                catch (NumberFormatException e) {
                    break;
                }
                break;
            case "-dirs":
                try {
                    // report the process working directory and user's home directory
                    findDirs();
                } 
                catch (NumberFormatException e) {
                    break;
                }
                break;
            case "-os":
                try {
                    // report the name and version number of the user's operating system
                    findOS();
                } 
                catch (NumberFormatException e) {
                    break;
                }
                break;
            case "-java":
                try {
                    // report the java vendor, runtime name, version, 
                    // VM version and VM name
                    findJavas();
                } 
                catch (NumberFormatException e) {
                    break;
                }
                break;
            default:
                break;
         }
 
         // check for additional arguments
         if (args.length > 1) {
             checkArgs(args, zero + 1);
         }
        }

        static void findCPUs() {
            /**
             * This method finds and prints a report of the number of
             * physical and logical CPUs available in the user's computer
             */
            
            // cool stuff here

            System.out.println("Processors available: \t" + num);
        }

        static void findMem() {
            /**
             * This method finds and prints a report of the available free, 
             * total, and max memory in the user's system
             */
            
            // cool stuff here

            System.out.println("Free Memory available: \t" + free);
            System.out.println("Total Memory available: \t" + total);
            System.out.println("Max Memory available: \t" + max);
        }

        static void findDirs() {
            /**
             * This method finds and prints a report of the process working
             * directory and the user's home directory
             */
            
            // cool stuff here

            System.out.println("Working Directory: \t" + work);
            System.out.println("User Home Directory: \t" + home);
        }

        static void findOS() {
            /**
             * This method finds and prints the user's operating system 
             * name and version
             */
            
            // cool stuff here

            System.out.println("OS Name: \t" + name);
            System.out.println("OS Version: \t" + version);
        }

        static void findJavas() {
            /**
             * This method finds and prints a report of the java vendor, 
             * runtime name, version, VM version and name on the user's
             * computer
             */
            
            // cool stuff here

            System.out.println("Java Vendor: \t" + vendor);
            System.out.println("Java Runtime: \t" + runtime);
            System.out.println("Java Version: \t" + version);
            System.out.println("Java VM Version: \t" + vmVersion);
            System.out.println("Java VM Name: \t" + vmName);
        }
     }