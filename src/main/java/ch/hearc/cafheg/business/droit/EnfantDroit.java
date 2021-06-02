package ch.hearc.cafheg.business.droit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnfantDroit {

  String residance;
  Boolean parentsEnsemble;
  Parent parent1;
  Parent parent2;
  String canton;

}
