package app.document.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import app.document.entity.ApiTest;
import app.document.entity.ApiTestDetail;

@Repository
public class ApiTestDao {
	
	protected final String selectSql = "select * from apitest";
	protected final String selectDetailSql = "select * from apitestdetail a left join apitest b on a.mainid = b.id where a.mainid = ?";
	protected final String saveReturnSql = "select * from apitest where code = ?";
	protected final String saveSql = "insert into apitest (name,code,url,type,cookie,params,count,createdate) values(?,?,?,?,?,?,?,?)";
	protected final String saveDetailSql = "insert into apitestdetail (start,end,status,result,mainId,time) values(?,?,?,?,?,?)";
	protected final String deleteSql = "delete from apitest where id = ?";
	protected final String deleteDetailSql = "delete from apitestdetail where mainid = ?";
	
	@Autowired
    private JdbcTemplate jdbcTemplate ;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ApiTest> findAll(){
        return jdbcTemplate.query(selectSql,new BeanPropertyRowMapper(ApiTest.class));
    }
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ApiTest save(ApiTest apiTest) {
		jdbcTemplate.update(saveSql,new Object[]{apiTest.getName(),apiTest.getCode(),apiTest.getUrl(),
				apiTest.getType(),apiTest.getCookie(),apiTest.getParams(),apiTest.getCount(),apiTest.getCreateDate()});
		return (ApiTest) jdbcTemplate.queryForObject(saveReturnSql, new BeanPropertyRowMapper(ApiTest.class),apiTest.getCode());
	}

	public boolean codeExists(String code, Long id) {
		String existsSql = "select count(*) from apitest  where code = '" + code + "'";
		if (null != id) {
			existsSql += " and id<>" + id;
		}
		return jdbcTemplate.queryForObject(existsSql,Long.class) > 0;
   }
	public int delete(Long id) {
		jdbcTemplate.update(deleteDetailSql, id);
		return jdbcTemplate.update(deleteSql, id);
	}

	public void saveDetail(ApiTestDetail detail) {
		try {
			jdbcTemplate.update(saveDetailSql,new Object[]{detail.getStart(),detail.getEnd(),
					detail.getStatus(),detail.getResult(),detail.getMainId(),detail.getEnd().getTime() - detail.getStart().getTime()});
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ApiTestDetail> getApiTestDetail(Long mainId) {
		return jdbcTemplate.query(selectDetailSql, new BeanPropertyRowMapper(ApiTestDetail.class), mainId) ;
	}

}
