package main.application.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import main.application.dto.UserAccountDto;
import main.application.dto.VideoContentDto;
import main.application.entity.Comment;
import main.application.entity.VideoContent;
import main.application.exceptions.NotFoundException;

public interface IVideoService {
	/**
	 * return all videos from database
	 * 
	 * @return List VideoContent
	 * @throws
	 */
	List<VideoContent> getVideos();
	/**
	 * return 5 most popular videos
	 * 
	 * @return List VideoContentDto
	 * @throws
	 */
	List<VideoContent> getMostPopularVideos();
	
	/**
	 * return all user's data
	 * 
	 * @return List UserAccountDto
	 * @throws
	 */
	List<UserAccountDto> getUsers();
	
	/**
	 * return user data
	 * 
	 * @param Long
	 * @return UserAccount
	 * @throws NotFoundException
	 */
	ResponseEntity<?> getUser(Long id);
	
	/**
	 * add new user account
	 * 
	 * @param UserAccountDto, password
	 * @return ResponseEntity
	 * @throws
	 */
	ResponseEntity<?> addUser(UserAccountDto userAccountDto, String password);
	
	/**
	 * update user account data
	 * 
	 * @return ResponseEntity
	 * @throws NotFoundException
	 */
	ResponseEntity<?> updateUser(UserAccountDto userAccountDto);
	
	/**
	 * remove all comments and user account from database
	 * 
	 * @param Long
	 * @return ResponseEntity
	 * @throws NotFoundException
	 */
	ResponseEntity<?> removeUser(Long id);
	
	/**
	 * add video content
	 * 
	 * @param VideoContent videoContentDto
	 * @return ResponseEntity<>(VideoContentDto,HttpStatus)
	 * @throws
	 */
	ResponseEntity<?> addVideo(VideoContent videoContentDto);
	
	/**
	 * update video content
	 * 
	 * @param VideoContentDto videoContentDto
	 * @return ResponseEntity
	 * @throws NotFoundException
	 */
	ResponseEntity<?> updateVideo(VideoContentDto videoContentDto);
	
	/**
	 * remove all comments and video content from database
	 * 
	 * @param Long id
	 * @return ResponseEntity
	 * @throws NotFoundException
	 */
	ResponseEntity<?> removeVideo(Long id);
	
	/**
	 * return random VideoContentDto from database
	 * 
	 * @return VideoContentDto
	 * @throws
	 */
	VideoContentDto getRandomVideoContent();
	

	/**
	 * save comment to database
	 * 
	 * @param Comment comment
	 * @return void method
	 * @throws
	 */
	void addComment(Comment comment);

	ResponseEntity<?> deleteCommentById(Long commentId);
}
