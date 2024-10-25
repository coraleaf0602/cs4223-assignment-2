import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import bus.Bus;
import cache.*;
import cpu.CPU;
import memory.DRAM;

public class SingleCore {
    public static void main(String[] args) {
        String protocol;
        File inputFile;
        int cacheSize;
        int associativity;
        int blockSize;

        DRAM dram = new DRAM(10, 100);
        Bus bus = new Bus();
        Cache cache;
        CPU cpu;

        System.out.println(args.length);
        for (String s : args) {
            System.out.println(s);
        }
        // Types of instructions
        if (args.length == 5) {
            protocol = args[0];
            cacheSize = Integer.parseInt(args[2]);
            associativity = Integer.parseInt(args[3]);
            blockSize = Integer.parseInt(args[4]);

            cache = new Cache(cacheSize, blockSize, associativity, dram, bus);
            cpu = new CPU(cache);

            // file mangagement
            String[] extensions = { "_0.data", "_1.data", "_2.data", "_3.data" };
            // this can be bodytrack_four or fluidanimate_four
            String benchmarkFolder = "Benchmarks/blackscholes_four/";
            // this can be changed into a for loop
            String filePath = benchmarkFolder + args[1] + extensions[0];
            inputFile = new File(filePath);

            try {
                Scanner myReader = new Scanner(inputFile);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    // Use to count the stats as well
                    System.out.println(data);
                    cpu.executeInstruction(data);
                }
                myReader.close();
                System.out.println("===== Simulation Results =====");
                cpu.reportStats(); // Report CPU statistics

                /* 
                * 6. Amount of Data traffic in bytes on the bus (this is due to bus read, bus
                * read exclusive, bus writeback, and bus update transactions). Only include the
                * traffic for data and not for address. Thus invalidation requests do not
                * contribute to the data traffic.
                */
                System.out.println("Bus data traffic: " + bus.getDataTraffic() + " bytes");
                /* 
                 * 7. Number of invalidations or updates on the bus
                 * 8. Distribution of accesses to private data versus shared data (for example,
                 * access to modified state is private, while access to shared state is shared
                 * data)
                 */

            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } else {
            System.err.println("Error: Not enough arguments in program");
        }

    }
}

// coherence MESI blackscholes 1024 1 16