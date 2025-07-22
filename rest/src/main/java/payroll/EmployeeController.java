package payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

// tag::hateoas-imports[]
// end::hateoas-imports[]
@CrossOrigin(origins = "http://localhost:3000")
@RestController
class EmployeeController {

	private final EmployeeRepository repository;

	EmployeeController(EmployeeRepository repository) {
		this.repository = repository;
	}

	// Aggregate root

	// tag::get-aggregate-root[]
	@GetMapping("/employees")
	CollectionModel<EntityModel<Employee>> all() {

		List<EntityModel<Employee>> employees = repository.findAll().stream()
				.map(employee -> EntityModel.of(employee,
						linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
						linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
				.collect(Collectors.toList());

		return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
	}
	// end::get-aggregate-root[]

	@PostMapping("/employees")
	Employee newEmployee(@RequestBody Employee newEmployee) {
		if (newEmployee.getId() != null) {
			throw new IllegalArgumentException("New employee should not have an ID");
		}
		if (newEmployee.getName() == null || newEmployee.getRole() == null)
			throw new IllegalArgumentException("New employee must have a name and role");
		if (newEmployee.getName().isBlank() || newEmployee.getRole().isBlank())
			throw new IllegalArgumentException("New employee name and role must not be blank");
		if (repository.findByName(newEmployee.getName()).isPresent())
			throw new IllegalArgumentException("Employee with name '" + newEmployee.getName() + "' already exists");
		return repository.save(newEmployee);
	}

	// Single itemxxz

	// tag::get-single-item[]
	@GetMapping("/employees/{id}")
	EntityModel<Employee> one(@PathVariable Long id) {

		Employee employee = repository.findById(id) //
				.orElseThrow(() -> new EmployeeNotFoundException(id));

		return EntityModel.of(employee, //
				linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
				linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
	}
	// end::get-single-item[]

	@PutMapping("/employees/{id}")
	Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

		return repository.findById(id) //
				.map(employee -> {
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				}) //
				.orElseGet(() -> {
					return repository.save(newEmployee);
				});
	}

	@DeleteMapping("/employees/{id}")
	Map<String, String> deleteEmployee(@PathVariable Long id) {
		if (!repository.existsById(id)) {
			throw new EmployeeNotFoundException(id);
		}

		repository.deleteById(id);

		Map<String, String> response = new HashMap<>();
		response.put("message", "Employee with id " + id + " successfully deleted");
		return response;
	}
}
