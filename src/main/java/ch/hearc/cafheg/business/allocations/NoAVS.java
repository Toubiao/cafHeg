package ch.hearc.cafheg.business.allocations;

import java.util.Objects;

public class NoAVS {

  public final String value;

  public NoAVS(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NoAVS noAVS = (NoAVS) o;
    
    if (noAVS.value == null) {
      return false;
    }
    return Objects.equals(value, noAVS.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
