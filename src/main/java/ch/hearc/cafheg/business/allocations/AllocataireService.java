package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.versements.VersementService;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllocataireService {

  private static final Logger logger
      = (Logger) LoggerFactory.getLogger(AllocationService.class);

  private final VersementService versementService;
  private final AllocataireMapper allocataireMapper;

  @Autowired
  public AllocataireService(
      VersementService versementService,
      AllocataireMapper allocataireMapper) {
    this.versementService = versementService;
    this.allocataireMapper = allocataireMapper;
  }

  public boolean deleteAllocataireById(Long id) {
    logger.info("Suppression allocataire");
    if (versementService.hasAllocataireAlreadyVersement(id)) {
      boolean resultDeleteOP = allocataireMapper.deleteById(id);
      logger.info("Allocataire supprimé avec succès ? " + (resultDeleteOP ? "oui"
          : "non"));
      return resultDeleteOP;
    } else {
      logger.info("Impossible de supprimé allocataire avec versement");
      return false;
    }
  }

  public boolean updateAllocataireById(Long id, Allocataire allocataire) {
    logger.info("Modification allocataire");
    return allocataireMapper.updateById(id, allocataire);
  }


}
