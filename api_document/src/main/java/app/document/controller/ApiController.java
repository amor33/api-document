package app.document.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import app.document.entity.Api;
import app.document.entity.ApiTest;
import app.document.entity.ApiTestDetail;
import app.document.entity.Params;
import app.document.entity.ReturnEntity;
import app.document.excel.ExcelUtils;
import app.document.excel.Metals;
import app.document.service.ApiService;
import app.document.service.ApiTestService;

@RestController
@RequestMapping("/template")
public class ApiController {
	private static final int SOCKET_TIME_OUT = 6000;
	private static final int CONNECT_TIME_OUT = 6000;
	
	private static RequestConfig getRequestConfig(int socketTimeout, int connectTimeout) {
		return RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
	}
	private static RequestConfig getRequestConfig() {
		return getRequestConfig(SOCKET_TIME_OUT, CONNECT_TIME_OUT);
	}
	
	private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(100);
    @Autowired
    private ApiService apiService ;
    @Autowired
    private ApiTestService apiTestService ;

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
		return apiService.delete(apiId);
	}
    
    @RequestMapping("/apitestsave")
    public String apiTest(ApiTest apiTest ,HttpServletRequest request){
    		apiTest.setCreateDate(new Date());
    		List<NameValuePair> params = createParams(request);
    		
    		JSONObject obj = new JSONObject();
    		for(NameValuePair nvp : params){
    			obj.put(nvp.getName(), nvp.getValue());
    		}
    		apiTest.setParams(obj.toJSONString());
    		Long mainId = apiTestService.save(apiTest).getId();
    		for (int i= 0 ; i < apiTest.getCount(); i++){
    			executorService.execute(new Runnable() {
    				@Override
    				public void run() {
    					CloseableHttpClient client = HttpClients.createDefault();
					try {
						Date start = new Date();
    						HttpResponse  response = null;
						if (apiTest.getType().equals("POST")) {
							HttpPost req = new HttpPost(apiTest.getUrl());
							req.setConfig(getRequestConfig());
							req.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
							req.addHeader(apiTest.getAuthType(), apiTest.getCookie());
							response = client.execute(req);
						} else {
							URIBuilder uri = new URIBuilder(apiTest.getUrl());
							uri.setParameters(params);
							HttpGet req = new HttpGet(uri.build());
							req.setConfig(getRequestConfig());
							req.addHeader(apiTest.getAuthType(), apiTest.getCookie());
							response = client.execute(req);
						}
						apiTestService.saveDetail(new ApiTestDetail(mainId, start, new Date(), 
								response.getStatusLine().getStatusCode(), handelEntity(response.getEntity())));
					} catch (URISyntaxException e) {
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if(client != null) {
//								client.close();
							}
						} catch(Exception e) {
							
						}
					}
    				}
    			});
    		}
    		return "OK";
	}

	private  String handelEntity(HttpEntity entity) {

		try {
			String str = EntityUtils.toString(entity, "UTF-8");
			return str;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
    private List<NameValuePair> createParams(HttpServletRequest req) {
    		List<NameValuePair> nvps = new ArrayList <NameValuePair>();
    		if(StringUtils.isNotBlank(req.getParameter("key"))){
			if(req.getParameter("key").startsWith("[") && req.getParameter("key").endsWith("]")){
				JSONArray keys = JSONArray.parseArray(req.getParameter("key"));
    				JSONArray values = JSONArray.parseArray(req.getParameter("value"));
    				for(int i = 0; i < keys.size(); i++){
    					nvps.add(new BasicNameValuePair(keys.get(i).toString(), values.get(i).toString()));
    				}
			}else{
				nvps.add(new BasicNameValuePair(req.getParameter("key"),req.getParameter("value")));
			}
		}
		return nvps;
	}

	@RequestMapping("/apitestlist")
    public ReturnEntity apiTestList(){
    		return  ReturnEntity.succeed(apiTestService.getAllApiTest());
	}
    @RequestMapping("/apitestdetail")
    public ReturnEntity apiTestList(@RequestParam(value="mainId") Long mainId){
    		return  ReturnEntity.succeed(apiTestService.getApiTestDetail(mainId));
	}
    @RequestMapping("/excel")
    public void excel(@RequestParam(value="mainId") Long mainId, HttpServletResponse resp){
    		List<ApiTestDetail> list = apiTestService.getApiTestDetail(mainId);
		Map<String,Object> data = new  HashMap<String,Object>();
		data.put("list", list);	
		Properties prop = new Properties();
		prop.setProperty(Metals.DEFAULT_DATE_FORMAT, "yyyy-MM-dd");
		String templateLoacation = "classpath:app.document.controller/api.xlsx";
		ExcelUtils.mergeForDownload(templateLoacation, data,"apitest.xlsx", prop,resp);
	}
    @RequestMapping("/apitestdelete")
    public Integer apiTestDelete(@RequestParam(value="id") Long mainId){
    		return  apiTestService.delete(mainId);
	}
}
