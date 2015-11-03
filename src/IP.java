/**
 * Created by srait on 11/3/2015.
 */
public class IP implements Comparable<IP>{
    private String address;
    private int lastOctet;

    public IP(String address){
        this.address=address;
        lastOctet=address.lastIndexOf("."+1,address.length());
    }

    @Override
    public int compareTo(IP o) {
        return this.lastOctet-o.lastOctet;
    }

    public boolean equals(IP o){
        return this.compareTo(o)==0;
    }

    public String toString(){
        return address;
    }
}
