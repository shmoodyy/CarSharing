package carsharing;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        String filename = "defaultName";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-databaseFileName") && i < args.length - 1) {
                filename = args[i + 1];
            }
        }
        CarSharingDB.setDB_URL("./src/carsharing/db/" + filename);
        new CarSharingUI();
    }
}