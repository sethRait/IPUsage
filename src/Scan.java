/**
 * Created by srait on 11/3/2015.
 */
import java.util.*;
import java.io.*;
import java.time.*;
import java.time.format.*;

public class Scan {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM'/'dd'/'yyyy");   //input and output format for dates
    private String IPRange;
    private List<IP> active;
    private List<String> rawData;   //for debugging purposes only
    private Map<IP, LocalDate> dateRep;
    private List<LocalDate> date;

    /**
     * Initializes a new Scan object with the results of a current NMAP scan.
     * Initializes Date to today
     */
    public Scan(String IPRange){
        this.IPRange = IPRange;
        active = new LinkedList<>();
        runCmd();
        LocalDate today = LocalDate.now();  //current date
        date = new LinkedList<>();
        for(int i=0;i<active.size();i++)
            date.add(today);
    }

    /**
     * Initializes a new Scan object from the given list of IP addresses and date
     * @param active IP addresses from a previous scan
     * @param date list of dates from a previous scan
     */
    public Scan(List<IP> active, List<LocalDate> date){
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
            Process pr = rt.exec("nmap -sn -n "+IPRange);
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
            if(duplicate.get(i).contains("Host")||duplicate.get(i).contains("MAC")) //For summery lines
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
     * Creates and returns a Map representation of the list, wherein the Key is the IP
     * and the Value is the Date associated with the IP.
     * if called on a newly created object, date will be today.
     * @return dateRep
     */
    public Map<IP, LocalDate> asMap(){
        dateRep = new TreeMap<>();
        Iterator<LocalDate> dateIt = date.iterator();
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
        LocalDate currDate = dateRep.get(currIP);
        String s="["+currIP+": "+currDate.format(formatter);
        while(itr.hasNext()) {
            currIP = itr.next();
            currDate = dateRep.get(currIP);
            s += ", " + currIP+": "+currDate.format(formatter);
        }
        return s+"]";
    }
}