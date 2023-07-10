package bookertests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import java.io.IOException;

import static io.restassured.RestAssured.*;

public class BookingTests {

    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    @Test
    public void testGetAllIds() {
        Response response = RestAssured.given().get("/booking");
        response.then().statusCode(200);
        //get Ids of all available books for booking
        response.jsonPath().get("data.findAll");
        response.prettyPrint();
    }

    @Test
    public void testChangePaymentForBooking(){

    }
}
