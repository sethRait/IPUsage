# IPUsage
Java applications for running IP usage reports on a series of /24 subnet.

Given an output directory and a list of subnets to scan, the program will first generate one file for each subnet
(if the file does not already exist) with a listing of whether each IP address on the given subnet was active
at the time of the scan.  If the file already exists, it will be updated to reflect the most recent scan.
Next, each file will be merged into a single-file report listing all the IP addresses on each
subnet which have been inactive for more than 30 days.

## Installation:
- Install [NMAP](https://nmap.org/download.html) on your system.
- Install JRE 8
- Clone this repository.

## Usage:
```
java -jar C:\path\to\SubnetReporting.jar <Directory for log files> <subnet 1> <subnet 2> ... <subnet n>
```
Where ```<Directory for log files>``` is the directory in which you would like the program to store the generated files,
 and each ```<subnet n>``` is a subnet on which to run the NMAP scan in the form ```a.b.c.d/x```.

For example, the following command will scan two subnets and place the output files in ```C:\Users\foo\bar```:
```
java -jar C:\path\to\SubnetReporting.jar C:\Users\foo\bar 192.168.62.0/24 168.57.42.0/24
```
This will produce three output files:

- C:\Users\foo\bar\subnet_192-168-62-0_24.txt
- C:\Users\foo\bar\subnet_168-57-42-0_24.txt
- C:\Users\foo\bar\Inactive_30.txt
