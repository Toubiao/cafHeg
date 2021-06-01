package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.business.droit.EnfantDroit;
import ch.hearc.cafheg.business.droit.Parent;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AllocationServiceTest {

  private AllocationService allocationService;

  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;

  @BeforeEach
  void setUp() {
    allocataireMapper = Mockito.mock(AllocataireMapper.class);
    allocationMapper = Mockito.mock(AllocationMapper.class);

    allocationService = new AllocationService(allocataireMapper, allocationMapper);
  }

  @Test
  void findAllAllocataires_GivenEmptyAllocataires_ShouldBeEmpty() {
    Mockito.when(allocataireMapper.findAll("Geiser")).thenReturn(Collections.emptyList());
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertThat(all).isEmpty();
  }

  @Test
  void findAllAllocataires_Given2Geiser_ShouldBe2() {
    Mockito.when(allocataireMapper.findAll("Geiser"))
        .thenReturn(Arrays.asList(new Allocataire(new NoAVS("1000-2000"), "Geiser", "Arnaud"),
            new Allocataire(new NoAVS("1000-2001"), "Geiser", "Aurélie")));
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getNoAVS()).isEqualTo(new NoAVS("1000-2000")),
        () -> assertThat(all.get(0).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(0).getPrenom()).isEqualTo("Arnaud"),
        () -> assertThat(all.get(1).getNoAVS()).isEqualTo(new NoAVS("1000-2001")),
        () -> assertThat(all.get(1).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(1).getPrenom()).isEqualTo("Aurélie"));
  }

  @Test
  void findAllocationsActuelles() {
    Mockito.when(allocationMapper.findAll())
        .thenReturn(Arrays.asList(new Allocation(new Montant(new BigDecimal(1000)), Canton.NE,
            LocalDate.now(), null), new Allocation(new Montant(new BigDecimal(2000)), Canton.FR,
            LocalDate.now(), null)));
    List<Allocation> all = allocationService.findAllocationsActuelles();
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getMontant()).isEqualTo(new Montant(new BigDecimal(1000))),
        () -> assertThat(all.get(0).getCanton()).isEqualTo(Canton.NE),
        () -> assertThat(all.get(0).getDebut()).isEqualTo(LocalDate.now()),
        () -> assertThat(all.get(0).getFin()).isNull(),
        () -> assertThat(all.get(1).getMontant()).isEqualTo(new Montant(new BigDecimal(2000))),
        () -> assertThat(all.get(1).getCanton()).isEqualTo(Canton.FR),
        () -> assertThat(all.get(1).getDebut()).isEqualTo(LocalDate.now()),
        () -> assertThat(all.get(1).getFin()).isNull());
  }


  @Test
  void getParentDroitAllocation_GivenParent1Lucratif_ShouldReturnParent1() throws ParseException {
    Parent p1 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    Parent p2 = new Parent(false, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", true, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p1);
  }

  @Test
  void getParentDroitAllocation_GivenParent2Lucratif_ShouldReturnParent2() throws ParseException {
    Parent p1 = new Parent(false, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    Parent p2 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", true, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p2);
  }

  @Test
  void getParentDroitAllocation_GivenOnlyParent1Authority_ShouldReturnParent1()
      throws ParseException {
    Parent p1 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    Parent p2 = new Parent(true, "rue de salut", false, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", true, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p1);
  }

  @Test
  void getParentDroitAllocation_GivenOnlyParent2Authority_ShouldReturnParent2()
      throws ParseException {
    Parent p1 = new Parent(true, "rue de salut", false, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    Parent p2 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", true, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p2);
  }

  @Test
  void getParentDroitAllocation_GivenChildWithDivorcedParent1LivingWithChild_ShouldReturnParent1()
      throws ParseException {
    Parent p1 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    Parent p2 = new Parent(true, "rue du je me suis fait trompé", true,
        NumberFormat.getInstance().parse("5000"), "NE", false);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", false, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p1);
  }

  @Test
  void getParentDroitAllocation_GivenChildWithDivorcedParent2LivingWithChild_ShouldReturnParent2()
      throws ParseException {
    Parent p1 = new Parent(true, "rue du je me suis fait trompé", true,
        NumberFormat.getInstance().parse("5000"), "NE", false);
    Parent p2 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", false, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p2);
  }

  @Test
  void getParentDroitAllocation_GivenParent1WorkingInSameCantonChild_ShouldReturnParent1()
      throws ParseException {
    Parent p1 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    Parent p2 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "GE", false);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", true, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p1);

  }

  @Test
  void getParentDroitAllocation_GivenParent2WorkingInSameCantonChild_ShouldReturnParent2()
      throws ParseException {
    Parent p1 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "GE", false);
    Parent p2 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", true, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p2);

  }

  //                                         1 parent est salarié et l'autre pas -> parent 1 -> parent2
  //                                          2 parents indépendants -> parent1Big Salaire -> parentBig Salaire
  @Test
  void getParentDroitAllocation_GivenBothParentNotIndepedantParent1WithHigherSalary_ShouldReturnParent1()
      throws ParseException {
    Parent p1 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("6000"),
        "NE", false);
    Parent p2 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", true, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p1);
  }

  @Test
  void getParentDroitAllocation_GivenBothParentNotIndepedantParent2WithHigherSalary_ShouldReturnParent2()
      throws ParseException {
    Parent p1 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("6000"),
        "NE", false);
    Parent p2 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("7000"),
        "NE", false);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", true, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p2);
  }

  @Test
  void getParentDroitAllocation_GivenParent1Indepedant_ShouldReturnParent2() throws ParseException {
    Parent p1 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("6000"),
        "NE", true);
    Parent p2 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", true, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p2);
  }

  @Test
  void getParentDroitAllocation_GivenParent2Indepedant_ShouldReturnParent1() throws ParseException {
    Parent p1 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("6000"),
        "NE", false);
    Parent p2 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", true);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", true, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p1);
  }

  @Test
  void getParentDroitAllocation_GivenBothParentIndepedantParent1WithHigherSalary_ShouldReturnParent1()
      throws ParseException {
    Parent p1 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("6000"),
        "NE", true);
    Parent p2 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", true);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", true, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p1);
  }

  @Test
  void getParentDroitAllocation_GivenBothParentIndepedantParent2WithHigherSalary_ShouldReturnParent2()
      throws ParseException {
    Parent p1 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("6000"),
        "NE", true);
    Parent p2 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("8000"),
        "NE", true);
    EnfantDroit enfantDroit = new EnfantDroit("rue de salut", true, p1, p2, "NE");
    assertThat(allocationService.getParentDroitAllocation(enfantDroit)).isEqualTo(p2);
  }


}
