/**
 * Created by srait on 2/15/2016.
 */
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
public class Inactive30 {
    public static final String FILE_NAME1 = "Inactive_30.txt";
    public static final String INPUT1 = "Active_IP_58.txt";
    public static final String INPUT2 = "Active_IP_90.txt";
    public static final String FILE_LOCATION = "C:\\Users\\srait\\Box Sync\\Public Domain\\Reports";
    public static void main(String[] args) {
        try{
            File output = new File(FILE_LOCATION + "\\" + FILE_NAME1);
            File input1 = new File(FILE_LOCATION + "\\" + INPUT1);
            File input2 = new File(FILE_LOCATION + "\\" + INPUT2);
            Map<IP, String> old = parseInput(input1, input2);
            writeFile(old, output);
        }catch(Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Writes a file containing all IP addresses with a date older than 30 days
     * @param old
     * @param output
     * @throws FileNotFoundException
     */
    private static void writeFile(Map<IP, String> old, File output) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(output);
        Iterator<IP> it = old.keySet().iterator();
        IP curIP;
        String curDate;
        while(it.hasNext()){
            curIP = it.next();
            curDate = old.get(curIP);
            writer.println(curDate + "\t" + curIP);
        }
        writer.close();
    }

    private static Map<IP, String> parseInput(File input1, File input2) throws FileNotFoundException {
        Map<IP, String> old = new TreeMap<>();
        Scanner reader = new Scanner(input1);
        String curDate;
        IP curIP;
        while(reader.hasNextLine()){
            while(reader.hasNext()) {
                curDate = reader.next();
                curIP = new IP(reader.next());
                if(isOld(curDate))
                    old.put(curIP, curDate);
            }
        }
        reader.close();
        Scanner reader2 = new Scanner(input2);
        while(reader2.hasNextLine()){
            while(reader2.hasNext()) {
                curDate = reader2.next();
                curIP = new IP(reader2.next());
                if(isOld(curDate))
                    old.put(curIP, curDate);
            }
        }
        reader2.close();
        return old;
    }

    private static boolean isOld(String curDate) {
        int div = curDate.indexOf("/");
        int div2 = curDate.lastIndexOf("/");
        int month1 = Integer.parseInt(curDate.substring(0, div));
        int day1 = Integer.parseInt(curDate.substring(div + 1, div2));
        int year1 = Integer.parseInt(curDate.substring(div2 + 1));

        String today = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        div = today.indexOf("/");
        div2 = today.lastIndexOf("/");
        int month2 = Integer.parseInt(today.substring(0,div));
        int day2 = Integer.parseInt(today.substring(div+1, div2));
        int year2 = Integer.parseInt(today.substring(div2+1));

        if(year1 < year2)
            return(!(year2 - year1 == 1 && month1 == 12 && month2 == 1));
        else{
            if(month1 == month2)
                return false;
            else if(month2 - month1 == 1)
                return day2 > day1;
            return true;
        }
    }
}