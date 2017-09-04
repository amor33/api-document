package app.document.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import app.document.entity.Api;

@Repository
public class ApiDao {
	protected final String deleteSql = "delete from api where id = ?";
	protected final String selectSql = "select * from api";
	protected final String saveSql = "insert into api (code,discription,exampleuri,name,project,result,type,uri) values(?,?,?,?,?,?,?,?)";
	protected final String saveReturnSql = "select * from api where code = ?";
	
    @Autowired
    private JdbcTemplate jdbcTemplate ;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Api> findAll(){
        return jdbcTemplate.query(selectSql,new BeanPropertyRowMapper(Api.class));
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Api save(Api api) {
		jdbcTemplate.update(saveSql,new Object[]{api.getCode(),api.getDiscription(),api.getExampleUri(),api.getName()
				,api.getProject(),api.getResult(),api.getType(),api.getUri()});
		return (Api) jdbcTemplate.queryForObject(saveReturnSql, new BeanPropertyRowMapper(Api.class),api.getCode());
	}

	public int delete(Long apiId) {
		return jdbcTemplate.update(deleteSql, apiId);
	}
}
