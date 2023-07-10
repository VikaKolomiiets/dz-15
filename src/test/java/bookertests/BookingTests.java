package bookertests;

import bookers.Booking;
import bookers.Bookingdates;
import bookers.CreateBooking;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class BookingTests {

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    //Task_1 : Створити бронювання книжки (POST)
    @Test
    public void testCreateBooking() {
        Booking createBody = new Booking(
                "Alise",
                "TestBooking",
                259,
                true,
                new Bookingdates(Date.valueOf("2018-07-07"), Date.valueOf("2019-07-08")),
                "new booking");
        String token = getToken();

        Response responseIds = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .and()
                .body(createBody)
                .when()
                .post("/booking")
                .then()
                .extract().response();
        System.out.println("Status code: "  + responseIds.statusCode());
        responseIds.prettyPrint();
    }


    //Task_2 :  Отримати id усіх доступних бронювань книжок (GET /booking)
    @Test
    public void testGetAllBookingIds() {
        Response responseIds = RestAssured.given().get("/booking?checkout<2023-07-07");
        List<Integer> bookingIds = responseIds.jsonPath().
                getList("bookingid").stream().map(o -> (Integer) o).toList();
        System.out.println("Number of Booking: " + bookingIds.size());
        System.out.println("Status code: " + responseIds.statusCode());
        responseIds.prettyPrint();
    }


    //Task_3 :  Вибрати одне з id з п.2 та змінити ціну бронювання (PATCH)
    @Test
    public void testChangePaymentForBooking1() {
        Response responseIds = RestAssured.given().get("/booking?checkout<2023-07-07");
        Integer bookingId = responseIds.jsonPath().
                getList("bookingid").stream().map(o -> (Integer) o).toList().get(0);

        Response responseId = RestAssured.given().get("/booking/" + bookingId);
        responseId.prettyPrint();
        Booking body = responseId.getBody().as(Booking.class);
        int oldprice = body.getTotalprice().intValue();
        body.setTotalprice(oldprice * 2);

        String token = getToken();
        Response postRequest = RestAssured.given().
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                header("Cookie", "token=" + token).
                and().
                body(body).
                when().
                patch("/booking/" + bookingId).
                then().
                extract().response();
        System.out.println(postRequest.statusCode());
        postRequest.prettyPrint();
    }

    //Task_4 :   Вибрати інший id з отриманих у п.2 та змінити ім’я та additionalneeds (PUT)
    @Test
    public void testChangeNameAndNeedsForBooking1() {
        Response responseIds = RestAssured.given().get("/booking?checkout<2023-07-07");
        Integer bookingId = responseIds.jsonPath().
                getList("bookingid").stream().map(o -> (Integer) o).toList().get(10);
        Response responseId = RestAssured.given().get("/booking/" + bookingId);
        responseId.prettyPrint();

        Booking body = responseId.getBody().as(Booking.class);
        body.setFirstname("TestFirstName");
        body.setLastname("TestLastName");
        body.setAdditionalneeds("More info");

        String token = getToken();
        Response postRequest = RestAssured.given().
                header("Content-Type", "application/json").
                header("Accept", "application/json").
                header("Cookie", "token=" + token).
                and().
                body(body).
                when().
                put("/booking/" + bookingId).
                then().
                extract().response();
        System.out.println(postRequest.statusCode());
        postRequest.prettyPrint();
    }

    // Task_5: Видалити одне бронювання з отриманих у п.2 (DELETE)
    @Test
    public void testDeleteBooking() {
        Response responseIds = RestAssured.given().get("/booking?checkout<2023-07-07");
        Integer bookingId = responseIds.jsonPath().
                getList("bookingid").stream().map(o -> (Integer) o).toList().get(5);
        Response deleteId = RestAssured.given().get("/booking/" + bookingId);
        deleteId.prettyPrint();
        String token = getToken();
        Response deleteRequest = RestAssured.given().
                header("Cookie", "token=" + token).
                when().
                delete("/booking/" + bookingId).
                then().
                extract().response();
        System.out.println(deleteRequest.statusCode());
    }


    private String getToken() {
        Response postRequest = RestAssured.given().
                header("Content-Type", "application/json").
                and().
                body("{\"username\" : \"admin\", \"password\" : \"password123\"}").
                post("https://restful-booker.herokuapp.com/auth");
       // postRequest.prettyPrint();
        return postRequest.jsonPath().get("token").toString();
    }

}
