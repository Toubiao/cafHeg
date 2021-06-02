package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.Allocation;
import ch.hearc.cafheg.business.allocations.Canton;
import ch.hearc.cafheg.business.common.Montant;
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
public class AllocationMapper extends Mapper {

  private static final Logger logger
      = (Logger) LoggerFactory.getLogger(AllocationMapper.class);

  public List<Allocation> findAll() {
    Connection connection = getConnection();
    try (PreparedStatement preparedStatement = connection
        .prepareStatement("SELECT * FROM ALLOCATIONS")) {
      ResultSet resultSet = preparedStatement.executeQuery();
      List<Allocation> allocations = new ArrayList<>();
      while (resultSet.next()) {
        allocations.add(
            new Allocation(new Montant(resultSet.getBigDecimal(2)),
                Canton.fromValue(resultSet.getString(3)), resultSet.getDate(4).toLocalDate(),
                resultSet.getDate(5) != null ? resultSet.getDate(5).toLocalDate() : null));
      }
      return allocations;
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new RuntimeException(e);
    }

  }
}
