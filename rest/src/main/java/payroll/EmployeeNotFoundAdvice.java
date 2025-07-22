package payroll;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class EmployeeNotFoundAdvice {

	@ExceptionHandler(EmployeeNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	Map<String, String> employeeNotFoundHandler(EmployeeNotFoundException ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", "Employee not found");
		error.put("message", ex.getMessage());
		return error;
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	Map<String, String> illegalArgumentHandler(IllegalArgumentException ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", "Invalid request");
		error.put("message", ex.getMessage());
		return error;
	}
}
