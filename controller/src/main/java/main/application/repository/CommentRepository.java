package main.application.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import main.application.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	@Query(nativeQuery = true, value = "SELECT c.video_id from comments c where c.video_id=?1")
	List<Long> findAllByVideoId(Long id);

	@Query(nativeQuery = true, value = "select c.owner_id from comments c where c.owner_id=?1")
	List<Long> findAllByOwner(Long id);
	
	@Query(nativeQuery = true, value = "select * from comments c where c.video_id=?1")
	List<Comment> findVideoCommentsByVideoId(Long videoId);
}
