package bookertests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import static io.restassured.RestAssured.*;

public class BookingTests {

//    @BeforeMethod
//    public void setUp(){
//        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
//    }

    @Test
    public void testCreateBookPost(){
        Response response = RestAssured.get("https://restful-booker.herokuapp.com/booking");
        response.statusCode();
        response.prettyPrint();
        JsonPath jsp =new JsonPath(response.asString());
        int size = jsp.getInt("data.size()");
    }
}
