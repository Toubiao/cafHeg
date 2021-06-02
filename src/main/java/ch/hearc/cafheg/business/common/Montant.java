package ch.hearc.cafheg.business.common;

import java.math.BigDecimal;
import java.util.Objects;

public class Montant {

  public final BigDecimal value;

  public Montant(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Montant montant = (Montant) o;
    if (montant.value == null) {
      return false;
    }

    return Objects.equals(value, montant.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
