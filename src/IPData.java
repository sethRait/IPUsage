/**
 * Created by srait on 11/3/2015.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
public class IPData {

    public static final Calendar TODAY = new GregorianCalendar();
    public static final String DATE = TODAY.get(Calendar.MONTH)+"/"+TODAY.get(Calendar.DAY_OF_MONTH)+"/"+TODAY.get(Calendar.YEAR);
    public static final String FILE_LOCATION = "C:\\users\\temp";    //location to save output file
    public static final String FILE_NAME = "Active_IP.txt";     //output file name


    public static void main(String[] args) throws Exception {
        File output = new File(FILE_LOCATION+"\\"+FILE_NAME);
        if(!output.exists())
            createFile(output);
        else{
            Scan old = readFile(output);
            Map<IP, String> newData=process(old);
            writeFile(newData, output);
        }

    }

    /**
     * Create a new file to begin data collection.  By default, all possible IP addresses are listed as being active on TODAY
     * @param output the output file for holding old scan data
     * @throws FileNotFoundException
     */
    public static void createFile(File output) throws FileNotFoundException{
        PrintWriter write = new PrintWriter(output);
        String header = Scan.IP_RANGE.substring(0,Scan.IP_RANGE.lastIndexOf(".")+1);
        for(int i=1;i<=255;i++)
            write.println(DATE+"\t"+header+i);
        write.close();
    }

    /**
     * Read from the log file
     * @param output the output file for holding old scan data
     */
    public static Scan readFile(File output)throws FileNotFoundException {
        List<String> dates = new LinkedList<>();
        List<IP> addresses = new LinkedList<>();
        Scanner reader = new Scanner(output);
        reader.useDelimiter("[\t\r\n]+");
        while(reader.hasNext()){
            dates.add(reader.next());
            addresses.add(new IP(reader.next()));
        }
        return new Scan(addresses, dates);
    }

    /**
     * Helper method
     * Compare new data and old data
     * @param old old scan data
     */
     public static Map<IP, String> process(Scan old) throws ParseException {
        Scan cur = new Scan();  //current IP usage
        Map<IP, String> newData = cur.asMap();
        Map<IP, String> oldData = old.asMap();
        if(newData.size()>oldData.size())
            return(process(newData, oldData));
        else
            return(process(oldData, newData));
     }

    /**
     * Compare new and old data
     * @param big the larger of two Scan objects represented as a Map
     * @param little the smaller of two Scan objects represented as a Map
     */
    public static Map<IP, String> process(Map<IP, String> big, Map<IP, String> little) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Iterator<IP> littleIt = little.keySet().iterator();
        while(littleIt.hasNext()){
            IP currentIP = littleIt.next();
            if(big.containsKey(currentIP)){     //if the larger map contains the specified IP
                if(sdf.parse(big.get(currentIP)).before(sdf.parse(little.get(currentIP)))) //if the date in big is before the date in little
                    big.put(currentIP, DATE);  //update the date for the IP in the large map
            }
        }
        return big;
    }

    /**
     * Writes changes to output file.  Overwrites previous entries.
     * @param newData results from new scan and date checks
     * @param output the output file for holding old scan data
     * @throws FileNotFoundException
     */
    public static void writeFile(Map<IP, String> newData, File output)throws FileNotFoundException{
        PrintWriter writer = new PrintWriter(output);
        Iterator<IP> it = newData.keySet().iterator();
        IP curIP;
        String curDate;
        while(it.hasNext()){
            curIP = it.next();
            curDate = newData.get(curIP);
            writer.println(curDate + "\t" + curIP);
        }
        writer.close();
    }
}