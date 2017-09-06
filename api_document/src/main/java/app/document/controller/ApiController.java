package app.document.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;

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
    @RequestMapping("/project")
    @ResponseBody
    public List<String> project(){
        return apiService.project() ;
    }
    @RequestMapping("/save")
    public  Api save( Api api ,HttpServletRequest req){
        return apiService.update(api,getParams(req)) ;
    }
    @RequestMapping("/checkcode")
    public  boolean codeExitis(@RequestParam(value = "apiId",required=false) Long id, @RequestParam(value="code")String code){
        return apiService.codeExitis(id, code) ;
    }
    private List<Params> getParams(HttpServletRequest req) {
    		List<Params> params = new ArrayList<Params>();
    		if(StringUtils.isNotBlank(req.getParameter("paramsType"))){
    			if(req.getParameter("paramsType").startsWith("[") && req.getParameter("paramsType").endsWith("]")){
    				JSONArray paramsType = JSONArray.parseArray(req.getParameter("paramsType"));
        			JSONArray paramsName = JSONArray.parseArray(req.getParameter("paramsName"));
        			JSONArray exampleParams = JSONArray.parseArray(req.getParameter("exampleParams"));
        			JSONArray isRequireds = JSONArray.parseArray(req.getParameter("isRequired"));
        			JSONArray paramsDiscription = JSONArray.parseArray(req.getParameter("paramsDiscription"));
        			for(int i = 0 ; i<paramsType.size(); i++){
        				params.add( new Params(paramsName.get(i).toString(),paramsType.get(i).toString(),exampleParams.get(i).toString()
        						,isRequireds.get(i).toString(),paramsDiscription.get(i).toString()));
        			}
    			}else{
    				params.add( new Params(req.getParameter("paramsName").replaceAll("\"",""),
    						req.getParameter("paramsType").replaceAll("\"",""),req.getParameter("exampleParams").replaceAll("\"",""),
    						req.getParameter("isRequired").replaceAll("\"",""),req.getParameter("paramsDiscription").replaceAll("\"","")));
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
