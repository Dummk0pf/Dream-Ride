## Project Description

This is a project to simulate a rental business, it contains interfaces for both admin and users, This is a project done to apply OOP Concepts

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

_Screen.java_ -> This is an interface for the admin and the borrower Screens

_AdminScreen.java_ -> This is the file that focuses on the admin side interface

_BorrowerScreen.java_ -> This is the file that focuses on the borrower interface, the ability to add vehicles to the cart and view all the available vehicles

_SQLInterface.java_ -> This is an interface between the database and the business logic, used to send SQL commands and recieve Data from the database

_Table.java_ -> This is the interface for displaying all the result tables
All the files with table in their names have similar methods and properties describing their names

_PaymentDetails.java_ -> This is the file that handles the payments between the admin and borrower

_Main.java_ -> This is driver code for the application and seperates the admin and borrower sides
