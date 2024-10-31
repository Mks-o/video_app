package main.application.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import main.application.dto.UserAccountDto;
import main.application.source.ValidRoles;

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
@ToString
@Schema
public class UserAccount implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(unique = true)
	@Schema(description = "Unique field id", type = "Long", example = "1")
	Long id;
	
	@Size(max=20, min=5, message = "Name must contains from 5 to 20 symbols")
	@NotEmpty
	String name;

	@Size( max=20, min=5, message = "Login must contains from 5 to 20 symbols")
	@Schema(description = "Not empty field must contains from 5 to 20 symbols", type = "String", example = "someLogin")
	@NotEmpty
	@Column(unique = true)
	String login;

	@Size( max=80, min=5, message = "Password must contains only a-z A-Z 0-9 characters and in range 5-15 symbols")
	@Schema(description = "Password must contains only a-z A-Z 0-9 characters and in range 5-15 symbols", type = "String", example = "str284rtsZ")
	@Setter
	String password;
	
	@Schema(description = "mail", type = "String", example = "name@gmail.il")
	String mail;

	@Schema(description = "role [USER, ADMINISTRATOR]", type = "Enum", example = "USER",allowableValues = {"USER", "ADMINISTRATOR","GUEST"})
	@NotEmpty
	@ValidRoles
	String role;
	LocalDate birthdate;
	
	@Schema(description = "raiting must be in range [-2000000, 2000000]", type = "Integer", example = "0")
	@Min(value = -2000000, message = "raiting must be greater then -2000000")
	@Max(value = 2000000, message = "raiting must be less then 2000000")
	Integer raiting;
	
	@OneToMany(mappedBy = "ownerId",
			targetEntity = VideoContent.class)
	@Schema(description = "user video content", type = "Null", example = " ")
	@Setter
	@Nullable
	List<VideoContent> videos;
	
	@OneToMany(mappedBy = "ownerId",
			targetEntity = Comment.class)
	@Setter
	@Schema(description = "user comments", type = "Null", example = " ")
	@Nullable
	List<Comment> comments;

	@Valid
	public UserAccountDto convertToDto() {
		return new UserAccountDto(id, name, login, mail, role,  birthdate, raiting, videos, comments);
	}

	public UserAccount update(UserAccountDto userAccountDto) {
		this.name = userAccountDto.getName() == null ? name : userAccountDto.getName();
		this.login = userAccountDto.getLogin() == null ? name : userAccountDto.getLogin();
		this.role = userAccountDto.getRole() == null ? role : userAccountDto.getRole();
		this.mail = userAccountDto.getMail() == null ? mail : userAccountDto.getMail();
		this.birthdate = userAccountDto.getBirthdate() == null ? birthdate : userAccountDto.getBirthdate();
		this.raiting = userAccountDto.getRaiting() == null ? raiting : userAccountDto.getRaiting();
		this.videos = userAccountDto.getVideos() == null ? videos : userAccountDto.getVideos();
		this.comments = userAccountDto.getComments() == null ? comments : userAccountDto.getComments();
		return this;
	}
}
