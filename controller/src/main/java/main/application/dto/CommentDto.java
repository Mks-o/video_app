package main.application.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import main.application.entity.Comment;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
	@Schema(description = "Unique field id", type = "Long", example = "1")
	Long id;
	
	@Schema(description = "content must be in range [0, 120] symbols", type = "content", example = "De quelle manière ; par quel moyen.")
	@Size(min = 0, max = 120)
	@Nullable
	String content;
	
	@Valid
	public Comment convertToEntity(Long videoId) {
		return new Comment(id, content, null, videoId);
	}
}
