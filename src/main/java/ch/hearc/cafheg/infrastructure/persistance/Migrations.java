package ch.hearc.cafheg.infrastructure.persistance;

import org.flywaydb.core.Flyway;

public class Migrations {

  private final Database database;
  private final boolean forTest;

  public Migrations(Database database) {
    this.database = database;
    this.forTest = false;
  }

  public void start() {
    System.out.println("Doing migrations");

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
    System.out.println("Migrations done");
  }

}
