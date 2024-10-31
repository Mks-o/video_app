package main.application.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import main.application.dto.UserAccountDto;
import main.application.service.MainService;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Validated
@Tag(name = "UserAccount")
public class UserAccountController {
	
	MainService mainService;
	
	@Operation(summary = "get user by id", description = "get user/permit for user with same login or role administrator",
			responses = @ApiResponse(responseCode = "200", 
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = UserAccountDto.class)), 
			description = "Returns ResponseEntity<>(UserAccountDto, HttpStatus.OK)", 
			headers = @Header(schema = 
			@Schema(implementation = UserAccountDto.class), name = "UserAccountDto")))

	@GetMapping("/user/get/{id}")
	@CrossOrigin
	public ResponseEntity<?> getUser(@PathVariable Long id) {
		return mainService.getUser(id);
	}

	@Operation(summary = "update user account values", description = "update user/permit for user with same login or role administrator",
	responses = @ApiResponse(responseCode = "200", 
	content = @Content(mediaType = "application/json", 
	schema = @Schema(implementation = UserAccountDto.class)), 
	description = "Returns ResponseEntity<>(UserAccountDto, HttpStatus.OK)", 
	headers = @Header(schema = 
	@Schema(implementation = UserAccountDto.class), name = "UserAccountDto")))

	@Transactional
	@PostMapping("/user/updateuser")
	@CrossOrigin
	public ResponseEntity<?> updateUser(@Valid @RequestBody UserAccountDto userAccountDto) {
		try {
			log.debug("updateUser() -> Data received: {}",userAccountDto);
			System.out.println(userAccountDto);
			mainService.updateUser(userAccountDto);
			System.out.println("responsed");
			return new ResponseEntity<String>(userAccountDto.toString(),HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.getMessage()+e.getLocalizedMessage());
			return null;
		}
	}
	
	@Operation(summary = "delete user account", description = "delete user/permit for user with same login or role administrator",
	responses = @ApiResponse(responseCode = "200", 
	content = @Content(mediaType = "application/json", 
	schema = @Schema(implementation = UserAccountDto.class)), 
	description = "Returns ResponseEntity<>(UserAccountDto, HttpStatus.OK)", 
	headers = @Header(schema = 
	@Schema(implementation = UserAccountDto.class), name = "UserAccountDto")))
	@CrossOrigin
	@DeleteMapping("/user/delete/{userid}")
	public ResponseEntity<?> deleteUser(@PathVariable Long userid) {
		return mainService.removeUser(userid);
	}
	
	@Operation(summary = "get list of all users", description = "all users list/permit for authenticated users",
	responses = @ApiResponse(responseCode = "200", 
	content = @Content(mediaType = "application/json", 
	schema = @Schema(implementation = UserAccountDto.class)), 
	description = "Returns ResponseEntity<>(List<UserAccountDto>, HttpStatus.OK)", 
	headers = @Header(schema = 
	@Schema(implementation = List.class), name = "UserAccountDto")))
	
	@GetMapping("/user/getusers")
	@CrossOrigin
	public List<UserAccountDto> getUsers() {
		return mainService.getUsers();
	}
	
}
