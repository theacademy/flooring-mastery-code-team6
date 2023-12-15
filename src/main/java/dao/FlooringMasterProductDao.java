package dao;

import dto.Product;
import dto.Tax;

import java.util.Map;

public interface FlooringMasterProductDao {

    Map<String, Product> getAllProducts();

    Map<String, Tax> getAllTaxRates();



}
