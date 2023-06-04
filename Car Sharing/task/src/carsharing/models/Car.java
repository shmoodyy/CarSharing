package carsharing.models;

public class Car {
    private int id;
    private String name;
    private int companyID;

    public Car(String name, int companyID) {
        this.name = name;
        this.companyID = companyID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", companyID=" + companyID +
                '}';
    }
}