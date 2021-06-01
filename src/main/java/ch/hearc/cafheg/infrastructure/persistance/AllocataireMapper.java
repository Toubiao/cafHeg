package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.NoAVS;
import ch.hearc.cafheg.business.versements.VersementParentEnfant;
import ch.hearc.cafheg.infrastructure.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllocataireMapper extends Mapper {

  @Autowired
  VersementMapper versementMapper;

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
      throw new RuntimeException(e);
    }
  }

  public Allocataire findById(long id) {
    Connection connection = getConnection();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(
          "SELECT NO_AVS, NOM, PRENOM FROM ALLOCATAIRES WHERE NUMERO=?");
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();
      return new Allocataire(new NoAVS(resultSet.getString(1)),
          resultSet.getString(2), resultSet.getString(3));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
