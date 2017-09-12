package app.document.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.document.dao.ApiTestDao;
import app.document.entity.ApiTest;
import app.document.entity.ApiTestDetail;

/**
 * 
 * @author WenZT
 * @Date 2017年9月9日
 */
@Service
public class ApiTestService {

    @Autowired
    private ApiTestDao apiTestDao ;
    
    public List<ApiTest> getAllApiTest(){
        return apiTestDao.findAll() ;
    }

	public ApiTest save(ApiTest apiTest) {
		return apiTestDao.save(apiTest);
	}
	
	public boolean codeExitis(Long id, String code) {
		return apiTestDao.codeExists(code, id);
	}
	
	public void saveDetail(ApiTestDetail detail) {
		apiTestDao.saveDetail(detail);
	}
	public List<ApiTestDetail> getApiTestDetail(Long mainId){
        return apiTestDao.getApiTestDetail(mainId) ;
    }
	public Integer delete(Long apiId) {
		return apiTestDao.delete(apiId);
	}
}
