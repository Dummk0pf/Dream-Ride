import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jakewharton.fliptables.FlipTable;

public interface Table extends Screen{

    default boolean displayTable(ResultSet resultSet) throws SQLException{

        if (resultSet == null) {
            System.out.println("resultSet == null");
            return false;
        }
        if (!resultSet.isBeforeFirst()) {
            // System.out.println("Result set not at first.");
            return false;
        }

        List<String> headers = new ArrayList<>();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        int columnCount = resultSetMetaData.getColumnCount();
        for (int column = 0; column < columnCount; column++) {
            headers.add(resultSetMetaData.getColumnName(column + 1));
        }

        List<String[]> data = new ArrayList<>();
        while (resultSet.next()) {
            String[] rowData = new String[columnCount];
            for (int column = 0; column < columnCount; column++) {
                String dataRow = resultSet.getString(column + 1);
                if(dataRow == null){
                    dataRow = "-";
                }
                rowData[column] = dataRow;
            }
            data.add(rowData);
        }

        String[] headerArray = headers.toArray(new String[headers.size()]);
        String[][] dataArray = data.toArray(new String[data.size()][]);

        System.out.println(FlipTable.of(headerArray, dataArray));

        return true;
    }    
} 
