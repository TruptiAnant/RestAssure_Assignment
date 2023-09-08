import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.testng.annotations.Test
public class Test {
    String baseUrl="https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest";
    @BeforeTest
    public  void setBaseUrl(){
        RestAssured.baseURI=this.baseUrl;
        RestAssured.urlEncodingEnabled=false;
    }
    public void testItemsMoreThan20(){
        RequestSpecification requestSpecification=RestAssured.given();
        requestSpecification.header("Content-Type", ContentType.ANY,"Accept","ContentType.ANY");
        Response response=requestSpecification.get("/currencies.json");
        JsonPath jsonPath=new JsonPath(response.asString());
        System.out.println("List of items size"+jsonPath.getInt("size()"));
        Assert.assertTrue(jsonPath.getInt("size()")>20);
    }

    public  void testUSDollarInResponse(){
        RequestSpecification requestSpecification=RestAssured.given();
        requestSpecification.header("Content-Type", ContentType.ANY,"Accept","ContentType.ANY");
        Response response=requestSpecification.get("/currencies.json");
        JsonPath jsonPath=new JsonPath(response.asString());
        System.out.println("USD  "+JsonPath.from(response.asString()).get("usd"));
        Assert.assertNotNull(jsonPath.from(response.asString()).get("usd"));
    }
@org.testng.annotations.Test
    public void testBritishCurrencyInResponse(){
        RequestSpecification requestSpecification=RestAssured.given();
        requestSpecification.header("Content-Type", ContentType.ANY,"Accept","ContentType.ANY");
        Response response=requestSpecification.get("/currencies.json");
        JsonPath jsonPath=new JsonPath(response.asString());
        System.out.println("GBP  "+JsonPath.from(response.asString()).get("gbp"));
        Assert.assertNotNull(jsonPath.from(response.asString()).get("gbp"));
    }

    public void testAllCountryCurrency() throws Exception{
        RequestSpecification requestSpecification=RestAssured.given();
        requestSpecification.header("Content-Type", ContentType.ANY,"Accept","ContentType.ANY");
        Response response=requestSpecification.get("/currencies.json");

        Map<String , Map<String,Double>> currencyPair=new HashMap<>();

        for(String currency :getAllAbbreviations(response)){
            Response response1=requestSpecification.get("/currencies/"+currency+".json");
            Map<String,Double>parsedJsonObject=JsonPath.from(response1.asString()).get(currency);
            if(null != response.getBody()){
                currencyPair.put(currency,parsedJsonObject);
            }
        }
        System.out.println("currency pairs  "+currencyPair);
    }
 private List<String> getAllAbbreviations(Response response) throws Exception{
        Map<String,String> parsedJsonObject= new ObjectMapper().readValue(response.getBody().asString(), new TypeReference<Map<String, String>>() {
        });

return  new ArrayList<>(parsedJsonObject.keySet());
 }
}
