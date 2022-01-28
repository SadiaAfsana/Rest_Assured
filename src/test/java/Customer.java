import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class Customer {

    Properties props = new Properties();
    FileInputStream file = new FileInputStream("./src/test/resources/config.properties");

    public Customer() throws FileNotFoundException {
    }

    public void callingCustomerLoginAPI() throws IOException, ConfigurationException {

        props.load(file);

        RestAssured.baseURI=props.getProperty("baseURL");
        Response response =
                given()
                        .contentType("application/json")
                        .body("{ \n" +
                                " \"username\":\"salman\", \n" +
                                " \"password\":\"salman1234\" \n" +
                                "} \n")
                .when()
                        .post("/customer/api/v1/login")
                .then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath resObj = response.jsonPath();
        String token =  resObj.get("token");
        Utils.setEnvVariable("token",token);
        System.out.println("token : " +token);
    }

    public void callingCustomerListAPI() throws IOException {

        props.load(file);
        RestAssured.baseURI=props.getProperty("baseURL");
        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization",props.getProperty("token"))
                .when()
                        .get("/customer/api/v1/list")

                .then()
                        .assertThat().statusCode(200).extract().response();

        System.out.println(response.asString());

        JsonPath resObj = response.jsonPath();
        String id = resObj.get("Customers[0].id").toString();
        Assert.assertEquals("101",id);
    }

    public void callingSearchCustomerAPI() throws IOException {

        props.load(file);
        RestAssured.baseURI=props.getProperty("baseURL");
        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization",props.getProperty("token"))
                .when()
                        .get("/customer/api/v1/get/101")

                .then()
                        .assertThat().statusCode(200).extract().response();

        System.out.println(response.asString());

        JsonPath jsonObj = response.jsonPath();
        String name = jsonObj.get("name");
        Assert.assertEquals("Mr. Kamal", name);
        //Assert.assertEquals("Mr. Kamal",jsonObj.get("name").toString());
    }
}