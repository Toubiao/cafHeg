package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.droit.EnfantDroit;
import ch.hearc.cafheg.business.droit.Parent;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;

import java.util.List;

public class AllocationService {

  private static final String PARENT_1 = "Parent1";
  private static final String PARENT_2 = "Parent2";

  private final AllocataireMapper allocataireMapper;
  private final AllocationMapper allocationMapper;

  public AllocationService(
      AllocataireMapper allocataireMapper,
      AllocationMapper allocationMapper) {
    this.allocataireMapper = allocataireMapper;
    this.allocationMapper = allocationMapper;
  }

  public List<Allocataire> findAllAllocataires(String likeNom) {
    return allocataireMapper.findAll(likeNom);
  }

  public List<Allocation> findAllocationsActuelles() {
    return allocationMapper.findAll();
  }

  public Parent getParentDroitAllocation(EnfantDroit ed) {
    System.out.println("Déterminer le droit aux allocations");
    Parent parent1 = ed.getParent1();
    Parent parent2 = ed.getParent2();
    Parent parentDroit;

    if(parent1.getActiviteLucrative() ^ parent2.getActiviteLucrative()){
      parentDroit = (parent1.getActiviteLucrative()) ? parent1 : parent2;
    }else {
      if(parent1.getAAutoriteTutelaire() ^ parent2.getAAutoriteTutelaire()){
        parentDroit = (parent1.getAAutoriteTutelaire()) ? parent1 : parent2;
      }else{
        if(!ed.getParentsEnsemble()){
          parentDroit = ed.getResidance().equals(parent1.getResidence()) ? parent1 : parent2;
        }else{
          if (parent1.getCanton().equals(ed.getCanton()) ^ parent2.getCanton().equals(ed.getCanton())){
            parentDroit = ed.getCanton().equals(parent1.getCanton()) ? parent1 : parent2;
          }else if((!parent1.getIndependant() && !parent2.getIndependant()) || parent1.getIndependant() ^ parent2.getIndependant()){
            if(!parent1.getIndependant() && !parent2.getIndependant()){
              parentDroit = getParentWithHigherSalary(parent1, parent2);
            }else{
              parentDroit = parent1.getIndependant() ? parent2 : parent1;
            }
          }else{
            // 2 indépendants
            parentDroit = getParentWithHigherSalary(parent1, parent2);
          }
        }
      }
    }
    return parentDroit;
  }

  private Parent getParentWithHigherSalary(Parent parent1, Parent parent2) {

    return parent1.getSalaire().doubleValue() > parent2.getSalaire().doubleValue() ? parent1 : parent2;
  }
}
