package controller;

public enum FileType {
    ORDER_NUMBER_TRACKER("orderNumberTracker.txt"),
    PRODUCT("Products.txt"),
    TAX("Taxes.txt"),
    ORDER("Orders");

    private final String fileName;

    FileType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
