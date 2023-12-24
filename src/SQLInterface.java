import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*Import Statements*/

public class SQLInterface {

    private Connection connection = null;
    private Statement statment = null;

    public SQLInterface() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dream_ride", "root", "123456789");
            this.statment = connection.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    public ResultSet excecuteSelect(String column, String tableName, String whereCondition, String groupByColumn, String havingCondition, String orderByColumn){

        String query = "SELECT "+column+" FROM "+tableName;

        if(whereCondition != null){
            query += " WHERE "+whereCondition;
        }

        if(groupByColumn != null){
            query += " GROUP BY "+groupByColumn;
        }

        if(havingCondition != null){
            query += " HAVING "+havingCondition;
        }

        if(orderByColumn != null){
            query += " ORDER BY "+orderByColumn;
        }

        try {

            ResultSet result = statment.executeQuery(query);
            return result;

        } catch (SQLException e) {
            System.out.println("Class: SQLInterface Method: excecuteSelect");
        }
        
        return null;
    }
    
    public boolean excecuteInsert(String tableName, String insertValues){

        String query = "INSERT INTO "+tableName+" VALUES "+insertValues;
        
        try {
            
            statment.execute(query);

            return true;

        } catch (Exception e) {
            // System.out.println("Class: SQLInterface Method: excecuteInsert");
            return false;
        }
        
    }

    public boolean excecuteInsert(String tableName, String columnName, String insertValues){

        String query = "INSERT INTO "+tableName+" "+columnName+" VALUES "+insertValues;
        
        try {
            
            statment.execute(query);

            return true;

        } catch (Exception e) {
            // System.out.println("Class: SQLInterface Method: excecuteInsert");
            return false;
        }
        
    }
    
    public boolean excecuteUpdate(String tableName, String setString, String whereCondition){

        String query = "UPDATE "+tableName+" SET "+setString+" WHERE "+whereCondition;
        
        try {
            
            statment.execute(query);

            return true;

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Class: SQLInterface Method: excecuteUpdate");
            return false;
        }
    }
    
    public boolean excecuteDelete(String tableName, String whereCondition){

        String query = "DELETE FROM "+tableName+" WHERE "+whereCondition;
        
        try {
            
            statment.execute(query);

            return true;
        } catch (Exception e) {
            System.out.println("Class: SQLInterface Method: excecuteDelete");
        }

        return false;
    }

}