package ch.hearc.cafheg.business.allocations;

import static ch.hearc.cafheg.infrastructure.persistance.Database.inTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.Database;
import ch.hearc.cafheg.infrastructure.persistance.Migrations;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AllocataireServiceTest {

  private AllocataireService allocataireService;

  private AllocataireMapper allocataireMapper;
  private VersementMapper versementMapper;

  @BeforeEach
  void setUp() {
    allocataireMapper = Mockito.mock(AllocataireMapper.class);
    versementMapper = Mockito.mock(VersementMapper.class);

    allocataireService = new AllocataireService(versementMapper, allocataireMapper);
  }

  @Test
  void findAllAllocataires_GivenEmptyAllocataires_ShouldBeEmpty() {
    Mockito.when(allocataireMapper.findAll("Geiser")).thenReturn(Collections.emptyList());
    List<Allocataire> all = allocataireService.findAllAllocataires("Geiser");
    assertThat(all).isEmpty();
  }

  @Test
  void findAllAllocataires_Given2Geiser_ShouldBe2() {
    Mockito.when(allocataireMapper.findAll("Geiser"))
        .thenReturn(Arrays.asList(new Allocataire(new NoAVS("1000-2000"), "Geiser", "Arnaud"),
            new Allocataire(new NoAVS("1000-2001"), "Geiser", "Aurélie")));
    List<Allocataire> all = allocataireService.findAllAllocataires("Geiser");
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getNoAVS()).isEqualTo(new NoAVS("1000-2000")),
        () -> assertThat(all.get(0).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(0).getPrenom()).isEqualTo("Arnaud"),
        () -> assertThat(all.get(1).getNoAVS()).isEqualTo(new NoAVS("1000-2001")),
        () -> assertThat(all.get(1).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(1).getPrenom()).isEqualTo("Aurélie"));
  }

  @Test
  void deleteAllocaireById_Given2Geiser_ShouldBeSizeReduceBy1AfterDelete() throws Exception {
    Mockito.when(allocataireMapper.findAll("Geiser"))
        .thenReturn(Arrays.asList(new Allocataire(new NoAVS("1000-2000"), "Geiser", "Arnaud"),
            new Allocataire(new NoAVS("1000-2001"), "Geiser", "Aurélie")));
    List<Allocataire> all = allocataireService.findAllAllocataires("Geiser");
    allocataireService.deleteAllocataireById(1L);
  }

  @Test
  void modifyAllocaireById_GivenDifferentSurnameAndFirstname_ShouldBetrue() throws Exception {
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
    NoAVS avsno = new NoAVS("756.6698.5760.41");
    Allocataire alloc = new Allocataire(avsno, "LeBg", "Oussama");
    inTransaction(() -> allocataireService.updateAllocataireById(1L, alloc));
    Allocataire allocataire = inTransaction(() -> allocataireMapper.findById(1L));
    //assertThat(allocataire.getNom()).isEqualTo("LeBg");

  }

}
