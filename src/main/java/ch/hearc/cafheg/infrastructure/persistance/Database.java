package ch.hearc.cafheg.infrastructure.persistance;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;
import javax.sql.DataSource;

public class Database {

  private static DataSource dataSource;
  private static ThreadLocal<Connection> connection = new ThreadLocal<>();

  public Database() {
  }

  static Connection getConnection() {
    return connection.get();
  }

  public static <T> T inTransaction(Supplier<T> inTransaction) {
    try {
      connection.set(dataSource.getConnection());
      return inTransaction.get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        connection.get().close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      connection.remove();
    }
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public void start() {
    System.out.println("Initializing datasource");
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:h2:mem:sample");
    config.setMaximumPoolSize(20);
    config.setDriverClassName("org.h2.Driver");
    dataSource = new HikariDataSource(config);
    System.out.println("Datasource initialized");
  }
}
