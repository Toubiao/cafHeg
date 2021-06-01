package ch.hearc.cafheg.infrastructure.persistance;

import ch.qos.logback.classic.Logger;
import org.flywaydb.core.Flyway;
import org.slf4j.LoggerFactory;

public class Migrations {

  private static final Logger logger
      = (Logger) LoggerFactory.getLogger(Migrations.class);

  private final Database database;
  private final boolean forTest;

  public Migrations(Database database, boolean forTest) {
    this.database = database;
    this.forTest = forTest;
  }

  public Migrations(Database database) {
    this.database = database;
    this.forTest = false;
  }

  public void start() {
    logger.debug("Doing migrations");

    String location;
    if (forTest) {
      location = "classpath:db/ddl";
    } else {
      location = "classpath:db";
    }

    Flyway flyway = Flyway.configure()
        .dataSource(database.getDataSource())
        .locations(location)
        .load();

    flyway.migrate();
    logger.debug("Migrations done");
  }

}
