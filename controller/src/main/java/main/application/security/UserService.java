package main.application.security;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import main.application.constants.Roles;
import main.application.entity.UserAccount;
import main.application.exceptions.NotAuthenticationException;
import main.application.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements UserDetailsService {

	UserRepository repository;

	/**
	 * Check user exists and generate new UserDetails
	 * 
	 * @param String
	 * @return UserDetails
	 * @throws NotAuthenticationException
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		UserAccount user = findByUsername(username).get();

		System.out.println(username+"  ===> "+user);
		Set<String> authorityList = new HashSet<>();
		authorityList.add("ROLE_" + user.getRole());
		return new User(user.getLogin(), user.getPassword(), AuthorityUtils.createAuthorityList(authorityList));
	}

	public Optional<UserAccount> findByUsername(String username) {
		return repository.findByLogin(username);
	}

	/**
	 * check user access for request
	 * 
	 * @param ownerId
	 * @return ResponseEntity(UserAccountDto,HttpStatus)
	 */
	public ResponseEntity<?> isUserValid(Long ownerId) {
		UserAccount user;
		try {
			user = repository.findById(ownerId).get();
		} catch (Exception e) {
			return new ResponseEntity<>(" user with id %d not found".formatted(ownerId), HttpStatus.NOT_FOUND);
		}
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		UserAccount currentUser = findByUsername(currentUserName).get();
		if (currentUserName == null || currentUser == null) {
			return new ResponseEntity<>("Wrong authorization, current user name header %d".formatted(ownerId),
					HttpStatus.UNAUTHORIZED);
		}
		if (!currentUserName.equals(user.getLogin())
				&& !currentUser.getRole().equals(Roles.ADMINISTRATOR.name())) {
			return new ResponseEntity<>(
					"incorrect name or role %s %s".formatted(currentUser.getRole(), currentUserName),
					HttpStatus.LOCKED);
		}
		return new ResponseEntity<>(user.convertToDto(), HttpStatus.OK);
	}

}
