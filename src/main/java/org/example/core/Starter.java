package org.example.core;


import org.example.aspects.LogAspect;
import org.example.aspects.NoAopLog;
import org.example.aspects.ReplaceAop;
import org.example.core.sneak.Customer;
import org.example.core.sneak.CustomerException;

import java.time.LocalDateTime;

public class Starter {
    public static void main(String[] args) throws CustomerException {

        LogAspect.LOGGER.warn("Start!");
        Customer customer = new Customer(1, "aaAAA", 1.2, LocalDateTime.now());

        boolean isPrivilegedCustomer = customer.isPrivilegedCustomer(100.45);
        System.out.println(isPrivilegedCustomer);
        customer = getCustomer(234, "New One");
        Double res = getCustomerBalance(567);
        methodOne(customer);
        methodTwo(customer);
        System.out.println( doNotLogMePlease(3.1415));
        methodFour(customer);
    }

    public static void methodOne(Customer customer) {
        try {
            boolean isPrivilegedCustomer = customer.isPrivilegedCustomer(100.45);
            System.out.println(isPrivilegedCustomer);
        } catch (CustomerException e) {
            e.printStackTrace();
        }
    }

    public static void methodTwo(Customer customer) throws CustomerException {
        boolean isPrivilegedCustomer = customer.isPrivilegedCustomer(100.45);
        System.out.println(isPrivilegedCustomer);
    }

    public static Customer getCustomer(int id, String name) {
        return new Customer(id, name, 1.2, LocalDateTime.now());
    }

    @ReplaceAop
    public static Double getCustomerBalance(int id) {
        return new Customer(id, "name", 1.2, LocalDateTime.now()).getCustomerBalance();
    }

    @NoAopLog
    public static double doNotLogMePlease(double parameter)   {
        return parameter * 13.5;
    }

    public static void methodFour(Customer customer) throws CustomerException {
        customer.setCustomerActivated(null  );
        boolean isPrivilegedCustomer = customer.isPrivilegedCustomer(null);
        System.out.println(isPrivilegedCustomer);
    }
}
