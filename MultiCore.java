import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import bus.Bus;
import cache.*;
import cpu.CPU;
import memory.DRAM;
import protocol.*;

public class MultiCore {
    public static void main(String[] args) {
        String protocolString;
        Protocol protocol;
        File inputFile;
        int cacheSize;
        int associativity;
        int blockSize;
        int numOfCores = 4;

        DRAM dram = new DRAM(100);
        Bus bus = new Bus(dram);
        protocol = new MESI();
        Cache[] caches = new Cache[numOfCores];
        CacheController[] controllers = new CacheController[numOfCores];
        CPU[] cpus = new CPU[numOfCores];

        System.out.println(args.length);
        for (String s : args) {
            System.out.println(s);
        }
        // Types of instructions
        if (args.length == 5) {
            protocolString = args[0];
            if(protocolString.equalsIgnoreCase("MESI")) {
                protocol = new MESI();
            } else if (protocolString.equalsIgnoreCase("Dragon")) {
                protocol = new Dragon();
            }
            cacheSize = Integer.parseInt(args[2]);
            associativity = Integer.parseInt(args[3]);
            blockSize = Integer.parseInt(args[4]);
        } else {
            protocol = new MESI();
            cacheSize = 4096; // Default cache size
            associativity = 2;
            blockSize = 32;
        }

        for(int i = 0; i < numOfCores; i++) {
            caches[i] = new Cache(cacheSize, blockSize, associativity, protocol, i);
            controllers[i] = new CacheController(caches[i], bus, protocol, i, dram);
            cpus[i] = new CPU(caches[i], i, controllers[i]);
            bus.registerCache(controllers[i]);
        }
        // Need to create 4 caches for each core - also need associate each cache with a core
        // file mangagement
        String[] extensions = { "_0.data", "_1.data", "_2.data", "_3.data" };
        // this can be bodytrack_four or fluidanimate_four
        String benchmarkFolder = "Benchmarks/" + args[1] + "_four/";
        for(int i = 0; i < numOfCores; i++) {
            String filePath = benchmarkFolder + args[1] + extensions[i];
            inputFile = new File(filePath);

            try {
                Scanner myReader = new Scanner(inputFile);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    // Use to count the stats as well
                    System.out.println(data);
                    cpus[i].executeInstruction(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        System.out.println("===== Simulation Results =====");
        for(int i = 0; i < numOfCores; i++) {
            cpus[i].reportStats(); // Report CPU statistics
        }

    }
}
