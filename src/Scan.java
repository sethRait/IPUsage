/**
 * Created by srait on 11/3/2015.
 */
import java.util.*;
import java.io.*;
public class Scan {

    public static final String IP_RANGE = "129.64.58.0";     //IP range for NMAP scan ex. 192.168.50.0
    private List<IP> active;
    private List<String> rawData;   //for debugging purposes only

    public Scan(){
        active = new LinkedList<>();
        runCmd();
    }
    public Scan(List<IP> active){
        this.active=active;
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

    public String toString(){
        Iterator itr = active.iterator();
        String s="["+itr.next();
        while(itr.hasNext())
            s+=", "+itr.next();
        return s+"]";
    }
}
