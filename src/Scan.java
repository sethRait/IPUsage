import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Scan {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM'/'dd'/'yyyy");   //input and output format for dates
    private String IPRange;
    private List<IP> active;
    private Map<IP, LocalDate> dateRep;
    private List<LocalDate> dates;

    /**
     * Initializes a new Scan object with the results of a current NMAP scan.
     * Initializes Date to today
     */
    Scan(String IPRange) {
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
    Scan(List<IP> active, List<LocalDate> dates) {
        this.active = active;
        this.dates = dates;
    }

    /**
     * Runs an NMAP scan on the specified subnet and parses the results for active IP addresses.
     * At the completion of this method, 'active' is a list of all the IP addresses which were active during the scan.
     * The scan used here does not do a full port scan, it only pings hosts.
     */
    private void runCmd() {
        active = new LinkedList<>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("nmap -sn -n " + IPRange);
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            Pattern p = Pattern.compile("(?:[0-9]{1,3}\\.){3}[0-9]{1,3}");  // Pattern for matching IP addresses
            Matcher matcher;
            String line;
            while ((line = input.readLine()) != null) {
                matcher = p.matcher(line);
                if (matcher.find())
                    active.add(new IP(matcher.group()));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Creates and returns a Map representation of the list, wherein the Key is the IP
     * and the Value is the Date associated with the IP.
     * if called on a newly created object, date will be today.
     * @return dateRep
     */
    Map<IP, LocalDate> asMap() {
        dateRep = new TreeMap<>();
        if (dates.size() == 1) {
            for (IP ip : active)
                dateRep.put(ip, dates.get(0));
        } else {
            Iterator<LocalDate> dateIter = dates.iterator();
            for (IP ip : active)
                dateRep.put(ip, dateIter.next());
        }
        return dateRep;
    }

    public String toString(){
        Iterator itr = active.iterator();
        String s = "[" + itr.next();
        while(itr.hasNext())
            s += ", " + itr.next();
        return s + "]";
    }
}