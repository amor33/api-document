package app.document.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.document.dao.ApiDao;
import app.document.dao.ParamsDao;
import app.document.entity.Api;
import app.document.entity.Params;
/**
 * 
 * @author WenZT
 * @Date 2017年9月1日
 */
@Service
public class ApiService {

    @Autowired
    private ApiDao apiDao ;
    @Autowired
    private ParamsDao paramsDao;
    
    public List<Api> getAllApi(){
        return apiDao.findAll() ;
    }

	public Api save(Api api, List<Params> params) {
		api = apiDao.save(api);
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		if(null != params){
			 for(Params p : params){
					batchArgs.add(new Object[]{p.getParamsName(),p.getParamsType(),p.getExampleParams(),api.getId()
							,p.getIsRequired(),p.getParamsDiscription()});
				}
				paramsDao.save(batchArgs);
		 }
		return api;
	}

	public Api update(Api api, List<Params> params) {
		delete(api.getId());
		return save(api,params);
	}

	public Integer delete(Long apiId) {
		paramsDao.delete(apiId);
		return apiDao.delete(apiId);
	}

	public List<Params> getPatams(Long apiId) {
		return paramsDao.getParams(apiId);
	}

	public boolean codeExitis(Long id, String code) {
		return apiDao.codeExists(code, id);
	}

	public List<String> project() {
		return apiDao.project();
	}
}
