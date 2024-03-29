package expense_system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The CsvReader class holds .csv file header and row data.
 */
public class Csv {
    
    // FIELDS
    private String filepath;
    private String[] header;
    private List<String[]> data = new ArrayList<>();

    // CONSTRUCTOR
    public Csv(String filepath) throws IOException {
        this.filepath = filepath;
        scanCsv(filepath);
    }

    // METHODS
    
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

    // setters
    public void setData(List<String[]> data) {
        this.data = data;
    }
    
    // other methods

    // A method to add a row of data
    public void addDataRow(String[] row) {
        this.data.add(row);
    }

    // A method to scan the file contents into the Csv class instance header and data attributes 
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

    // A method to write to a file
    // If we write to an existing file, then the file will be overwritten unless the FileWriter
    // 'append' flag is set to true
    public void writeCsv(String filepath) throws IOException {

        BufferedWriter writer = null;
        String line = "";
        
        try {
            writer = new BufferedWriter(new FileWriter(filepath));
            
            // Write the header
            line = String.join(",", this.header);
            writer.write(line);
            writer.newLine();

            // Iterate over the data rows
            for (String[] dataElements : this.data) {
                line = String.join(",", dataElements);
                writer.write(line);
                writer.newLine();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            writer.close();
        }
    }

    // A method to demonstrate usage
    public static void main(String[] args) throws IOException {

        // Demonstrate making a new CsvReader object
        Csv csv  = new Csv("Teams.csv");
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
