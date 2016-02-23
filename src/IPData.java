/**
 * Created by srait on 11/3/2015.
 */
import java.text.ParseException;
import java.util.*;
import java.io.*;
import java.time.*;
public class IPData {
    //Change global vars according to subnet and file location desired
    public static final LocalDate TODAY = LocalDate.now();
    public static final String FILE_LOCATION = "C:\\Reports";    //location to save output file
    public static final String FILE_NAME1 = "loc_1";     //output file name
    public static final String FILE_NAME2 = "loc_2";


    public static void main(String[] args) throws Exception {
        String IPRange1 = "192.168.1.0/24";   //move these to global vars
        String IPRange2 = "10.1.10.0/24";
        File output1 = new File(FILE_LOCATION+"\\"+FILE_NAME1);
        File output2 = new File(FILE_LOCATION+"\\"+FILE_NAME2);

        //first subnet
        if(!output1.exists())
            createFile(output1, IPRange1);
        else{
            Scan old = readFile(output1);
            Map<IP, LocalDate> newData=process(old, IPRange1);
            writeFile(newData, output1);
        }

        //second subnet
        if(!output2.exists())
            createFile(output2, IPRange2);
        else{
            Scan old = readFile(output2);
            Map<IP, LocalDate> newData=process(old, IPRange2);
            writeFile(newData, output2);
        }
        System.out.println(TODAY.format(Scan.formatter));
    }

    /**
     * Create a new file to begin data collection.  By default, all possible IP addresses are listed as being active on TODAY
     * @param output the output file for holding old scan data
     * @throws FileNotFoundException
     */
    public static void createFile(File output, String IPRange) throws FileNotFoundException{
        PrintWriter write = new PrintWriter(output);
        String header = IPRange.substring(0,IPRange.lastIndexOf(".")+1);
        for(int i=1;i<=255;i++)
            write.println("01/01/1900"+"\t"+header+i);
        write.close();
    }

    /**
     * Read from the log file
     * @param output the output file for holding old scan data
     */
    public static Scan readFile(File output)throws FileNotFoundException {
        List<LocalDate> dates = new LinkedList<>();
        List<IP> addresses = new LinkedList<>();
        Scanner reader = new Scanner(output);
        reader.useDelimiter("[\t\r\n]+");
        while(reader.hasNext()){
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
     public static Map<IP, LocalDate> process(Scan old, String IPRange) throws ParseException {
        Scan cur = new Scan(IPRange);  //current IP usage
        Map<IP, LocalDate> newData = cur.asMap();
        Map<IP, LocalDate> oldData = old.asMap();
         System.out.println(newData);
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
    public static Map<IP, LocalDate> process(Map<IP, LocalDate> big, Map<IP, LocalDate> little) throws ParseException {
        Iterator<IP> littleIt = little.keySet().iterator();
        while(littleIt.hasNext()){
            IP currentIP = littleIt.next();
            if(big.containsKey(currentIP)){     //if the larger map contains the specified IP
                if((big.get(currentIP)).isBefore(TODAY)) //if the date in big is before the date in little
                    big.put(currentIP, TODAY);  //update the date for the IP in the large map
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
    public static void writeFile(Map<IP, LocalDate> newData, File output)throws FileNotFoundException{
        PrintWriter writer = new PrintWriter(output);
        Iterator<IP> it = newData.keySet().iterator();
        IP curIP;
        LocalDate curDate;
        curIP = it.next();
        curDate = newData.get(curIP);
        while(it.hasNext()){
            writer.println(curDate.format(Scan.formatter) + "\t" + curIP);
            curIP = it.next();
            curDate = newData.get(curIP);
        }
        writer.print(curDate.format(Scan.formatter) + "\t" + curIP);
        writer.close();
    }
}