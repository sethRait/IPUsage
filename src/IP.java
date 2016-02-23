/**
 * Created by srait on 11/3/2015.
 */
public class IP implements Comparable<IP>{
    private String address;

    public IP(String address){
        this.address=address;
    }

    //integer representation of the first octet of the given IP address
    public int firstOctet(){
        return Integer.parseInt(address.substring(0, address.indexOf(".")));
    }

    //integer representation of the second octet of the given IP address
    public int secondOctet(){
        String center = address.substring(address.indexOf(".")+1, address.lastIndexOf("."));
        return middle(1, center);
    }

    //integer representation of the third octet of the given IP address
    public int thirdOctet(){
        String center = address.substring(address.indexOf(".")+1, address.lastIndexOf("."));
        return middle(2, center);
    }

    //integer representation of the last octet of the given IP address
    public int lastOctet(){
        return Integer.parseInt(address.substring(address.lastIndexOf(".") + 1, address.length()));
    }

    //helper method for returning one of the inner two octets
    private int middle(int i, String center) {
        if(i==1)
            return Integer.parseInt(center.substring(0,center.indexOf(".")));
        return Integer.parseInt(center.substring(center.indexOf(".")+1));
    }

    @Override
    public int compareTo(IP o) {
        return this.lastOctet()-o.lastOctet();
    }

    public boolean equals(IP o){
        return(this.lastOctet() == o.lastOctet());
    }

    public String toString(){
        return address;
    }
}