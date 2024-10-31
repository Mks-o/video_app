package main.application.entity;



import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import main.application.dto.CommentDto;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "comments")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Schema(description = "Unique field id", type = "Long", example = "1")
	@Column(unique = true)
	Long id;
	
	@Schema(description = "content must be in range [0, 520] symbols", type = "content", example = "De quelle manière ; par quel moyen.")
	@Size(min = 0, max = 520)
	@Nullable
	String content;
	
	@Schema(description = "Unique field ownerId", type = "Long", example = "1")
	@Setter
	Long ownerId;
	
	@Schema(description = "Unique field videoId", type = "Long", example = "1")
	@Setter
	Long videoId;
	
	@Valid
	public CommentDto convertToDto() {
		return new CommentDto(id, content);
	}
}
