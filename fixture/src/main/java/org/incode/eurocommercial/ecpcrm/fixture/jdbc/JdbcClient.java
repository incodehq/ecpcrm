package org.incode.eurocommercial.ecpcrm.fixture.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class JdbcClient {
    private Connection connection;
    private String databaseName;
    private String connectionUrl;
    private String userName;
    private String password;

    private static final String TIMESTAMP_FORMAT = "yyyyMMdd'T'HHmmss'000'";
    private static final String LOCALDATE_FORMAT = "yyyyMMdd";

    private static SimpleDateFormat timeStampFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
    ;
    private static SimpleDateFormat localDateFormat = new SimpleDateFormat(LOCALDATE_FORMAT);
    ;

    public static JdbcClient withDatabase(
            final String databaseName,
            final String userName,
            final String password) {

        return new JdbcClient(databaseName, userName, password);
    }

    private JdbcClient(
            final String databaseName,
            final String userName,
            final String password) {
        this.databaseName = databaseName;
        this.password = password;
        this.userName = userName;
        this.connectionUrl = "jdbc:mysql://localhost:3306/" + databaseName + "?profileSQL=true";
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = java.sql.DriverManager.getConnection(connectionUrl, userName, password);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error Trace in getConnection() : " + e.getMessage());
            }
        }
        return connection;
    }

    public void displayDbProperties() {
        java.sql.DatabaseMetaData dm = null;
        java.sql.ResultSet rs = null;
        try {
            Connection connection = this.getConnection();
            if (connection != null) {
                dm = connection.getMetaData();
                System.out.println("Driver Information");
                System.out.println("\tDriver Name: " + dm.getDriverName());
                System.out.println("\tDriver Version: " + dm.getDriverVersion());
                System.out.println("\nDatabase Information ");
                System.out.println("\tDatabase Name: " + dm.getDatabaseProductName());
                System.out.println("\tDatabase Version: " + dm.getDatabaseProductVersion());
                System.out.println("Avalilable Catalogs ");
                rs = dm.getCatalogs();
                while (rs.next()) {
                    System.out.println("\tcatalog: " + rs.getString(1));
                }
                rs.close();
                rs = null;
                closeConnection();
            } else
                System.out.println("Error: No active Connection");
        } catch (Exception e) {
            e.printStackTrace();
        }
        dm = null;
    }

    public void closeConnection() {
        try {
            if (connection != null)
                connection.close();
            connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List fromResultSet(final Class cls) {
        List<Object> objects = new ArrayList<>();
        Connection con = null;
        try {
            int count = 0;
            con = getConnection();
            final String tableName = cls.getSimpleName().replace("Import", "");
            final String SQL = String.format("SELECT * FROM `%s`", tableName);
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQL);
            java.sql.ResultSetMetaData rsmd = rs.getMetaData();

            Instant start = Instant.now();
            while (rs.next()) {
                try {
                    Object object = cls.newInstance();
                    int numColumns = rsmd.getColumnCount();
                    for (int i = 1; i < numColumns + 1; i++) {
                        String columnName = rsmd.getColumnName(i);
                        Object value = converted(rsmd, i, rs);
                        BeanUtils.setProperty(object, columnName, value);
                    }
                    objects.add(object);
                    count++;
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            Interval interval = new Interval(start, Instant.now());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return objects;
    }

    private static Object converted(ResultSetMetaData rsmd, int i, ResultSet rs) throws SQLException {
        final int columnType = rsmd.getColumnType(i);
        if (columnType == Types.ARRAY) {
            return rs.getArray(i);
        }
        if (columnType == Types.BIGINT || columnType == Types.INTEGER || columnType == Types.TINYINT || columnType == Types.SMALLINT) {
            return rs.getInt(i);
        }
        if (columnType == Types.BOOLEAN) {
            return rs.getBoolean(i);
        }
        if (columnType == Types.BLOB) {
            return rs.getBlob(i);
        }
        if (columnType == Types.DOUBLE || columnType == Types.FLOAT || columnType == Types.DECIMAL || columnType == Types.NUMERIC) {
            final BigDecimal bigDecimal = rs.getBigDecimal(i);
            return bigDecimal == null ? BigDecimal.ZERO : bigDecimal;
        }
        if (columnType == Types.NVARCHAR || columnType == Types.VARCHAR) {
            return rs.getString(i);
        }
        if (columnType == Types.DATE) {
            final Date date = rs.getDate(i);
            return date == null ? null : new LocalDate(date);
        }
        if (columnType == Types.TIMESTAMP) {
            final Timestamp timestamp = rs.getTimestamp(i);
            return timestamp == null ? null : new LocalDateTime(timestamp);
        }
        return rs.getObject(i);
    }

}
