Feature: Déterminer quel parent peut avoir droit aux allocations.

  Utile pour le service des allocations familliales, pour pouvoir déterminer quel parent d'une famille peut obtenir des allocations.

  Background:
    Given Enfant avec deux parents

  Scenario Outline: Cas 1 : Un parent avec une activité lucrative
    When Le parent un a une activite lucrative "<parent1>"
    And  Le parent deux a une activite lucrative "<parent2>"
    Then le parent "<parentDroit>" a le droit a l allocation
    Examples:
      | parent1 | parent2 | parentDroit |
      | true    | false   | parent1     |
      | false   | true    | parent2     |

  Scenario Outline: Cas 2 : Deux parents avec une activité lucrative mais seulement 1 avec l'autorité parentale
    When Les deux parents ont une activite lucrative
    And  Parent un detient l'autorité parentale "<parent1Authority>"
    And  Parent deux detient l'autorité parentale "<parent2Authority>"
    Then le parent "<parentDroit>" a le droit a l allocation
    Examples:
      | parent1Authority | parent2Authority | parentDroit |
      | true             | false            | parent1     |
      | false            | true             | parent2     |

  Scenario Outline: Cas 3 : Deux parents separés avec une activité lucrative ainsi que l'autorité parentale et un parent vit avec l'enfant
    When Les deux parents ont une activite lucrative
    And  Les deux parents ont l'autorité parentales
    And  Les parents sont separés
    And  Parent un vit a l adresse "<parent1Adresse>"
    And  Parent deux vit a l adresse "<parent2Adresse>"
    And  l enfant vit a l adresse "<enfantAdresse>"
    Then le parent "<parentDroit>" a le droit a l allocation
    Examples:
      | parent1Adresse                | parent2Adresse                | enfantAdresse | parentDroit |
      | Rue du salut                  | Rue du je me suis fait trompe | Rue du salut  | parent1     |
      | Rue du je me suis fait trompe | Rue du salut                  | Rue du salut  | parent2     |

  Scenario Outline: Cas 4 : Deux parents vivant ensemble avec une activité lucrative ainsi que l'autorité parentale et un parent travail dans le même canton que l'enfant
    When Les deux parents ont une activite lucrative
    And  Les deux parents ont l'autorité parentales
    And  Les parents vivent ensemble
    And  Parent un travail dans le canton "<parent1CantonDeTravail>"
    And  Parent deux travail danse le canton "<parent2CantonDeTravail>"
    And  l enfant vit dans le canton "<enfantCanton>"
    Then le parent "<parentDroit>" a le droit a l allocation
    Examples:
      | parent1CantonDeTravail | parent2CantonDeTravail | enfantCanton | parentDroit |
      | NE                     | GE                     | NE           | parent1     |
      | GE                     | NE                     | NE           | parent2     |

  Scenario Outline: Cas 5-1 : Deux parents vivant ensemble avec une activité lucrative ainsi que l'autorité parentale les deux parents sont salaries et un parent gagne plus que l'autre
    When Les deux parents ont une activite lucrative
    And  Les deux parents ont l autorite parentales
    And  Les parents vivent ensemble
    And  Les parents ne sont pas indepandants
    And  Parent un a le salaire suivant "<parent1Salaire>"
    And  Parent deux a le salaire suivant "<parent2Salaire>"
    Then le parent "<parentDroit>" a le droit a l allocation
    Examples:
      | parent1Salaire | parent2Salaire | parentDroit |
      | 7000           | 6000           | parent1     |
      | 6000           | 7000           | parent2     |

  Scenario Outline: Cas 5-2 : Deux parents vivant ensemble avec une activité lucrative ainsi que l'autorité parentale et un parent est indepandant et un parent est salarie
    When Les deux parents ont une activite lucrative
    And  Les deux parents ont l'autorité parentales
    And  Les parents vivent ensemble
    And  Parent un est il indepandant "<parent1Indepdant>"
    And  Parent deux est il indepandant "<parent2Indepdant>"
    Then le parent "<parentDroit>" a le droit a l allocation
    Examples:
      | parent1Indepdant | parent2Indepdant | parentDroit |
      | false            | true             | parent1     |
      | true             | false            | parent2     |

  Scenario Outline: Cas 6 : Deux parents vivant ensemble avec une activité lucrative ainsi que l'autorité parentale les deux parents sont indépendant et un parent gagne plus que l'autre
    When Les deux parents ont une activite lucrative
    And  Les deux parents ont l'autorité parentales
    And  Les parents vivent ensemble
    And  Les parents sont indepandants
    And  Parent un a le salaire suivant "<parent1Salaire>"
    And  Parent deux a le salaire suivant "<parent2Salaire>"
    Then le parent "<parentDroit>" a le droit a l allocation
    Examples:
      | parent1Salaire | parent2Salaire | parentDroit |
      | 7000           | 6000           | parent1     |
      | 6000           | 7000           | parent2     |
