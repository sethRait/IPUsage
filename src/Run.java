import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Run {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) { // Need at least an output directory and one subnet to scan.
            showHelp();
            return;
        }
        String outputDir = args[0];
        List<String> subnets = new LinkedList<>();
        subnets.addAll(Arrays.asList(args).subList(1, args.length));

        List<File> logs = IPData.orderData(outputDir, subnets);
        Inactive30.merge(outputDir, logs);
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
