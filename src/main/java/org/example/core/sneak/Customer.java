package org.example.core.sneak;

import java.time.LocalDateTime;

public class Customer {
    private Integer id;
    private String customerName;
    private Double customerBalance;
    private LocalDateTime customerActivated;

    public boolean isPrivilegedCustomer(
            Double requiredBalance) throws CustomerException {
        if (this.customerActivated == null) {
            throw new CustomerException("Customer was not activated");
        }
        return this.customerBalance >= requiredBalance &&
                this.customerActivated.isBefore(LocalDateTime.now().minusYears(10));
    }

    public Customer(Integer id, String customerName, Double customerBalance, LocalDateTime customerActivated) {
        this.id = id;
        this.customerName = customerName;
        this.customerBalance = customerBalance;
        this.customerActivated = customerActivated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getCustomerBalance() {
        return customerBalance;
    }

    public void setCustomerBalance(Double customerBalance) {
        this.customerBalance = customerBalance;
    }

    public LocalDateTime getCustomerActivated() {
        return customerActivated;
    }

    public void setCustomerActivated(LocalDateTime customerActivated) {
        this.customerActivated = customerActivated;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", customerBalance=" + customerBalance +
                ", customerActivated=" + customerActivated +
                '}';
    }
}
