package ch.hearc.cafheg.business.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import ch.hearc.cafheg.business.allocations.AllocationService;
import ch.hearc.cafheg.business.droit.EnfantDroit;
import ch.hearc.cafheg.business.droit.Parent;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.text.NumberFormat;
import java.text.ParseException;

public class AllocationStepdefs {

  Parent parent1;
  Parent parent2;
  EnfantDroit enfant;
  AllocationService allocationService;
  AllocataireMapper allocataireMapper;
  AllocationMapper allocationMapper;

  @Given("Enfant avec deux parents")
  public void enfantAvecDeuxParents() throws ParseException {
    parent1 = new Parent(true, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    parent2 = new Parent(false, "rue de salut", true, NumberFormat.getInstance().parse("5000"),
        "NE", false);
    enfant = new EnfantDroit("rue de salut", true, parent1, parent2, "NE");
    allocationService = new AllocationService(allocataireMapper, allocationMapper);
  }

  @When("Le parent un a une activite lucrative {string}")
  public void leParentUnAUneActiviteLucrative(String arg0) {
    parent1.setActiviteLucrative(Boolean.valueOf(arg0));
  }

  @When("Les deux parents ont une activite lucrative")
  public void lesDeuxParentsOntUneActiviteLucrative() {
    parent1.setActiviteLucrative(true);
    parent2.setActiviteLucrative(true);
  }

  @And("Le parent deux a une activite lucrative {string}")
  public void leParentDeuxAUneActiviteLucrative(String arg0) {
    parent2.setActiviteLucrative(Boolean.valueOf(arg0));
  }

  @And("Parent un detient l'autorité parentale {string}")
  public void parentUnDetientLAutoritéParentale(String arg0) {
    parent1.setAAutoriteTutelaire(Boolean.valueOf(arg0));
  }

  @And("Parent deux detient l'autorité parentale {string}")
  public void parentDeuxDetientLAutoritéParentale(String arg0) {
    parent2.setAAutoriteTutelaire(Boolean.valueOf(arg0));
  }

  @And("Les deux parents ont l'autorité parentales")
  public void lesDeuxParentsOntLAutoritéParentales() {
    parent1.setAAutoriteTutelaire(true);
    parent2.setAAutoriteTutelaire(true);
  }

  @And("Les parents sont separés")
  public void lesParentsSontSeparés() {
    enfant.setParentsEnsemble(false);
  }

  @And("Parent un vit a l adresse {string}")
  public void parentUnVitALAdresse(String arg0) {
    parent1.setResidence(arg0);
  }

  @And("Parent deux vit a l adresse {string}")
  public void parentDeuxVitALAdresse(String arg0) {
    parent2.setResidence(arg0);
  }

  @And("l enfant vit a l adresse {string}")
  public void lEnfantVitALAdresse(String arg0) {
    enfant.setResidance(arg0);
  }

  @And("Les parents vivent ensemble")
  public void lesParentsViventEnsemble() {
    enfant.setParentsEnsemble(true);
  }

  @And("Parent un travail dans le canton {string}")
  public void parentUnTravailDansLeCanton(String arg0) {
    parent1.setCanton(arg0);
  }

  @And("Parent deux travail danse le canton {string}")
  public void parentDeuxTravailDanseLeCanton(String arg0) {
    parent2.setCanton(arg0);
  }

  @And("l enfant vit dans le canton {string}")
  public void lEnfantVitDansLeCanton(String arg0) {
    enfant.setCanton(arg0);
  }

  @And("Les deux parents ont l autorite parentales")
  public void lesDeuxParentsOntLAutoriteParentales() {
    parent1.setAAutoriteTutelaire(true);
    parent2.setAAutoriteTutelaire(true);
  }

  @And("Les parents ne sont pas indepandants")
  public void lesParentsNeSontPasIndepandants() {
    parent1.setIndependant(false);
    parent2.setIndependant(false);

  }

  @And("Parent un a le salaire suivant {string}")
  public void parentUnALeSalaireSuivant(String arg0) throws ParseException {
    parent1.setSalaire(NumberFormat.getInstance().parse(arg0));
  }

  @And("Parent deux a le salaire suivant {string}")
  public void parentDeuxALeSalaireSuivant(String arg0) throws ParseException {
    parent2.setSalaire(NumberFormat.getInstance().parse(arg0));
  }

  @And("Parent un est il indepandant {string}")
  public void parentUnEstIlIndepandant(String arg0) {
    parent1.setIndependant(Boolean.valueOf(arg0));
  }

  @And("Parent deux est il indepandant {string}")
  public void parentDeuxEstIlIndepandant(String arg0) {
    parent2.setIndependant(Boolean.valueOf(arg0));
  }

  @And("Les parents sont indepandants")
  public void lesParentsSontIndepandants() {
    parent1.setIndependant(true);
    parent2.setIndependant(true);
  }

  @Then("le parent {string} a le droit a l allocation")
  public void leParentALeDroitALAllocation(String arg0) {
    assertThat(allocationService.getParentDroitAllocation(enfant))
        .isEqualTo((arg0.equals("parent1") ? parent1 : parent2));
  }
}
