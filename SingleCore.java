import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SingleCore { 
    /* 
        1. Overall Execution Cycle (different core will complete at different cycles;
        report the maximum value across all cores) for the entire trace as well as
        execution cycle per core
        2. Number of compute cycles per core. These are the total number of cycles
        spent processing other instructions between load/store instructions
        3. Number of load/store instructions per core
        4. Number of idle cycles (these are cycles where the core is waiting for the
        request to the cache to be completed) per core
        5. Data cache hit and miss counts for each core
        6. Amount of Data traffic in bytes on the bus (this is due to bus read, bus read
        exclusive, bus writeback, and bus update transactions). Only include the
        traffic for data and not for address. Thus invalidation requests do not
        contribute to the data traffic.
        7. Number of invalidations or updates on the bus
        8. Distribution of accesses to private data versus shared data (for example,
        access to modified state is private, while access to shared state is shared data)
     */
    public static void main(String[] args) {
        String protocol;
        File inputFile; 
        int cacheSize;
        int associativity;
        int blockSize;
        // Types of instructions 
        if(args.length == 6) { 
            protocol = args[0] + args[1];
            inputFile = new File(args[2]); 
            cacheSize = Integer.parseInt(args[3]);
            associativity = Integer.parseInt(args[4]);
            blockSize = Integer.parseInt(args[5]);

            try {
                Scanner myReader = new Scanner(inputFile);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    // Use to count the stats as well 
                    System.out.println(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } else { 
            System.err.println("Error: Not enough arguments in program");
        }

    }
}