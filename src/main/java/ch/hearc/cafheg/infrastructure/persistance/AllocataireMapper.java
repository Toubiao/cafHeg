package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.NoAVS;
import ch.qos.logback.classic.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository
public class AllocataireMapper extends Mapper {

  private static final Logger logger
      = (Logger) LoggerFactory.getLogger(AllocataireMapper.class);

  public List<Allocataire> findAll(String likeNom) {
    Connection connection = getConnection();
    try {
      PreparedStatement preparedStatement;
      if (likeNom == null) {
        preparedStatement = connection
            .prepareStatement("SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES");
      } else {
        preparedStatement = connection
            .prepareStatement("SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES WHERE NOM LIKE ?");
        preparedStatement.setString(1, likeNom + "%");
      }
      List<Allocataire> allocataires = new ArrayList<>();
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          allocataires
              .add(new Allocataire(new NoAVS(resultSet.getString(3)), resultSet.getString(2),
                  resultSet.getString(1)));
        }
      }
      return allocataires;
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public boolean deleteById(long id) {
    Connection cnn = getConnection();
    try (PreparedStatement preparedStatement = cnn
        .prepareStatement("DELETE FROM ALLOCATAIRES WHERE NUMERO=?")) {
      preparedStatement.setLong(1, id);
      int nbDeleted = preparedStatement.executeUpdate();
      return nbDeleted > 0;
    } catch (SQLException throwables) {
      logger.error(throwables.getMessage());
    }
    return false;
  }

  public boolean updateById(long id, Allocataire allocataire) {
    Allocataire oldAllocataire = findById(id);
    if (!oldAllocataire.equals(allocataire)) {
      Connection cnn = getConnection();
      try (PreparedStatement preparedStatement = cnn
          .prepareStatement("UPDATE ALLOCATAIRES SET NOM = ?, PRENOM = ? WHERE NUMERO=?")) {
        preparedStatement.setString(1, allocataire.getNom());
        preparedStatement.setString(2, allocataire.getPrenom());
        preparedStatement.setLong(3, id);
        int nbUpdated = preparedStatement.executeUpdate();
        return nbUpdated > 0;
      } catch (SQLException throwables) {
        logger.error(throwables.getMessage());
      }
    } else {
      logger.debug("Aucun changement pour l'allocataire");
    }
    return false;
  }

  public Allocataire findById(long id) {
    Connection connection = getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT NO_AVS, NOM, PRENOM FROM ALLOCATAIRES WHERE NUMERO=?")) {
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();
      return new Allocataire(new NoAVS(resultSet.getString(1)),
          resultSet.getString(2), resultSet.getString(3));
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
