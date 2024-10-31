package main.application.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern.Flag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import main.application.dto.*;
import main.application.dto.jwt.*;
import main.application.entity.UserAccount;
import main.application.repository.UserRepository;
import main.application.security.TokenUtils;
import main.application.security.UserService;
import main.application.service.MainService;

@RestController
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

/*https://www.youtube.com/watch?v=NIv9TFTSIlg&list=WL&index=1*/

@Tag(name = "Authentication")
public class AuthController {

	TokenUtils tokenUtils;
	UserService userService;
	MainService mainService;
	UserRepository userRepository;
	
	@Operation(summary = "request for authenticate user and generate token", description = "authenticated/generate token/permit authenticated users", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtRequest.class)), description = "Returns ResponseEntity<>(JwtResponce, HttpStatusCode.OK)", headers = @Header(schema = @Schema(implementation = UserAccountDto.class), name = "UserAccountDto")),
			@ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtRequest.class)), description = "Returns ResponseEntity<>(String, HttpStatus.BAD_REQUEST)", headers = @Header(schema = @Schema(implementation = UserAccountDto.class), name = "UserAccountDto")) })

	@PostMapping("/authenticated/auth")
	@CrossOrigin
	public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {	
		UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
		String res = tokenUtils.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponce(res));
	}

	@Operation(summary = "request for create new user", description = "create new user/permit all", responses = @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDto.class)), description = "Returns ResponseEntity<>(UserAccountDto,HttpStatusCode.OK)", headers = @Header(schema = @Schema(implementation = UserAccountDto.class), name = "UserAccountDto")))
	@PostMapping("/registration/{password}")
	@CrossOrigin
	public ResponseEntity<?> createNewUser(@Validated @RequestBody UserAccountDto userDto,
			@Pattern(regexp = "[a-zA-Z0-9]{5,15}", flags = Flag.UNICODE_CASE, message = "Password must contains only a-z A-Z 0-9 characters and in range 5-15 symbols") @PathVariable String password) {
		return mainService.addUser(userDto, password);
	}

	@Operation(summary = "request for update password", description = "update password/permit for authenticated", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDto.class)), description = "Returns ResponseEntity<>(Password changed, HttpStatus.OK)", headers = @Header(schema = @Schema(implementation = UserAccountDto.class), name = "UserAccountDto")))
	@PostMapping("/authenticated/setpassword")
	@CrossOrigin
	public ResponseEntity<?> setPassword(@Valid @RequestBody UserPasswordDto userPasswordDto) {
		return mainService.setPassword(userPasswordDto);
	}

	
	@PostMapping("/registration/login")
	@CrossOrigin
	public ResponseEntity<?> Login(@RequestBody JwtRequest authRequest) {
		UserAccount userAccountDto = new UserAccount();
		try {
			userAccountDto = userRepository.findByLogin(authRequest.getUsername()).get();
	    } catch (Exception e) {
	        SecurityContextHolder.getContext().setAuthentication(null);
	        return new ResponseEntity<>("Login not happend",HttpStatus.BAD_REQUEST);
	    }
		return new ResponseEntity<>(userAccountDto.convertToDto(),HttpStatus.OK);
	}
}
