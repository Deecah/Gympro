package connectDB;

public interface DatabaseInfor {
    public static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String url = "jdbc:sqlserver://BANH\\SQLEXPRESS:1433;databaseName=Gympro;encrypt=false";
    public static String user = "sa";
    public static String pass = "12345";
}