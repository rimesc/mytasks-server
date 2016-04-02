package uk.co.zoneofavoidance.my.tasks.controllers.rest;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static uk.co.zoneofavoidance.my.tasks.security.SecurityConfiguration.ADMIN_ROLE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.zoneofavoidance.my.tasks.controllers.UserForm;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.BindingErrorsResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.ErrorResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.UserResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.UsersResponse;
import uk.co.zoneofavoidance.my.tasks.services.UserService;
import uk.co.zoneofavoidance.my.tasks.services.UsernameAlreadyExistsException;

@RestController
@RequestMapping(path = "/api/admin")
public class AdminRestController {

   @Autowired
   private UserService users;

   @RequestMapping(path = "users", method = GET, produces = "application/json")
   public UsersResponse getUsers() {
      return new UsersResponse(users.getUsers().stream().map(this::convert).collect(toList()));
   }

   @RequestMapping(path = "users/current", method = GET, produces = "application/json")
   public UserResponse getCurrentUser(@AuthenticationPrincipal final User user) {
      return convert(user);
   }

   @RequestMapping(path = "users", method = POST, consumes = "application/json", produces = "application/json")
   public UserResponse postUsers(@Valid @RequestBody final UserForm userForm) {
      return convert(users.createUser(userForm.getUsername(), userForm.getPassword(), userForm.getAuthorities().contains(ADMIN_ROLE)));
   }

   private UserResponse convert(final UserDetails user) {
      return new UserResponse(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList()));
   }

   @ExceptionHandler(UsernameAlreadyExistsException.class)
   public ResponseEntity<ErrorResponse> handleUserAlreadyExists(final UsernameAlreadyExistsException ex) {
      return new ResponseEntity<>(ErrorResponse.create(BAD_REQUEST, ex.getMessage()), BAD_REQUEST);
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<BindingErrorsResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
      return new ResponseEntity<>(BindingErrorsResponse.create(ex.getBindingResult()), BAD_REQUEST);
   }

}
