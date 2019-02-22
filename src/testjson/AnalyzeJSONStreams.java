package testjson;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AnalyzeJSONStreams {
	
	public static final String URL_TO_TEST = "https://data.sfgov.org/resource/4qkm-qtkz.json";
	List<JsonObject> items= new ArrayList<>();
	
	public AnalyzeJSONStreams() throws IOException {
		String response = getResponseString();
    	JsonParser jparse = new JsonParser();
	    JsonArray jsonArray = (JsonArray) jparse.parse(response);
	    
	    for(int i=0;i<jsonArray.size();i++) {
	    	items.add(jsonArray.get(i).getAsJsonObject());
	    }
	}
	
    @Test
    public void verifyInstallations() throws IOException {
    	
	    List<JsonObject> result = items.stream().filter(item -> {
	    	return item.get("start_date_year").toString().contains("1983") && item.get("end_date_year").toString().contains("1984");
	    }).collect(Collectors.toList());
    	
    	assertEquals(4, result.size());
		
    }
    
    @Test
    public void verifyTitleGallerySite() throws IOException {
	    
	    List<JsonObject> result= items.stream()
	    		.filter(item -> {
	    			return item.get("title").toString().contains("New Glass") && item.get("gallery_site").toString().contains("F02");
	    		}).collect(Collectors.toList());
	      
	    assertEquals(1, result.size());
    }
    
    public static <E> String getResponseString() throws IOException {
		CloseableHttpClient close = HttpClients.createDefault();
		HttpGet get = new HttpGet(URL_TO_TEST);
	    get.setHeader("Content-Type","application/json");
	    CloseableHttpResponse response1 = close.execute(get);
	    try {
	        HttpEntity entity1 = response1.getEntity();
	        return EntityUtils.toString(entity1, "UTF-8");
	    } finally {
	        response1.close();
	    }
	}
}
