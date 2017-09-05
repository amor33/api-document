package app.document.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.document.entity.Api;
import app.document.entity.Params;
import app.document.entity.ReturnEntity;
import app.document.service.ApiService;

@RestController
@RequestMapping("/template")
public class ApiController {


    @Autowired
    private ApiService apiService ;

    @RequestMapping("/list")
    @ResponseBody
    public ReturnEntity list(){
    		
        return  ReturnEntity.succeed(apiService.getAllApi());
    }
    
    @RequestMapping("/paramslist")
    @ResponseBody
    public List<Params> paramsList(@RequestParam(value="apiId") Long apiId){
        return apiService.getPatams(apiId) ;
    }
    
    @RequestMapping("/save")
    public  Api save( Api api ,HttpServletRequest req){
        return apiService.update(api,getParams(req)) ;
    }
    
    private List<Params> getParams(HttpServletRequest req) {
    		List<Params> params = new ArrayList<Params>();
    		if(StringUtils.isNotBlank(req.getParameter("paramsType"))){
//    			JSONObject json=JSONObject.fromObject((String)req.getParameter("paramsType"));
    			String[] paramsType = req.getParameter("paramsType").split(",");
    			String[] paramsName = req.getParameter("paramsName").split(",");
    			String[] exampleParams = req.getParameter("exampleParams").split(",");
    			String[] isRequireds = req.getParameter("isRequired").split(",");
    			for(int i = 0 ; i<paramsType.length; i++){
    				params.add( new Params(
    						paramsName[i].substring(1,paramsName[i].length() - 1),
    						paramsType[i].substring(1,paramsType[i].length() - 1),
    						exampleParams[i].substring(1,exampleParams[i].length() - 1),
    						isRequireds[i].substring(1,isRequireds[i].length() - 1)));
    			}
    		}
		return params.isEmpty() ? null : params;
	}

	@RequestMapping("/update")
    public Api update(Api api,HttpServletRequest req){
        return apiService.update(api,getParams(req));
    }
    @RequestMapping("/delete")
    public Integer delete(@RequestParam(value="apiId") Long apiId){
        return apiService.delete(apiId) ;
    }
}
