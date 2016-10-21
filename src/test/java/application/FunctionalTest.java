package application;

import java.util.Map;
import java.util.HashMap;
import static io.restassured.RestAssured.given;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeClass;

public class FunctionalTest extends BaseTest {

	//store the link of newly created student
	private String token;
	
	@Autowired
	private StudentRepository studentRepository;

	@BeforeClass
	public void databaseSetUp(){

		studentRepository.deleteAll();
		studentRepository.insert(new Student("Mahendra","Thapa"));
	}

	// get method
	@Test(groups = "get")
	public void getStudent() {
		given().when().get("/").then().statusCode(200);
	}

	// post method
	@Test(groups = "post")
	public void setStudent() {
		Map<String, String> student = new HashMap<>();
		student.put("firstName", "Bijay");
		student.put("lastName", "Gurung");

		token = given().contentType("application/json").body(student).when().post("/").then().extract().response()
				.path("_links.self.href");

	}

	// Patch method
	@Test(groups = "update", dependsOnGroups = "post")
	public void updateStudentPatch() {

		Map<String, String> student = new HashMap<>();
		student.put("firstName", "Tula");

		given().contentType("application/json").body(student).when().patch(token).then().statusCode(200);
	}

	// Put method
	@Test(groups = "update", dependsOnGroups = "post")
	public void updateStudentPut() {

		Map<String, String> student = new HashMap<>();
		student.put("firstName", "Sita");

		given().contentType("application/json").body(student).when().put(token).then().statusCode(200);
	}

	// delete method
	@Test(groups = "delete", dependsOnGroups = "update")
	public void deleteStudent() {

		given().when().delete(token).then().statusCode(204);

	}
}