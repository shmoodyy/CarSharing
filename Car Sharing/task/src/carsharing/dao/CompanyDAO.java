package carsharing.dao;

import carsharing.CarSharingDB;
import carsharing.models.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO implements DAO<Company> {

    @Override
    public Company get(int id) throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        Company company = null;
        String psSQL = "SELECT * FROM company WHERE id = ?;";
        PreparedStatement ps = conn.prepareStatement(psSQL);

        ps.setInt(1, id);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            String companyName = resultSet.getString("name");
            company = new Company(companyName);
            int companyID = resultSet.getInt("id");
            company.setId(companyID);
        }

        CarSharingDB.closeResultSet(resultSet);
        CarSharingDB.closePreparedStatement(ps);
        CarSharingDB.closeConnection(conn);

        return company;
    }

    @Override
    public List<Company> getAll() throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        List<Company> companyList = new ArrayList<>();
        Statement cs = conn.createStatement();

        String sql = "SELECT * FROM company;";
        ResultSet resultSet = cs.executeQuery(sql);
        while (resultSet.next()) {
            String companyName = resultSet.getString("name");
            Company company = new Company(companyName);
            int companyID = resultSet.getInt("id");
            company.setId(companyID);
            companyList.add(company);
        }

        CarSharingDB.closeResultSet(resultSet);
        CarSharingDB.closeStatement(cs);
        CarSharingDB.closeConnection(conn);

        return companyList;
    }

    @Override
    public int save(Company company) throws SQLException {
        return 0;
    }

    @Override
    public int insert(Company company) throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        String insertSQL = "INSERT INTO company (name) VALUES (?);";
        PreparedStatement ps = conn.prepareStatement(insertSQL);

        ps.setString(1, company.getName());

        int result = ps.executeUpdate();

        CarSharingDB.closePreparedStatement(ps);
        CarSharingDB.closeConnection(conn);

        return result;
    }

    @Override
    public int update(Company company) throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        String updateSQL = "UPDATE company SET id = ?, name = ?;";
        PreparedStatement ps = conn.prepareStatement(updateSQL);

        ps.setInt (1, company.getId());

        int result = ps.executeUpdate();

        CarSharingDB.closePreparedStatement(ps);
        CarSharingDB.closeConnection(conn);

        return result;
    }

    @Override
    public int delete(Company company) throws SQLException {
        Connection conn = CarSharingDB.serverConnect();
        String sql = "DELETE FROM car WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, company.getId());

        int result = ps.executeUpdate();

        CarSharingDB.closePreparedStatement(ps);
        CarSharingDB.closeConnection(conn);

        return result;
    }
}