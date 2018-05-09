import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Runs the IPData NMAP scan tool and the Inactive merge tool.
 * Usage:
 * java Run path/to/output/dir [-i <#>] <subnet 1> [<subnet 2> ... <subnet n>]
 * Where items in square brackets are optional and -i is the number of days to record an inactive IP address.
 * The default for this is 30 days.
 */
public class Run {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) { // Need at least an output directory and one subnet to scan.
            showHelp();
            return;
        }

        String outputDir = args[0];
        int inactiveDays = getInactiveDays(args); // Parse the requested value for inactivity.

        List<String> subnets = new LinkedList<>();

        if (args[1].equals("-i")) {
            subnets.addAll(Arrays.asList(args).subList(3, args.length));
        }
        else {
            subnets.addAll(Arrays.asList(args).subList(1, args.length));
        }

        List<File> logs = IPData.orderData(outputDir, subnets);
        Inactive.merge(inactiveDays, outputDir, logs);
    }

    private static int getInactiveDays(String[] args) {
        int inactiveDays = 30;
        if (args[1].equals("-i")) {
            try {
                inactiveDays = Integer.parseInt(args[2]);
            } catch (Exception e) {  // This can be either a NumberFormatException or an IndexOutOfBoundsException.
                System.err.println("If using -i flag, must specify a number of days");
                showHelp();
                System.exit(0);
            }
        }
        if (inactiveDays < 1) {
            System.err.println("Please enter a valid number of days.");
            showHelp();
            System.exit(0);
        }
        return inactiveDays;
    }

    /**
     * Prints a help dialog to show correct usage for the program.
     */
    private static void showHelp() {
        System.err.println("Usage: A space separated list whose first element is the directory in which to" +
                "store output files, and all other elements are subnets to scan.  Example:\n" +
                "<output dir> <subnet1> <subnet2> ... <subnet n>\n" +
                "Where <output dir> is the PWD to store teh output file and each subnet is of the form a.b.c.d/e\n");
    }
}
