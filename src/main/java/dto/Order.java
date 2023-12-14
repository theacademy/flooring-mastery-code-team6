package dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Order {

    int orderNumber;
    String customerName;
    LocalDate orderDate;
    String state;
    BigDecimal taxRate;
    String productType;
    BigDecimal area;
    BigDecimal costPerSqFood;
    BigDecimal laborCostPerSqFoot;
    BigDecimal materialCost;
    BigDecimal tax;
    BigDecimal total;

    BigDecimal laborCost;

    public Order(LocalDate orderDate, String customerName, String state, String productType, BigDecimal area) {
        this.orderDate = orderDate;
        this.customerName=customerName;
        this.state=state;
        this.productType=productType;
        this.area=area;

        // a method that calculates total
        materialCost = calculateMaterialCost();
        laborCost = calculateLaborCost();
        tax = calculateTax();
        total = calculateTotal();

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

    public BigDecimal getCostPerSqFood() {
        return costPerSqFood;
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

    public void setCostPerSqFood(BigDecimal costPerSqFood) {
        this.costPerSqFood = costPerSqFood;
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



    // material cost, labor cost, tax, total

    public BigDecimal calculateMaterialCost() {

        return area.multiply(costPerSqFood);
    }

    public BigDecimal calculateLaborCost() {
        return area.multiply(laborCostPerSqFoot);
    }
    public BigDecimal calculateTax() {
        BigDecimal material = this.calculateMaterialCost();
        BigDecimal labor  = this.calculateLaborCost();

        return material.multiply(labor).multiply((taxRate.divide(BigDecimal.valueOf(100))));
    }

    public BigDecimal calculateTotal() {
        return materialCost.add(calculateLaborCost()).add(tax);
    }



    // other

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderNumber == order.orderNumber && Objects.equals(customerName, order.customerName) && Objects.equals(orderDate, order.orderDate) && Objects.equals(state, order.state) && Objects.equals(taxRate, order.taxRate) && Objects.equals(productType, order.productType) && Objects.equals(area, order.area) && Objects.equals(costPerSqFood, order.costPerSqFood) && Objects.equals(laborCostPerSqFoot, order.laborCostPerSqFoot) && Objects.equals(materialCost, order.materialCost) && Objects.equals(tax, order.tax) && Objects.equals(total, order.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, customerName, orderDate, state, taxRate, productType, area, costPerSqFood, laborCostPerSqFoot, materialCost, tax, total);
    }


}

