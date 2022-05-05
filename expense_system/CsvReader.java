package expense_system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The CsvReader class loads CSV file header and data into an instance.
 */
public class CsvReader {
    
    // fields
    private String filepath;
    private String[] header;
    private List<String[]> data = new ArrayList<>();

    // constructor
    public CsvReader(String filepath) throws IOException {
        this.filepath = filepath;
        scanCsv(filepath);
    }
    
    // getters
    public String getFilepath() {
        return this.filepath;
    }
    public String[] getHeader() {
        return this.header;
    }
    public List<String[]> getData() {
        return this.data;
    }
    
    // other methods
    public void addDataRow(String[] row) {
        this.data.add(row);
    }

    public void scanCsv(String filepath) throws IOException {
        BufferedReader reader = null;
        String line = "";
        
        try {
            reader = new BufferedReader(new FileReader(filepath));
            // Set a counter so that we can treat the header differently
            int counter = 0;
            // While loop - runs while we haven't reached the end of the file
            while((line = reader.readLine()) != null) {
                
                String[] row = line.split(",");

                if (counter == 0) {
                    // Add the String array to the header attribute
                    this.header = row;
                }

                else {
                    // If not the header, add the array to the data List
                    this.addDataRow(row);
                }
                
                // Increment the counter
                counter += 1;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            reader.close();
        }
    }

    public static void main(String[] args) throws IOException {

        // Demonstrate making a new CsvReader
        CsvReader csv  = new CsvReader("Teams.csv");
        // Print the instance's path
        System.out.println(csv.getFilepath());
        // Print the header data
        System.out.println(Arrays.toString(csv.getHeader()));
        // Iterate and print the row data
        for (String[] dataRow : csv.getData()) {
            System.out.println(Arrays.toString(dataRow));
        }
    }

}