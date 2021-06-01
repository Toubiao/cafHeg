package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import ch.qos.logback.classic.Logger;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllocataireService {

  private static final Logger logger
      = (Logger) LoggerFactory.getLogger(AllocationService.class);

  private final VersementMapper versementMapper;
  private final AllocataireMapper allocataireMapper;

  @Autowired
  public AllocataireService(
      VersementMapper versementMapper,
      AllocataireMapper allocataireMapper) {
    this.versementMapper = versementMapper;
    this.allocataireMapper = allocataireMapper;
  }

  public boolean deleteAllocataireById(Long id) {
    logger.info("Suppression allocataire");
    if (hasAllocataireAlreadyVersement(id)) {
      boolean resultDeleteOP = allocataireMapper.deleteById(id);
      logger.info("Allocataire supprimé avec succès ? " + (resultDeleteOP ? "oui"
          : "non"));
      return resultDeleteOP;
    } else {
      logger.info("Impossible de supprimé allocataire avec versement");
      return false;
    }
  }

  private boolean hasAllocataireAlreadyVersement(long id) {
    return versementMapper.findVersementParentEnfant().stream()
        .filter(v -> v.getParentId() == id)
        .collect(Collectors.toList()).isEmpty();
  }

  public boolean updateAllocataireById(Long id, Allocataire allocataire) {
    logger.info("Modification allocataire");
    return allocataireMapper.updateById(id, allocataire);
  }


}
