package dao;

import dto.Product;
import dto.Tax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FlooringMasterProductDaoImpl implements FlooringMasterProductDao {

    private final String TAXRATE_FILE = "src\\Taxes.txt";
    private final String DELIMITER = ",";
    Map<String, Tax> taxes;

    public FlooringMasterProductDaoImpl() {
        this.taxes = new HashMap<>();
    }

    @Override
    public Map<String, Product> getAllProducts() {
        return null;
    }

    @Override
    public Map<String, Tax> getAllTaxRates() {
        loadFromTaxFile(); // load the information from the taxes file
        return taxes; // return the state and tax rates information
    }

    private void loadFromTaxFile() {

        try {
            File taxFile = new File(TAXRATE_FILE);
            Scanner sc = new Scanner(taxFile);

            // skip the first line - header
            if (sc.hasNextLine()) {
                sc.nextLine();
            }

            // go through the file
            while(sc.hasNextLine()) {

                Tax tax = unmarshallTax(sc.nextLine());
                taxes.put(tax.getStateName(), tax);

            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }


    // convert lines in the tax file to a tax object
    private Tax unmarshallTax(String taxLine) {

        // split the string into state abreviation, state, and tax rate
        String[] values = taxLine.split(DELIMITER);

        // store the values in a tax object and return it
        return new Tax(values[0], values[1], new BigDecimal(values[2]));

    }








}
