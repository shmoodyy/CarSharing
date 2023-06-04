package carsharing.dao;

import carsharing.CarSharingDB;
import carsharing.models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements DAO<Customer> {

    @Override
    public Customer get(int id) throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        Customer customer = null;
        String psSQL = "SELECT * FROM customer WHERE id = ?;";
        PreparedStatement ps = conn.prepareStatement(psSQL);

        ps.setInt(1, id);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            String customerName = resultSet.getString("name");
            customer = new Customer(customerName);
            int customerID = resultSet.getInt("id");
            customer.setId(customerID);
            int rentedCarID = resultSet.getInt("rented_car_id");
            customer.setRentedCarID(rentedCarID);
        }

        CarSharingDB.closeResultSet(resultSet);
        CarSharingDB.closePreparedStatement(ps);
        CarSharingDB.closeConnection(conn);

        return customer;
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        List<Customer> customerList = new ArrayList<>();
        Statement cs = conn.createStatement();

        String sql = "SELECT * FROM customer;";
        ResultSet resultSet = cs.executeQuery(sql);
        while (resultSet.next()) {
            String customerName = resultSet.getString("name");
            Customer customer = new Customer(customerName);
            int customerID = resultSet.getInt("id");
            customer.setId(customerID);
            int rentedCarID = resultSet.getInt("rented_car_id");
            customer.setRentedCarID(rentedCarID);
            customerList.add(customer);
        }

        CarSharingDB.closeResultSet(resultSet);
        CarSharingDB.closeStatement(cs);
        CarSharingDB.closeConnection(conn);

        return customerList;
    }

    @Override
    public int save(Customer customer) throws SQLException {
        return 0;
    }

    @Override
    public int insert(Customer customer) throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        String insertSQL = "INSERT INTO customer (name, rented_car_id) VALUES (?, NULL);";
        PreparedStatement ps = conn.prepareStatement(insertSQL);
        ps.setString(1, customer.getName());

        int result = ps.executeUpdate();

        CarSharingDB.closePreparedStatement(ps);
        CarSharingDB.closeConnection(conn);

        return result;
    }

    @Override
    public int update(Customer customer) throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        String updateSQL = "UPDATE customer SET rented_car_id = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(updateSQL);

        if (customer.getRentedCarID() != 0) {
            ps.setInt (1, customer.getRentedCarID());
        } else {
            ps.setNull(1, customer.getRentedCarID());
        }

        ps.setInt (2, customer.getId());

        int result = ps.executeUpdate();

        CarSharingDB.closePreparedStatement(ps);
        CarSharingDB.closeConnection(conn);

        return result;
    }

    @Override
    public int delete(Customer customer) throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        String sql = "DELETE FROM customer WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, customer.getId());

        int result = ps.executeUpdate();

        CarSharingDB.closePreparedStatement(ps);
        CarSharingDB.closeConnection(conn);

        return result;
    }
}