package chapter21;

import org.junit.jupiter.api.Test;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestDatabaseConnection {

    //For everything to work, we need the corresponding jar for the database we want to use
    //For this added maven and postgres dependency

    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/companies?user=postgres&password=admin";

    @Test
    public void createConnection() {

        try(Connection con = DriverManager.getConnection(DATABASE_URL)) {
            System.out.println("connection successful " + con);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void createConnectionPassingCredentialsAsMethodAttributes() {
        final var databaseUrl = "jdbc:postgresql://localhost:5432/filings";

        try(Connection con = DriverManager.getConnection(databaseUrl,"companies", "admin")) {
            System.out.println("connection successful " + con);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testPreparedStatement() {
        try(Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM public.balance_sheet"
            );
            System.out.println("prepared statement " + preparedStatement.getConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testPreparedStatementExecuteUpdate() {
        final var cik = "3214214142";
        try(Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            PreparedStatement preparedStatementUpdate = connection.prepareStatement(
                    "UPDATE public.company SET city = 'chisinau' WHERE cik = '1232121'"
            );

            PreparedStatement preparedStatementInsert = connection.prepareStatement(
                    "INSERT INTO public.company(cik, symbol) VALUES('" + cik +"', 'iadubsia')"
            );

            PreparedStatement preparedStatementDelete = connection.prepareStatement(
                    "DELETE FROM public.company WHERE cik = '" + cik + "'"
            );

            assertEquals(0, preparedStatementUpdate.executeUpdate());
            assertEquals(1, preparedStatementInsert.executeUpdate());
            assertEquals(1, preparedStatementDelete.executeUpdate());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testPreparedStatementExecuteQuery() {

        try(Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            PreparedStatement prepareStatement = conn.prepareStatement("SELECT * FROM public.company");
            ResultSet resultSet = prepareStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testPreparedStatementExecuteMethodWithQuery() {

        try(Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            PreparedStatement queryStatement = conn.prepareStatement("SELECT * FROM public.company");
            boolean isResultSet = queryStatement.execute();

            if (isResultSet) {
                ResultSet resultSet = queryStatement.getResultSet();
                System.out.println("Is result set");
            } else {
                System.out.println("Should not reach this");
                fail();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testPreparedStatementExecuteMethodWithUpdate() {

        try(Connection conn = DriverManager.getConnection(DATABASE_URL)) {

            PreparedStatement updateStatement = conn.prepareStatement(
                    "UPDATE public.company SET city = 'chisinau' WHERE cik = '1232121'"
            );

            if (updateStatement.execute()) {
                System.out.println("Will not reach this");
                fail();
            } else {
                int updateCount = updateStatement.getUpdateCount();
                assertEquals(0, updateCount);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testPreparedStatementExecuteMethodUsedIncorrectly() {

        try(Connection conn = DriverManager.getConnection(DATABASE_URL)) {

            PreparedStatement updateStatement = conn.prepareStatement(
                    "UPDATE public.company SET city = 'chisinau' WHERE cik = '1232121'"
            );

            boolean isResultSet = updateStatement.execute();
            assertFalse(isResultSet);
            assertThrows(SQLException.class, () -> updateStatement.executeQuery());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testPreparedStatementWithBindVariables() {

        int value = new Random().nextInt(1000);

        try(Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            PreparedStatement updateStatement = conn.prepareStatement(
                    "INSERT INTO public.company(cik,symbol) VALUES(?, ?)"
            );

            updateStatement.setLong(1, value);
            updateStatement.setString(2, "companySymbol");
            int rowsUpdated = updateStatement.executeUpdate();
            assertEquals(1, rowsUpdated);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void testResultSetMethod() {

        try(Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            PreparedStatement queryStatement = conn.prepareStatement("SELECT cik, symbol FROM public.company");
            boolean isResultSet = queryStatement.execute();

            if (isResultSet) {
                ResultSet resultSet = queryStatement.getResultSet();

                while(resultSet.next()) {
                    System.out.println(resultSet.getLong("cik"));
                    System.out.println(resultSet.getString("symbol"));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void testResultSetMethodExceptions() {
        try(Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            PreparedStatement queryStatement = conn.prepareStatement("SELECT cik, symbol FROM public.company");
            ResultSet resultSet = queryStatement.executeQuery();

            assertThrows(SQLException.class, () -> {
                //if next not called, counter wasn't moved
                resultSet.getLong("cik");
            });

            assertThrows(SQLException.class, () -> {
                while(resultSet.next()) {
                    //Counting starts from 1 not 0
                    resultSet.getLong(0);
                }
            });

            assertThrows(SQLException.class, () -> {
                while(resultSet.next()) {
                    //when providing column name, it should exist
                    resultSet.getLong("non existing column");
                }
            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
