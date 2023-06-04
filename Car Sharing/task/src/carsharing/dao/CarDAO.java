package carsharing.dao;

import carsharing.CarSharingDB;
import carsharing.models.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO implements DAO<Car> {

    @Override
    public Car get(int id) throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        Car car = null;
        String psSQL = "SELECT * FROM car WHERE id = ?;";
        PreparedStatement ps = conn.prepareStatement(psSQL);

        ps.setInt(1, id);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            String carName = resultSet.getString("name");
            int companyID = resultSet.getInt("company_id");
            car = new Car(carName, companyID);
            int carID = resultSet.getInt("id");
            car.setId(carID);
        }

        CarSharingDB.closeResultSet(resultSet);
        CarSharingDB.closePreparedStatement(ps);
        CarSharingDB.closeConnection(conn);

        return car;
    }

    @Override
    public List<Car> getAll() throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        List<Car> carList = new ArrayList<>();
        Statement cs = conn.createStatement();

        String sql = "SELECT * FROM car;";
        ResultSet resultSet = cs.executeQuery(sql);
        while (resultSet.next()) {
            String carName = resultSet.getString("name");
            int companyID = resultSet.getInt("company_id");
            Car car = new Car(carName, companyID);
            int carID = resultSet.getInt("id");
            car.setId(carID);
            carList.add(car);
        }

        CarSharingDB.closeResultSet(resultSet);
        CarSharingDB.closeStatement(cs);
        CarSharingDB.closeConnection(conn);

        return carList;
    }

    @Override
    public int save(Car car) throws SQLException {
        return 0;
    }

    @Override
    public int insert(Car car) throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        String insertSQL = "INSERT INTO car (name, company_id) VALUES (?, ?);";
        PreparedStatement ps = conn.prepareStatement(insertSQL);
        ps.setString(1, car.getName());
        ps.setInt(2, car.getCompanyID());

        int result = ps.executeUpdate();

        CarSharingDB.closePreparedStatement(ps);
        CarSharingDB.closeConnection(conn);

        return result;
    }

    @Override
    public int update(Car car) throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        String updateSQL = "UPDATE car SET company_id = ? WHERE id = ?;";
        PreparedStatement ps = conn.prepareStatement(updateSQL);

        ps.setInt (1, car.getCompanyID());
        ps.setInt (2, car.getId());

        int result = ps.executeUpdate();

        CarSharingDB.closePreparedStatement(ps);
        CarSharingDB.closeConnection(conn);

        return result;
    }

    @Override
    public int delete(Car car) throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        String sql = "DELETE FROM car WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, car.getId());

        int result = ps.executeUpdate();

        CarSharingDB.closePreparedStatement(ps);
        CarSharingDB.closeConnection(conn);

        return result;
    }
}