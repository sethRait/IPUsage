/**
 * Created by srait on 11/3/2015.
 */
import java.util.*;
import java.io.*;
public class Scan {

    public static final String IP_RANGE = "192.168.1.0";     //IP range for NMAP scan ex. 192.168.1.0
    private List<IP> active;
    private List<String> rawData;   //for debugging purposes only
    private Map<IP, String> dateRep;
    private List<String> date;

    /**
     * Initializes a new Scan object with the results of a current NMAP scan.
     * Initializes Date to today
     */
    public Scan(){
        active = new LinkedList<>();
        runCmd();
        Calendar cal = new GregorianCalendar();
        String currDate = cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.YEAR);
        date = new LinkedList<>();
        for(int i=0;i<active.size();i++)
            date.add(currDate);
    }

    /**
     * Initializes a new Scan object from the given list of IP addresses and date
     * @param active
     */
    public Scan(List<IP> active, List<String> date){
        this.active=active;
        this.date=date;
    }

    /**
     * runs the NMAP scan and places preliminary results in the rawData list,
     * then calls purge() to retrieve IP addresses from the list.
     */
    public void runCmd(){
        rawData = new LinkedList<>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("nmap -sn -n 129.64.58.0/24");
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line=null;
            while((line=input.readLine()) != null) {
                rawData.add(line);
            }
            purge();
        }
        catch(Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * gets IP addresses from rawData
     */
    public void purge(){
        List<String> duplicate = new LinkedList<>(rawData);
        Collections.sort(duplicate);
        int i=0;
        while(i<duplicate.size()){
            if(duplicate.get(i).contains("Host"))
                duplicate.remove(i);
            else
                i++;
        }
        trim(duplicate);
        int j=0;
        while(j<duplicate.size()){
            String s = duplicate.get(j);
            String ip = "";
            for(char c : s.toCharArray()){
                if(Character.isDigit(c))
                    ip += c;
                else if(c=='.')
                    ip +=".";
            }
            active.add(new IP(ip));
            j++;
        }
    }

    /**
     * Removes header and timestamp info
     * @param duplicate
     */
    private void trim(List<String> duplicate){
        duplicate.remove(0); //header
        duplicate.remove(0);  //header
        duplicate.remove(duplicate.size()-1);   //date
    }

    public int size(){
        return active.size();
    }

    /**
     * for testing purposes
     * @return rawData
     */
    public List<String> raw(){
        return rawData;
    }

    /**
     * Creates and returns a HashMap representation of the list, wherein the Key is the IP
     * and the Value is the Date associated with the IP.
     * if called on a newly created object, date will be today.
     * @return dateRep
     */
    public Map<IP, String> asMap(){
        dateRep = new TreeMap<>();
        Iterator<String> dateIt = date.iterator();
        for(IP ip : active)
            dateRep.put(ip, dateIt.next());
        return dateRep;
    }


    public String toString(){
        Iterator itr = active.iterator();
        String s="["+itr.next();
        while(itr.hasNext())
            s+=", "+itr.next();
        return s+"]";
    }

    public String toStringMap(){
        asMap();
        Iterator<IP> itr = dateRep.keySet().iterator();
        IP currIP = itr.next();
        String currDate = dateRep.get(currIP);
        String s="["+currIP+": "+currDate;
        while(itr.hasNext()) {
            currIP = itr.next();
            currDate = dateRep.get(currIP);
            s += ", " + currIP+": "+currDate;
        }
        return s+"]";
    }
}
