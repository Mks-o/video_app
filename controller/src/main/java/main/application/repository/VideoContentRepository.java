package main.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import main.application.entity.VideoContent;

public interface VideoContentRepository extends JpaRepository<VideoContent, Long>{
	
	@Query(nativeQuery = true, 
			value = "select * from videos  order by raiting desc limit 5")
	List<VideoContent> findMostPopularVideos();
	
	@Query(nativeQuery = true, value = "SELECT max(id) FROM USERS")
	Long findMaxId();
	
	@Query(nativeQuery = true, value="SELECT * FROM videos\r\n"
			+ "ORDER BY RAND()\r\n"
			+ "LIMIT 2")
	List<VideoContent> findRandomVideos();
	
	@Query(nativeQuery = true, value="SELECT * FROM videos\r\n"
			+ "ORDER BY RAND()\r\n"
			+ "LIMIT 1")
	VideoContent findNextVideo();

	List<VideoContent> findAllByOwnerId(Long userId);

}
