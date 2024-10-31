package main.application.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
import main.application.dto.*;
import main.application.entity.*;
import main.application.service.MainService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
@Tag(name = "VideoContent")
public class VideoContentController {

	MainService mainService;

	@Operation(summary = "get list of all videos", description = "all videos list/permit for authenticated users", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)), description = "Returns List<VideoContent>", headers = @Header(schema = @Schema(implementation = List.class), name = "List<VideoContent>")))
	@CrossOrigin
	@GetMapping("/video/get/videos")
	public List<VideoContent> getVideos() {
		return mainService.getVideos();
	}

	@CrossOrigin
	@GetMapping("/video/get/uservideo/{userId}")
	public ResponseEntity<?> getUserVideo(@PathVariable Long userId) {
		return mainService.getUservideo(userId);
	}

	@Operation(summary = "get video by id", description = "get video/permit for authenticated users", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)), description = "Returns ResponseEntity<>(VideoContent, HttpStatus.OK)", headers = @Header(schema = @Schema(implementation = ResponseEntity.class), name = "ResponseEntity")))
	@CrossOrigin
	@GetMapping("/video/get/video/{videoId}")
	public ResponseEntity<?> getVideo(@PathVariable Long videoId) {
		return mainService.getvideo(videoId);
	}

	@Operation(summary = "get 2 random videos", description = "random videos/permit for authenticated users", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)), description = "Returns ResponseEntity<>(List<VideoContent>, HttpStatus.OK)", headers = @Header(schema = @Schema(implementation = ResponseEntity.class), name = "ResponseEntity")))
	@CrossOrigin
	@GetMapping("/video/get/randomvideos")
	public List<VideoContent> getRandomVideos() {
		return mainService.getRandomVideos();
	}

	@Operation(summary = "get next video", description = "random video/permit for authenticated users", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VideoContent.class)), description = "Returns VideoContent", headers = @Header(schema = @Schema(implementation = VideoContent.class), name = "VideoContent")))
	@CrossOrigin
	@GetMapping("/video/get/nextvideo")
	public VideoContent getNextVideo() {
		return mainService.getNextVideo();
	}

	@Operation(summary = "get top 5 rated videos", description = "rated videos/permit for authenticated users", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)), description = "Returns List<VideoContent>", headers = @Header(schema = @Schema(implementation = List.class), name = "List<VideoContent>")))
	@CrossOrigin()
	@GetMapping("/video/get/popularvideos")
	public List<VideoContent> getMostPopularVideos() {
		return mainService.getMostPopularVideos();
	}

	@Operation(summary = "add new video", description = "add video/permit for authenticated users", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)), description = "Returns ResponseEntity", headers = @Header(schema = @Schema(implementation = ResponseEntity.class), name = "ResponseEntity")))
	@CrossOrigin
	@PostMapping("/video/add")
	public ResponseEntity<?> addVideo(@RequestBody VideoContent video) {
		return mainService.addVideo(video);
	}

	@Operation(summary = "update video", description = "update video/permit for authenticated users", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)), description = "Returns ResponseEntity", headers = @Header(schema = @Schema(implementation = ResponseEntity.class), name = "ResponseEntity")))
	@CrossOrigin
	@PostMapping("/video/update")
	public ResponseEntity<?> updateVideo(@RequestBody VideoContentDto videoContentDto) {
		return mainService.updateVideo(videoContentDto);
	}

	@Operation(summary = "delete video", description = "delete video/permit for authenticated users", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)), description = "Returns ResponseEntity", headers = @Header(schema = @Schema(implementation = ResponseEntity.class), name = "ResponseEntity")))
	@CrossOrigin
	@DeleteMapping("/video/delete/{videoId}")
	public ResponseEntity<?> removeVideo(@PathVariable Long videoId) {
		return mainService.removeVideo(videoId);
	}

	@Operation(summary = "add new comment to video", description = "add new comment/permit for authenticated users", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class)), description = "Add new Comment to database", headers = @Header(schema = @Schema(implementation = Comment.class), name = "Comment")))
	@CrossOrigin
	@PostMapping("/video/addcomment")
	public void addComment(@Valid @RequestBody Comment comment) {
		System.out.println(comment);
		mainService.addComment(comment);
	}

	@Operation(summary = "get comments by video id", description = "get comments by video id/permit for authenticated users", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)), description = "Get all comments by video id from database", headers = @Header(schema = @Schema(implementation = Long.class), name = "VideoId")))
	@CrossOrigin
	@GetMapping("/video/getcomments/{videoId}")
	public ResponseEntity<?> getComments(@PathVariable Long videoId) {
		return mainService.getCommentsByVideoId(videoId);
	}

	@Operation(summary = "get comments", description = "get all comments/permit for authenticated users", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)), description = "Get all comments by video id from database"))
	@CrossOrigin
	@GetMapping("/comments/getall")
	public List<Comment> getComments() {
		return mainService.getAllComments();
	}

	@Operation(summary = "delete comment by id", description = "delete comment by id/permit for authenticated users", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)), description = "Delete comment by id", headers = @Header(schema = @Schema(implementation = Long.class), name = "Id")))
	@CrossOrigin
	@DeleteMapping("/comments/delete/{commentId}")
	public ResponseEntity<?> deleteCommentById(@PathVariable Long commentId) {
		return mainService.deleteCommentById(commentId);
	}
}
