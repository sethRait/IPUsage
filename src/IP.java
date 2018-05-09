class IP implements Comparable<IP> {
    private String address;

    IP(String address) {
        this.address=address;
    }

    //integer representation of the first octet of the given IP address
    private int firstOctet() {
        return Integer.parseInt(address.substring(0, address.indexOf(".")));
    }

    //integer representation of the second octet of the given IP address
    private int secondOctet() {
        String center = address.substring(address.indexOf(".")+1, address.lastIndexOf("."));
        return middle(1, center);
    }

    //integer representation of the third octet of the given IP address
    private int thirdOctet() {
        String center = address.substring(address.indexOf(".")+1, address.lastIndexOf("."));
        return middle(2, center);
    }

    //integer representation of the last octet of the given IP address
    private int lastOctet() {
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
        if(this.firstOctet() != o.firstOctet())
            return this.firstOctet() - o.firstOctet();
        else if(this.secondOctet() != o.secondOctet())
            return this.secondOctet() - o.secondOctet();
        else if(this.thirdOctet() != o.thirdOctet())
            return this.thirdOctet() - o.thirdOctet();
        else if(this.lastOctet() != o.lastOctet())
            return this.lastOctet() - o.lastOctet();
        else return 0;
    }

    public boolean equals(IP o){
        return(this.lastOctet() == o.lastOctet()&&this.secondOctet() == o.secondOctet()&&this.thirdOctet()==o.thirdOctet()&&this.lastOctet()==o.lastOctet());
    }

    public String toString(){
        return address;
    }
}