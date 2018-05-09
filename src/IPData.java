import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

public class IPData {
    private static final LocalDate TODAY = LocalDate.now();

    /**
     * The entrypoint of the subnet scan.  Scans the provided list of subnets and reports which IP addresses
     * on those subnets are currently active (according to an NMAP scan).  One report is generated for each subnet.
     *
     * @param outputDir The directory to place the subnet reports.
     * @param subnets   A list of subnets to scan.
     * @return The file descriptor representing each created report.
     * @throws FileNotFoundException
     * @throws ParseException
     */
    static List<File> orderData(String outputDir, List<String> subnets) throws FileNotFoundException, ParseException {
        Map<String, File> outputs = new HashMap<>();
        subnets.forEach(sn -> outputs.put(sn, new File(outputDir + "\\" + subnetToFileName(sn)))); // Create file descriptors for each subnet.

        outputs.entrySet().forEach((kv) -> {
            try {
                IPData.createIfNotExists(kv); // If the file doesn't exist, create and pre-populate it.
            } catch (FileNotFoundException e) {
                System.err.println("There was a problem creating or accessing required files: " + e.toString());
            }
        });
        scanFiles(outputs);
        return new LinkedList<>(outputs.values());
    }

    private static void scanFiles(Map<String, File> outputs) throws FileNotFoundException, ParseException {
        for (Map.Entry<String, File> kv : outputs.entrySet()) {
            Scan old = readFile(kv.getValue());
            Map<IP, LocalDate> newData = process(old, kv.getKey());
            writeFile(newData, kv.getValue());
        }
    }

    private static void createIfNotExists(Map.Entry<String, File> kv) throws FileNotFoundException {
        if (!kv.getValue().exists()) {
            System.out.println("Creating file for " + kv.getKey());
            createFile(kv.getValue(), kv.getKey());
        } else {
            System.out.println("File for " + kv.getKey() + " already exists");
        }
    }

    /**
     * Create a new file to begin data collection.  By default, all possible IP addresses are listed as being active on 1/1/1900
     * @param output the output file for holding old scan data
     * @throws FileNotFoundException
     */
    private static void createFile(File output, String IPRange) throws FileNotFoundException {
        PrintWriter write = new PrintWriter(output);
        String header = IPRange.substring(0, IPRange.lastIndexOf(".") + 1);
        for (int i = 1; i <= 255; i++)
            write.println("01/01/1900" + "\t" + header + i);
        write.close();
    }

    /**
     * Read from the log file
     * @param output the output file for holding old scan data
     */
    private static Scan readFile(File output) throws FileNotFoundException {
        List<LocalDate> dates = new LinkedList<>();
        List<IP> addresses = new LinkedList<>();
        Scanner reader = new Scanner(output);
        reader.useDelimiter("[\t\r\n]+");
        while (reader.hasNext()) {
            dates.add(LocalDate.parse(reader.next(), Scan.formatter));
            addresses.add(new IP(reader.next()));
        }
        return new Scan(addresses, dates);
    }

    /**
     * Helper method
     * Compare new data and old data
     * @param old old scan data
     */
    private static Map<IP, LocalDate> process(Scan old, String IPRange) throws ParseException {
        Scan cur = new Scan(IPRange);  //current IP usage
        Map<IP, LocalDate> newData = cur.asMap();
        Map<IP, LocalDate> oldData = old.asMap();
        System.out.println(newData);
        return newData.size() > oldData.size() ? process(newData, oldData) : process(oldData, newData);
     }

    /**
     * Compare new and old data
     * @param big the larger of two Scan objects represented as a Map
     * @param little the smaller of two Scan objects represented as a Map
     */
    private static Map<IP, LocalDate> process(Map<IP, LocalDate> big, Map<IP, LocalDate> little) throws ParseException {
        little.keySet().stream().filter(big::containsKey) // If the larger map contains the specified IP
                .filter(currentIP -> (big.get(currentIP)).isBefore(TODAY)) // If the date in big is before the date in little
                .forEach(currentIP -> big.put(currentIP, TODAY)); // Update the date for the IP in the large map
        return big;
    }

    /**
     * Writes changes to output file.  Overwrites previous entries.
     * @param newData results from new scan and date checks
     * @param output the output file for holding old scan data
     * @throws FileNotFoundException
     */
    private static void writeFile(Map<IP, LocalDate> newData, File output) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(output);
        Iterator<IP> it = newData.keySet().iterator();
        IP curIP;
        LocalDate curDate;
        curIP = it.next();
        curDate = newData.get(curIP);
        while (it.hasNext()) {
            writer.println(curDate.format(Scan.formatter) + "\t" + curIP);
            curIP = it.next();
            curDate = newData.get(curIP);
        }
        writer.print(curDate.format(Scan.formatter) + "\t" + curIP);
        writer.close();
    }

    private static String subnetToFileName(String subnet) {
        StringBuilder sb = new StringBuilder();
        sb.append("subnet_");
        for (char c : subnet.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            } else if (c == '.') {
                sb.append('-');
            } else {
                sb.append('_');
            }
        }
        sb.append(".txt");
        return sb.toString();
    }
}