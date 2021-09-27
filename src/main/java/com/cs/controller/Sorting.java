package com.cs.controller;

import java.util.*;

public class Sorting {

    public static void main(String args[]) {
        Employee e1 = new Employee(1, "Stuti", "Singh");
        Employee e2 = new Employee(1, "Anamika", "Singh");
        Employee e3 = new Employee(1, "Ruby", "Singh");

        List<Employee> listOfEmployees = Arrays.asList(e1, e2, e3);

        Collections.sort(listOfEmployees , (o1,o2) -> o1.getFirstName().compareTo(o2.getFirstName()));

        System.out.println(listOfEmployees);
    }

}
