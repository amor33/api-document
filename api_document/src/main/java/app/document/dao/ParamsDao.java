package app.document.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import app.document.entity.Params;

@Repository
public class ParamsDao {
	protected final String deleteSql = "delete from params where apiid = ?";
	protected final String insertSql = "insert into params (paramsname,paramstype,exampleparams,apiid,isrequired,paramsdiscription) values (?,?,?,?,?,?)";
	protected final String selectSql = "select * from params where apiId = ?" ;
    @Autowired
    private JdbcTemplate jdbcTemplate ;

	public void save(List<Object[]> batchArgs) {
		jdbcTemplate.batchUpdate(insertSql, batchArgs);
	}

	public int delete(Long apiId) {
		return jdbcTemplate.update(deleteSql, apiId);		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Params> getParams(Long apiId) {
		return jdbcTemplate.query(selectSql, new BeanPropertyRowMapper(Params.class), apiId) ;
	}
}
