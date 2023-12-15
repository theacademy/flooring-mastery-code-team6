package dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Order {

    private int orderNumber;
    private String customerName;
    private LocalDate orderDate;
    private String state;
    private BigDecimal taxRate;
    private String productType;
    private BigDecimal area;
    private BigDecimal costPerSqFoot;
    private BigDecimal laborCostPerSqFoot;
    private BigDecimal materialCost;
    private BigDecimal tax;
    private BigDecimal total;

    private BigDecimal laborCost;

    public Order(int orderNumber, LocalDate orderDate, String customerName, String state, String productType, BigDecimal area) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.state = state;
        this.productType = productType;
        this.area = area;

    }


    // getters

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }


    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getState() {
        return state;
    }

    ;

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public String getProductType() {
        return productType;
    }


    public BigDecimal getArea() {
        return area;
    }

    public BigDecimal getCostPerSqFoot() {
        return costPerSqFoot;
    }

    ;

    public BigDecimal getLaborCostPerSqFoot() {
        return laborCostPerSqFoot;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTotal() {
        return total;
    }


    // setters

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public void setCostPerSqFoot(BigDecimal costPerSqFoot) {this.costPerSqFoot = costPerSqFoot;
    }

    public void setLaborCostPerSqFoot(BigDecimal laborCostPerSqFoot) {
        this.laborCostPerSqFoot = laborCostPerSqFoot;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    // material cost, labor cost, tax, total
    public void calculateOrderCosts() {

        materialCost = calculateMaterialCost(area, costPerSqFoot);
        laborCost = calculateLaborCost(area, laborCostPerSqFoot);
        tax = calculateTax(materialCost, laborCost, taxRate);
        total = calculateTotal(materialCost, laborCost, tax);

    }

    public BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSqFoot) {
        return area.multiply(costPerSqFoot);
    }

    public BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSqFoot) {
        return area.multiply(laborCostPerSqFoot);
    }
    public BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate) {
        return materialCost.multiply(laborCost).multiply((taxRate.divide(BigDecimal.valueOf(100))));
    }

    public BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax) {
        return materialCost.add(laborCost).add(tax);
    }


    public void printOrderInfo() {

        String orderInfo = "Order Numer: " + orderNumber
                + "\nOrder Date: " + orderDate
                + "\nCustomer Name: " + customerName
                + "\nState: " + this.state
                + "\nProduct Type: " + productType
                + "\nArea: " + area;


        System.out.println(orderInfo);

    }


    // other

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderNumber == order.orderNumber && Objects.equals(customerName, order.customerName) && Objects.equals(orderDate, order.orderDate) && Objects.equals(state, order.state) && Objects.equals(taxRate, order.taxRate) && Objects.equals(productType, order.productType) && Objects.equals(area, order.area) && Objects.equals(costPerSqFoot, order.costPerSqFoot) && Objects.equals(laborCostPerSqFoot, order.laborCostPerSqFoot) && Objects.equals(materialCost, order.materialCost) && Objects.equals(tax, order.tax) && Objects.equals(total, order.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, customerName, orderDate, state, taxRate, productType, area, costPerSqFoot, laborCostPerSqFoot, materialCost, tax, total);
    }


}
