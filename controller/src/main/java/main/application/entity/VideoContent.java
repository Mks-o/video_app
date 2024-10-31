package main.application.entity;

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
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import main.application.dto.VideoContentDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Table(name = "videos")
public class VideoContent {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Schema(description = "Unique field id", type = "Long", example = "1")
	@Column(unique = true)
	Long id;
	
	@Schema(description = "Unique field id", type = "String", example = "Chercher les pourquoi et les comment")
	@Size(max = 350, min = 1, message = "title must contains from 1 to 350 symbols")
	String title;
	
	@Schema(description = "Unique field id", type = "String", example = "Chercher les pourquoi et les comment")
	@Size(max = 550, min = 1, message = "description must contains from 1 to 550 symbols")
	String description;
	
	@Schema(description = "url of video content", type = "String", example = "https://www.youtube.com/watch?v=ZDZiXmCl4pk")
	String url;
	
	@Schema(description = "raiting must be in range [-2000000, 2000000]", type = "Integer", example = "0")
	@Min(value = -2000000, message = "raiting must be greater then -2000000")
	@Max(value = 2000000, message = "raiting must be less then 2000000")
	Integer raiting;
	
	@Schema(description = "user video contents", type = "Null", example = " ")
	 @Nullable
	@Setter
	Long ownerId;
	@OneToMany(mappedBy = "videoId",
			targetEntity = Comment.class)
	List<Comment> comments;

	
	public void update(VideoContentDto videoContentDto) {
		title = videoContentDto.getTitle() == null ? title : videoContentDto.getTitle();
		description = videoContentDto.getDescription() == null ? description : videoContentDto.getDescription();
		comments = videoContentDto.getComments() == null ? comments : videoContentDto.getComments();
		raiting = videoContentDto.getRaiting() == null ? raiting : videoContentDto.getRaiting();
	}

	@Valid
	public VideoContentDto convertToDto() {
		return new VideoContentDto(id, title, description, raiting, comments);
	}

}
