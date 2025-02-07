package data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

//Generisches Interface zum Mappen von ResultSets auf Entitäten
public interface ResultSetMapper<T> {
	T map(ResultSet rs) throws SQLException;
}
