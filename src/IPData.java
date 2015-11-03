/**
 * Created by srait on 11/3/2015.
 */
import java.util.*;
import java.io.*;
public class IPData {

    public static final Calendar TODAY = new GregorianCalendar();
    public static final String DATE = TODAY.get(Calendar.MONTH)+"/"+TODAY.get(Calendar.DAY_OF_MONTH)+"/"+TODAY.get(Calendar.YEAR);
    public static final String FILE_LOCATION = "C:\\users\\srait\\box sync\\public domain\\reports";    //location to save output file
    public static final String FILE_NAME = "Active_IP_58.txt";


    public static void main(String[] args) throws FileNotFoundException {
        File output = new File(FILE_LOCATION+"\\"+FILE_NAME);
        if(!output.exists())
            createFile(output);
        //else

    }

    /**
     * Create a new file to begin data collection.  By default, all possible IP addresses are listed as being active on TODAY
     * @param output
     * @throws FileNotFoundException
     */
    public static void createFile(File output) throws FileNotFoundException{
        PrintWriter write = new PrintWriter(output);
        String header = Scan.IP_RANGE.substring(0,Scan.IP_RANGE.lastIndexOf(".")+1);
        for(int i=1;i<=255;i++){
            write.println(DATE+"\t"+header+i);
            System.out.println(DATE+"\t"+header+i);
        }
        write.close();
    }
}
