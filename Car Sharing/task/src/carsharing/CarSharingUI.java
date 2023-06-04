package carsharing;

import carsharing.dao.CarDAO;
import carsharing.dao.CompanyDAO;
import carsharing.dao.CustomerDAO;
import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.models.Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CarSharingUI {

    Scanner scanner = new Scanner(System.in);
    final String MAIN_MENU = """
            1. Log in as a manager
            2. Log in as a customer
            3. Create a customer
            0. Exit""";
    final String MANAGER_MENU = """
            1. Company list
            2. Create a company
            0. Back""";
    final String CAR_MENU = """
            1. Car list
            2. Create a car
            0. Back""";
    final String CUSTOMER_MENU = """
            1. Rent a car
            2. Return a rented car
            3. My rented car
            0. Back""";
    boolean isExit = false;
    CompanyDAO companyDAO;
    CarDAO carDAO;
    CustomerDAO customerDAO;

    CarSharingUI() throws SQLException {
        CarSharingDB.createTables();
        companyDAO = new CompanyDAO();
        carDAO = new CarDAO();
        customerDAO = new CustomerDAO();
        mainMenu();
    }

    public void mainMenu() throws SQLException {
        while (!isExit) {
            System.out.println(MAIN_MENU);
            int mmOption = scanner.nextInt();
            System.out.println();
            switch (mmOption) {
                case 1 -> managerMenu();
                case 2 -> listCustomers();
                case 3 -> createCustomer();
                case 0 -> isExit = true;
            }
        }
        System.out.println("Goodbye!");
    }

    public void managerMenu() throws SQLException {
        boolean isBack = false;
        while (!isBack) {
            System.out.println(MANAGER_MENU);
            int managerOption = scanner.nextInt();
            switch (managerOption) {
                case 1 -> listCompanies(null);
                case 2 -> createCompany();
                case 0 -> isBack = true;
            }
        }
        System.out.println();
    }

    public void listCompanies(Customer customer) throws SQLException {
        if (customer != null && customer.getRentedCarID() != 0) {
            System.out.println("You've already rented a car!");
        } else {
            List<Company> companyList = companyDAO.getAll();
            if (!companyList.isEmpty()) {
                boolean isBack = false;
                while (!isBack) {
                    System.out.println("Choose the company:");
                    companyList.forEach((company) -> System.out.printf("%d. %s%n", company.getId(), company.getName()));
                    System.out.println("0. Back");
                    int companyOption = scanner.nextInt();
                    if (companyOption == 0) {
                        isBack = true;
                    } else {
                        Optional<Company> matchingCompany = companyList.stream()
                                .filter((company -> company.getId() == companyOption))
                                .findFirst();
                        if (matchingCompany.isPresent()) {
                            Company matchedCompany = matchingCompany.get();
                            if (customer != null) listCarsToRent(customer, matchedCompany);
                            else  carMenu(matchedCompany);
                            isBack = true;
                        }
                    }
                }
            } else {
                System.out.println("The company list is empty!\n");
            }
        }
    }

    public void createCompany() throws SQLException {
        scanner.nextLine();
        System.out.println("\nEnter the company name:");
        String inputCompanyName = scanner.nextLine();
        System.out.println(companyDAO.insert(new Company(inputCompanyName)) > 0
                ? "The company was created!\n" : "Failed to create company!\n");
    }

    public void carMenu(Company company) throws SQLException {
        System.out.printf("%n'%s' company%n", company.getName());
        boolean isBack = false;
        while (!isBack) {
            System.out.println(CAR_MENU);
            int carOption = scanner.nextInt();
            switch (carOption) {
                case 1 -> listCars(company.getId());
                case 2 -> createCar(company.getId());
                case 0 -> isBack = true;
            }
        }
    }

    public List<Car> listCars(int companyID) throws SQLException {
        List<Car> carList = carDAO.getAll();
        List<Customer> customerList = customerDAO.getAll();
        List<Car> companyCarList = new ArrayList<>();
        long carCount = carList.stream().filter(car -> car.getCompanyID() == companyID).count();
        if (carCount > 0) {
            int count = 0;
            System.out.println("Car list:");
            for (Car car : carList) {
                boolean carIsRented = customerList.stream().anyMatch(renter -> renter.getRentedCarID() == car.getId());
                if (car.getCompanyID() == companyID && !carIsRented) {
                    System.out.println(++count + ". " + car.getName());
                    companyCarList.add(car);
                }
            }
            System.out.println();
        } else System.out.println("The car list is empty!\n");
        return companyCarList;
    }

    public void createCar(int companyID) throws SQLException {
        scanner.nextLine();
        System.out.println("\nEnter the car name:");
        String inputCarName = scanner.nextLine();
        System.out.println(carDAO.insert(new Car(inputCarName, companyID)) > 0
                ? "The car was created!\n" : "Failed to create car!\n");
    }

    public void listCustomers() throws SQLException {
        List<Customer> customerList = customerDAO.getAll();
        if (!customerList.isEmpty()) {
            boolean isBack = false;
            while (!isBack) {
                System.out.println("Choose a customer:");
                customerList.forEach((customer) -> System.out.printf("%d. %s%n", customer.getId(), customer.getName()));
                System.out.println("0. Back");
                int customerOption = scanner.nextInt();
                if (customerOption == 0) isBack = true;
                else {
                    Optional<Customer> matchingCustomer = customerList.stream()
                            .filter((customer -> customer.getId() == customerOption))
                            .findFirst();
                    if (matchingCustomer.isPresent()) {
                        Customer matchedCustomer = matchingCustomer.get();
                        customerMenu(matchedCustomer);
                        isBack = true;
                    }
                }
            }
        } else System.out.println("The customer list is empty!\n");
    }

    public void createCustomer() throws SQLException {
        scanner.nextLine();
        System.out.println("\nEnter the customer name:");
        String inputCustomerName = scanner.nextLine();
        System.out.println(customerDAO.insert(new Customer(inputCustomerName)) > 0
                ? "The customer was added!\n" : "Failed to add customer!\n");
    }

    public void customerMenu(Customer customer) throws SQLException {
        System.out.printf("%n'%s' customer%n", customer.getName());
        boolean isBack = false;
        while (!isBack) {
            System.out.println(CUSTOMER_MENU);
            int carOption = scanner.nextInt();
            System.out.println();
            switch (carOption) {
                case 1 -> listCompanies(customer);
                case 2 -> returnRental(customer);
                case 3 -> myRental(customer.getRentedCarID());
                case 0 -> isBack = true;
            }
            System.out.println();
        }
    }

    public void listCarsToRent(Customer customer, Company company) throws SQLException {
        List<Car> carList = carDAO.getAll();
        if (!carList.isEmpty()) {
            boolean isBack = false;
            while (!isBack) {
                List<Car> carsToRent = listCars(company.getId());
                System.out.println("0. Back");
                int rentalCarOption = scanner.nextInt();
                if (rentalCarOption == 0) isBack = true;
                else {
                    Optional<Car> matchingCar = carsToRent.stream()
                            .filter(car -> carsToRent.indexOf(car) == rentalCarOption - 1)
                            .findFirst();
                    if (matchingCar.isPresent()) {
                        Car matchedCar = matchingCar.get();
                        customer.setRentedCarID(matchedCar.getId());
                        if (customerDAO.update(customer) > 0) {
                            System.out.printf("You rented '%s'%n", carsToRent.get(rentalCarOption - 1).getName());
                            isBack = true;
                        }
                    }
                }
            }
            System.out.println();
        } else System.out.printf("No available cars in the '%s' company.%n", company.getName());
    }

    public void returnRental(Customer customer) throws SQLException {
        if (customer.getRentedCarID() == 0) {
            System.out.println("You didn't rent a car!\n");
        } else {
            customer.setRentedCarID(0);
            if (customerDAO.update(customer) > 0) System.out.println("You've returned a rented car!\n");
        }
    }

    public void myRental(int rentedCarID) throws SQLException {
        if (rentedCarID == 0) {
            System.out.println("You didn't rent a car!");
        } else {
            Car myRentalCar = carDAO.get(rentedCarID);
            Company myRentalCompany = companyDAO.get(myRentalCar.getCompanyID());
            System.out.printf("Your rented car:%n%s%nCompany:%n%s%n", myRentalCar.getName(), myRentalCompany.getName());
        }
    }
}