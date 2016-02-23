/**
 * Created by srait on 2/15/2016.
 */
import java.time.LocalDate;
import java.util.*;
import java.io.*;
public class Inactive30 {
    public static final LocalDate TODAY = LocalDate.now();
    public static final String FILE_NAME = "Inactive_30.txt";
    public static final String INPUT1 = IPData.FILE_NAME1;
    public static final String INPUT2 = IPData.FILE_NAME2;
    public static void main(String[] args) {
        try{
            File output = new File(IPData.FILE_LOCATION + "\\" + FILE_NAME);
            File input1 = new File(IPData.FILE_LOCATION + "\\" + INPUT1);
            File input2 = new File(IPData.FILE_LOCATION + "\\" + INPUT2);
            Map<IP, LocalDate> old = new TreeMap<>();
            parseInput(old, input1);
            parseInput(old, input2);
            writeFile(old, output);
        }catch(Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Writes a file containing all IP addresses with a date older than 30 days
     * @param old The map containing (IP, Date) tuples
     * @param output the output file to write the data
     * @throws FileNotFoundException
     */
    private static void writeFile(Map<IP, LocalDate> old, File output) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(output);
        Iterator<IP> it = old.keySet().iterator();
        IP curIP;
        LocalDate curDate;
        while(it.hasNext()){
            curIP = it.next();
            curDate = old.get(curIP);
            writer.println(curDate.format(Scan.formatter) + "\t" + curIP);
        }
        writer.close();
    }

    /**
     * Parses input files into Map of (IP, Date) pairs
     * @param input input file to parse
     * @throws FileNotFoundException
     */
    private static void parseInput(Map<IP, LocalDate> old, File input) throws FileNotFoundException {
        Scanner reader = new Scanner(input);
        LocalDate curDate;
        IP curIP;
        while(reader.hasNextLine()){
            while(reader.hasNext()) {
                curDate = LocalDate.parse(reader.next(), Scan.formatter);
                curIP = new IP(reader.next());
                if(curDate.isBefore(TODAY.minusDays(30)))
                    old.put(curIP, curDate);
            }
        }
    }
}