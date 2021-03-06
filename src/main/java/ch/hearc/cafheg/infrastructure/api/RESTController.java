package ch.hearc.cafheg.infrastructure.api;

import static ch.hearc.cafheg.infrastructure.persistance.Database.inTransaction;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.AllocataireService;
import ch.hearc.cafheg.business.allocations.Allocation;
import ch.hearc.cafheg.business.allocations.AllocationService;
import ch.hearc.cafheg.business.droit.EnfantDroit;
import ch.hearc.cafheg.business.droit.Parent;
import ch.hearc.cafheg.business.versements.VersementService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {

  private final AllocationService allocationService;
  private final AllocataireService allocataireService;
  private final VersementService versementService;

  @Autowired
  public RESTController(AllocationService allocationService,
      AllocataireService allocataireService,
      VersementService versementService) {
    this.allocationService = allocationService;
    this.allocataireService = allocataireService;
    this.versementService = versementService;
  }


  /*
    // Headers de la requête HTTP doit contenir "Content-Type: application/json"
    // BODY de la requête HTTP à transmettre afin de tester le endpoint
    {
        "enfantResidence" : "Neuchâtel",
        "parent1Residence" : "Neuchâtel",
        "parent2Residence" : "Bienne",
        "parent1ActiviteLucrative" : true,
        "parent2ActiviteLucrative" : true,
        "parent1Salaire" : 2500,
        "parent2Salaire" : 3000
    }
     */
  @PostMapping("/droits/quel-parent")
  public Parent getParentDroitAllocation(@RequestBody EnfantDroit enfantDroit) {
    return inTransaction(() -> allocationService.getParentDroitAllocation(enfantDroit));
  }

  @GetMapping("/allocataires")
  public List<Allocataire> allocataires(
      @RequestParam(value = "startsWith", required = false) String start) {
    return inTransaction(() -> allocataireService.findAllAllocataires(start));
  }

  @GetMapping("/allocations")
  public List<Allocation> allocations() {
    return inTransaction(allocationService::findAllocationsActuelles);
  }

  @GetMapping("/allocations/{year}/somme")
  public BigDecimal sommeAs(@PathVariable("year") int year) {
    return inTransaction(() -> versementService.findSommeAllocationParAnnee(year).getValue());
  }

  @GetMapping("/allocations-naissances/{year}/somme")
  public BigDecimal sommeAns(@PathVariable("year") int year) {
    return inTransaction(
        () -> versementService.findSommeAllocationNaissanceParAnnee(year).getValue());
  }

  @GetMapping(value = "/allocataires/{allocataireId}/allocations", produces = MediaType.APPLICATION_PDF_VALUE)
  public byte[] pdfAllocations(@PathVariable("allocataireId") int allocataireId) {
    return inTransaction(() -> versementService.exportPDFAllocataire(allocataireId));
  }

  @GetMapping(value = "/allocataires/{allocataireId}/versements", produces = MediaType.APPLICATION_PDF_VALUE)
  public byte[] pdfVersements(@PathVariable("allocataireId") int allocataireId) {
    return inTransaction(() -> versementService.exportPDFVersements(allocataireId));
  }
}
