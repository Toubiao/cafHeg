package ch.hearc.cafheg.business.droit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Parent {
    Boolean activiteLucrative;
    String residence;
    Boolean aAutoriteTutelaire;
    Number salaire;
    String canton;
    Boolean independant;
}
