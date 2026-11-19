# Java Inventory Management System

Core Java + OOP + Collections + JDBC + MySQL project matching the Inventory Management project described in the CV.

## Features
- Add, view, search, update and delete products
- Product stock quantity and stock-value summary
- MySQL persistence through JDBC
- ArrayList collection/cache
- Encapsulation through private fields/getters/setters
- Inheritance: ElectronicProduct and GroceryProduct extend Product
- Polymorphism through Product references and overridden methods
- Input validation and exception handling

## Setup
1. Install JDK 17+, Maven and MySQL.
2. Run `database/schema.sql` in MySQL.
3. Open `Connectivity.java` and change USER/PASSWORD.
4. Import as Maven project.
5. Run `InventoryApplication.java`.

Or run:
`mvn clean compile exec:java`
