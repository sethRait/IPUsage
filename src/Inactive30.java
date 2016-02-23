/**
 * Created by srait on 2/15/2016.
 */
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.io.*;
public class Inactive30 {
    public static final LocalDate TODAY = LocalDate.now();
    public static final String FILE_NAME1 = "";
    public static final String INPUT1 = "";
    public static final String INPUT2 = "";
    public static final String FILE_LOCATION = "C:\\Reports";
    public static void main(String[] args) {
        try{
            File output = new File(FILE_LOCATION + "\\" + FILE_NAME1);
            File input1 = new File(FILE_LOCATION + "\\" + INPUT1);
            File input2 = new File(FILE_LOCATION + "\\" + INPUT2);
            Map<IP, LocalDate> old = parseInput(input1, input2);
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
     * @param input1 first input file
     * @param input2 second input file
     * @return
     * @throws FileNotFoundException
     */
    private static Map<IP, LocalDate> parseInput(File input1, File input2) throws FileNotFoundException {
        Map<IP, LocalDate> old = new TreeMap<>();
        Scanner reader = new Scanner(input1);
        LocalDate curDate;
        IP curIP;
        while(reader.hasNextLine()){
            while(reader.hasNext()) {
                curDate = LocalDate.parse(reader.next(), Scan.formatter);
                curIP = new IP(reader.next());
                if(curDate.isAfter(TODAY.minusDays(30)))
                    old.put(curIP, curDate);
            }
        }

        //redundant.  Refactor this
        reader.close();
        Scanner reader2 = new Scanner(input2);
        while(reader2.hasNextLine()){
            while(reader2.hasNext()) {
                curDate = LocalDate.parse(reader2.next(), Scan.formatter);
                curIP = new IP(reader2.next());
                if(curDate.isAfter(TODAY.minusDays(30)))
                    old.put(curIP, curDate);
            }
        }
        reader2.close();
        return old;
    }
}