package ch.hegarc.cafheg;

import static ch.hearc.cafheg.infrastructure.persistance.Database.inTransaction;
import static org.assertj.core.api.Assertions.assertThat;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.AllocataireService;
import ch.hearc.cafheg.business.allocations.NoAVS;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.Database;
import ch.hearc.cafheg.infrastructure.persistance.Migrations;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import java.sql.Connection;
import java.util.List;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AllocataireServiceTestIT {

  private AllocataireService allocataireService;

  private AllocataireMapper allocataireMapper;
  private VersementMapper versementMapper;
  Database database;

  @BeforeEach
  void setUp() {
    allocataireMapper = new AllocataireMapper();
    versementMapper = new VersementMapper();

    allocataireService = new AllocataireService(versementMapper, allocataireMapper);

    database = new Database();
    database.start();
    Migrations migrations = new Migrations(database, true);
    migrations.start();
  }

  @Test
  void deleteAllocaireById_Given5Allocataires_ShouldBe4AfterDelete() throws Exception {
    Database database = new Database();
    database.start();
    Migrations migrations = new Migrations(database, true);
    migrations.start();
    try (Connection connection = database.getDataSource().getConnection()) {
      IDatabaseConnection dbCnn = new DatabaseConnection(connection);
      IDataSet dataSet = new FlatXmlDataSetBuilder()
          .build(getClass().getClassLoader().getResourceAsStream("testDataAllocataireIT.xml"));
      DatabaseOperation.CLEAN_INSERT.execute(dbCnn, dataSet);
    }
    List<Allocataire> allocataires = inTransaction(
        () -> allocataireService.findAllAllocataires(null));
    assertThat(allocataires.size()).isEqualTo(5);

    inTransaction(() -> allocataireService.deleteAllocataireById(1L));

    allocataires = inTransaction(
        () -> allocataireService.findAllAllocataires(null));
    assertThat(allocataires.size()).isEqualTo(4);
  }

  @Test
  void modifyAllocaireById_GivenDifferentSurnameAndFirstname_ShouldBetrue() throws Exception {
    try (Connection connection = database.getDataSource().getConnection()) {
      IDatabaseConnection dbCnn = new DatabaseConnection(connection);
      IDataSet dataSet = new FlatXmlDataSetBuilder()
          .build(getClass().getClassLoader().getResourceAsStream("testDataAllocataireIT.xml"));
      DatabaseOperation.CLEAN_INSERT.execute(dbCnn, dataSet);
    }
    NoAVS avsno = new NoAVS("756.6698.5760.41");
    Allocataire alloc = new Allocataire(avsno, "LeBg", "Oussama");
    inTransaction(() -> allocataireService.updateAllocataireById(1L, alloc));
    Allocataire allocataire = inTransaction(() -> allocataireMapper.findById(1L));
    assertThat(allocataire.getNom()).isEqualTo("LeBg");

  }
}
