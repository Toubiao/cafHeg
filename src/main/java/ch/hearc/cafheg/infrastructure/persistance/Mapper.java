package ch.hearc.cafheg.infrastructure.persistance;

import java.sql.Connection;

public class Mapper {
  protected Connection getConnection() {
    return Database.getConnection();
  }
}
