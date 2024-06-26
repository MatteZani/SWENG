package com.sweng;

import com.sweng.entity.User;
import com.sweng.utilities.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

@SpringBootTest
class RegistrationTest {

	@Mock
	private UserService userService;

	@Test
	void userCorrectlyCreated() {

		//given
		User expected = new User(1, "Pippo", "Franco");
		ResponseEntity<Object> expectedResponse = new ResponseEntity(expected, HttpStatus.OK);

		//when
		when(userService.saveUser(expected)).thenReturn(new ResponseEntity<>(expected, HttpStatus.OK));
		ResponseEntity<Object> actualResponse = userService.saveUser(expected);

		//then
		Assertions.assertEquals(expectedResponse, actualResponse);

	}

	@Test
	void userMissingData() {

		//given
		User invalidUser = new User(1, null, null);
		final int expectedStatusCode = 400;

		// Configure mock behavior
		when(userService.saveUser(invalidUser)).thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

		//when
		ResponseEntity<Object> response = userService.saveUser(invalidUser);

		//then
		Assertions.assertEquals(expectedStatusCode, response.getStatusCode().value());

	}
}

