package main.application.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import main.application.entity.Comment;
import main.application.entity.VideoContent;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class VideoContentDto {
	@Schema(description = "Unique field id", type = "Long", example = "1")
	Long id;
	
	@Schema(description = "Unique field id", type = "String", example = "Chercher les pourquoi et les comment")
	@Size(max = 350, min = 1, message = "title must contains from 1 to 350 symbols")
	String title;
	
	@Schema(description = "Unique field id", type = "String", example = "Chercher les pourquoi et les comment")
	@Size(max = 550, min = 1, message = "description must contains from 1 to 550 symbols")
	String description;
	
	@Schema(description = "raiting must be in range [-2000000, 2000000]", type = "Integer", example = "0")
	@Min(value = -2000000, message = "raiting must be greater then -2000000")
	@Max(value = 2000000, message = "raiting must be less then 2000000")
	Integer raiting;
	
	@Schema(description = "user video contents", type = "Null", example = " ")
	 @Nullable
	List<Comment> comments;
	
	@Valid
	public VideoContentDto convertToDto() {
		return new VideoContentDto(id, title, description, raiting, comments);
	}

	@Valid
	public VideoContent convertToEntity(String url, Long ownerId) {
		return new VideoContent(id, title, description, url, raiting, ownerId, comments);
	}
}
