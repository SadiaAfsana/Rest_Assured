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
import static io.restassured.RestAssured.put;

public class Customer {

    public Integer ID;
    public String name;
    public String email;
    public String address;
    public String phone_number;
    Properties props = new Properties();
    FileInputStream file = new FileInputStream("./src/test/resources/config.properties");

    public Customer() throws FileNotFoundException {
    }

    public void callingCustomerLoginAPI() throws IOException, ConfigurationException {

        props.load(file);

        RestAssured.baseURI = props.getProperty("baseURL");
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
        String token = resObj.get("token");
        Utils.setEnvVariable("token", token);
        System.out.println("token : " + token);
    }

    public void callingCustomerListAPI() throws IOException {

        props.load(file);
        RestAssured.baseURI = props.getProperty("baseURL");
        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", props.getProperty("token"))
                        .when()
                        .get("/customer/api/v1/list")

                        .then()
                        .assertThat().statusCode(200).extract().response();

        System.out.println(response.asString());

        JsonPath resObj = response.jsonPath();
        String id = resObj.get("Customers[0].id").toString();
        Assert.assertEquals("101", id);
    }

    public void callingSearchCustomerAPI() throws IOException {

        props.load(file);
        RestAssured.baseURI = props.getProperty("baseURL");
        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", props.getProperty("token"))
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

    public void generateCustomer() throws IOException, ConfigurationException {
        props.load(file);
        RestAssured.baseURI = "https://randomuser.me";
        Response response =
                given()
                        .contentType("application/json")
                        .when()
                        .get("/api")
                        .then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath jsonObj = response.jsonPath();

        ID = (int) Math.floor(Math.random() * (999999 - 100000) + 1);
        name = "Sadia " + jsonObj.get("results[0].name.first");
        email = jsonObj.get("results[0].email");
        address = jsonObj.get("results[0].location.state");
        phone_number = jsonObj.get("results[0].cell");

        Utils.setEnvVariable("id", ID.toString());
        Utils.setEnvVariable("name", name);
        Utils.setEnvVariable("email", email);
        Utils.setEnvVariable("address", address);
        Utils.setEnvVariable("phone_number", phone_number);

        System.out.println(response.asString());

    }

    public void createCustomer() throws IOException {
        props.load(file);
        RestAssured.baseURI = props.getProperty("baseURL");
        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", props.getProperty("token"))
                        .body("" +
                                "{\"id\":" + props.getProperty("id") + ",\n" +
                                "    \"name\":\"" + props.getProperty("name") + "\", \n" +
                                "    \"email\":\"" + props.getProperty("email") + "\",\n" +
                                "    \"address\":\"" + props.getProperty("address") + "\",\n" +
                                "    \"phone_number\":\"" + props.getProperty("phone_number") + "\"}")
                        .when()
                        .post("/customer/api/v1/create")
                        .then()
                        .assertThat().statusCode(201).extract().response();

        System.out.println(response.asString());
    }

    public void deleteCustomer() throws IOException {
        props.load(file);
        RestAssured.baseURI = props.getProperty("baseURL");
        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", props.getProperty("token"))
                        .when()
                        .body("" +
                                "{\"id\":" + props.getProperty("id") + ",\n" +
                                "    \"name\":\"" + props.getProperty("name") + "\", \n" +
                                "    \"email\":\"" + props.getProperty("email") + "\",\n" +
                                "    \"address\":\"" + props.getProperty("address") + "\",\n" +
                                "    \"phone_number\":\"" + props.getProperty("phone_number") + "\"}")

                        .delete("/customer/api/v1/delete/" + props.getProperty("id"))
                        .then()
                        .assertThat().statusCode(200).extract().response();
    }

    public void updateCustomer() throws IOException {
        props.load(file);
        RestAssured.baseURI = props.getProperty("baseURL");
        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", props.getProperty("token"))
                        .when()
                        .body("{\n" +
                                "    \"id\": 101,\n" +
                                "    \"name\": \"Mr. Jamal Islam\",\n" +
                                "    \"email\": \"mrjamal@test.com\",\n" +
                                "    \"address\": \"Dhanmondi,Dhaka\",\n" +
                                "    \"phone_number\": \"01502020110\"\n" +
                                "}")
                        .put("/customer/api/v1/update/101")
                        .then()
                        .assertThat().statusCode(200).extract().response();

    }
}
