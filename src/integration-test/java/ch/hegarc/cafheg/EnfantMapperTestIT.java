package ch.hegarc.cafheg;

import static ch.hearc.cafheg.infrastructure.persistance.Database.inTransaction;
import static org.assertj.core.api.Assertions.assertThat;

import ch.hearc.cafheg.infrastructure.persistance.Database;
import ch.hearc.cafheg.infrastructure.persistance.EnfantMapper;
import ch.hearc.cafheg.infrastructure.persistance.Migrations;
import java.sql.Connection;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EnfantMapperTestIT {

  private EnfantMapper enfantMapper;


  Database database;

  @BeforeEach
  void setUp() {
    enfantMapper = new EnfantMapper();
    database = new Database();
    database.start();
    Migrations migrations = new Migrations(database, true);
    migrations.start();
  }

  @Test
  void findById_GivenChildWithId1_ShouldBeTrue() throws Exception {
    Database database = new Database();
    database.start();
    Migrations migrations = new Migrations(database, true);
    migrations.start();
    try (Connection connection = database.getDataSource().getConnection()) {
      IDatabaseConnection dbCnn = new DatabaseConnection(connection);
      IDataSet dataSet = new FlatXmlDataSetBuilder()
          .build(getClass().getClassLoader().getResourceAsStream("testDataAllocataireIT.xml"));
      DatabaseOperation.CLEAN_INSERT.execute(dbCnn, dataSet);
      assertThat(inTransaction(() -> enfantMapper.findById(1L).getPrenom().equals("Oussama")))
          .isTrue();
    }
  }

}
