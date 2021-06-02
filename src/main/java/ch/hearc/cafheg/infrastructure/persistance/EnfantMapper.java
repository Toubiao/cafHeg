package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.NoAVS;
import ch.hearc.cafheg.business.versements.Enfant;
import ch.qos.logback.classic.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class EnfantMapper extends Mapper {

  private static final Logger logger
      = (Logger) LoggerFactory.getLogger(EnfantMapper.class);

  public Enfant findById(long id) {
    Connection connection = getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT NO_AVS, NOM, PRENOM FROM ENFANTS WHERE NUMERO=?")) {
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();
      return new Enfant(new NoAVS(resultSet.getString(1)),
          resultSet.getString(2), resultSet.getString(3));
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

}
