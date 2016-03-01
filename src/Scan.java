/**
 * Created by srait on 11/3/2015.
 */
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.time.*;
import java.time.format.*;

public class Scan {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM'/'dd'/'yyyy");   //input and output format for dates
    private String IPRange;
    private List<IP> active;
    private Map<IP, LocalDate> dateRep;
    private List<LocalDate> dates;

    /**
     * Initializes a new Scan object with the results of a current NMAP scan.
     * Initializes Date to today
     */
    public Scan(String IPRange){
        this.IPRange = IPRange;
        active = new LinkedList<>();
        runCmd();
        dates = new LinkedList<>();
        dates.add(LocalDate.now());  //current date
    }

    /**
     * Initializes a new Scan object from the given list of IP addresses and date
     * @param active IP addresses from a previous scan
     * @param dates list of dates from a previous scan
     */
    public Scan(List<IP> active, List<LocalDate> dates){
        this.active=active;
        this.dates=dates;
    }

    /**
     * runs the NMAP scan and places preliminary results in the rawData list,
     * then calls purge() to retrieve IP addresses from the list.
     */
    public void runCmd(){
        active = new LinkedList<>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("nmap -sn -n "+IPRange);
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            Pattern p = Pattern.compile("(?:[0-9]{1,3}\\.){3}[0-9]{1,3}");  //pattern for matching IP addresses
            Matcher matcher;
            String line;
            while((line=input.readLine()) != null) {
                matcher = p.matcher(line);
                if(matcher.find())
                    active.add((new IP(matcher.group())));
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    public int size(){
        return active.size();
    }

    /**
     * Creates and returns a Map representation of the list, wherein the Key is the IP
     * and the Value is the Date associated with the IP.
     * if called on a newly created object, date will be today.
     * @return dateRep
     */
    public Map<IP, LocalDate> asMap(){
        dateRep = new TreeMap<>();
        if(dates.size()==1)
            for(IP ip : active)
                dateRep.put(ip, dates.get(0));
        else{
            Iterator<LocalDate> dateIter = dates.iterator();
            for(IP ip : active)
                dateRep.put(ip, dateIter.next());
        }
        return dateRep;
    }

    public String toString(){
        Iterator itr = active.iterator();
        String s="["+itr.next();
        while(itr.hasNext())
            s+=", "+itr.next();
        return s+"]";
    }
}