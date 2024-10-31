package main.application.service;

import static main.application.constants.Roles.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import main.application.constants.Constants;
import main.application.dto.*;
import main.application.entity.*;
import main.application.exceptions.*;
import main.application.repository.*;
import main.application.security.UserService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainService implements IVideoService {

	VideoContentRepository serviceRepository;
	CommentRepository commentRepository;
	UserRepository userRepository;
	UserService userService;

	@Override
	public List<VideoContent> getVideos() {
		return serviceRepository.findAll();
	}

	public ResponseEntity<?> getUservideo(Long userId) {
		return new ResponseEntity<>(serviceRepository.findAllByOwnerId(userId), HttpStatus.OK);
	}

	@Override
	public VideoContentDto getRandomVideoContent() {
		Long id = serviceRepository.findMaxId();
		Long randomIndex = new Random().nextLong(id);
		return (serviceRepository.findById(randomIndex).orElse(serviceRepository.findById(id).get())).convertToDto();
	}

	/**
	 * return List of 2 VideoContent random video from database
	 * 
	 * @return List VideoContent
	 * @throws
	 */
	public List<VideoContent> getRandomVideos() {
		return serviceRepository.findRandomVideos();
	}

	/**
	 * return random video from database
	 * 
	 * @return VideoContent
	 * @throws
	 */
	public VideoContent getNextVideo() {
		return serviceRepository.findNextVideo();
	}

	/**
	 * return video by id
	 * 
	 * @param Long
	 * @return ResponseEntity
	 * @throws
	 */
	public ResponseEntity<?> getvideo(Long videoId) {
		return new ResponseEntity<>(serviceRepository.findById(videoId).get(), HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<?> addUser(UserAccountDto userAccountDto, String password) {

		if (userRepository.existsByLogin(userAccountDto.getLogin())) {
			return new ResponseEntity<>("User with login %s already exists".formatted(userAccountDto.getLogin()),
					HttpStatus.CONFLICT);
		}
		if (passwordNotValid(password)) {
			return new ResponseEntity<>("Invalid password", HttpStatus.NOT_ACCEPTABLE);
		}
		UserAccount user = userAccountDto.convertToEntity(BCrypt.hashpw(password, BCrypt.gensalt()));
		userRepository.save(user);
		return new ResponseEntity<>(user.convertToDto(), HttpStatus.CREATED);
	}

	/**
	 * check password validation
	 * 
	 * @return boolean
	 * @throws
	 */
	private boolean passwordNotValid(String password) {
		return password.length() < 7;
	}

	/**
	 * change user password
	 * 
	 * @param UserPasswordDto
	 * @return ResponseEntity
	 * @throws
	 */
	@Transactional
	public ResponseEntity<?> setPassword(UserPasswordDto userPasswordDto) {
		ResponseEntity<?> validresponseEntity = userService.isUserValid(userPasswordDto.getUserId());
		if (validresponseEntity.getStatusCode() == HttpStatus.OK) {
			UserAccount account = userRepository.findById(userPasswordDto.getUserId()).orElseThrow(
					() -> new NoContentException("User with id %d not exists".formatted(userPasswordDto.getUserId())));

			if (account == null) {
				return new ResponseEntity<>("User with id %d not exists".formatted(userPasswordDto.getUserId()),
						HttpStatus.UNAUTHORIZED);
			}
			if (BCrypt.checkpw(userPasswordDto.getPassword(), account.getPassword())
					|| passwordNotValid(userPasswordDto.getPassword()))
				return new ResponseEntity<>("Password not valid", HttpStatus.UNAUTHORIZED);

			account.setPassword(BCrypt.hashpw(userPasswordDto.getPassword(), BCrypt.gensalt()));
			userRepository.save(account);
			return new ResponseEntity<>("Password changed", HttpStatus.OK);
		}
		return validresponseEntity;
	}

	@Override
	public List<VideoContent> getMostPopularVideos() {
		List<VideoContent> content = serviceRepository.findMostPopularVideos();
		return content;
	}

	@Override
	public List<UserAccountDto> getUsers() {
		List<UserAccount> usersList = userRepository.findAll();
		return usersList.stream().map(u -> u.convertToDto()).collect(Collectors.toList());
	}

	@Override
	public ResponseEntity<?> getUser(Long id) {
		ResponseEntity<?> validresponseEntity = userService.isUserValid(id);
		if (validresponseEntity.getStatusCode() == HttpStatus.OK) {
			UserAccount user = userRepository.findById(id).get();
			return new ResponseEntity<>(user.convertToDto(), HttpStatus.OK);
		}
		return validresponseEntity;
	}

	@Override
	@Transactional
	public ResponseEntity<?> updateUser(UserAccountDto userAccountDto) {
		Long id = userAccountDto.getId();
		ResponseEntity<?> validresponseEntity = userService.isUserValid(id);
		if (validresponseEntity.getStatusCode() == HttpStatus.OK) {
			UserAccount user = userRepository.findById(id).get();
			user.update(userAccountDto);
			userRepository.save(user);
			return new ResponseEntity<>(user.convertToDto(), HttpStatus.OK);
		}
		return validresponseEntity;
	}

	@Override
	@Transactional
	public ResponseEntity<?> removeUser(Long id) {
		ResponseEntity<?> validresponseEntity = userService.isUserValid(id);
		if (validresponseEntity.getStatusCode() == HttpStatus.OK) {
			List<Long> commentsIds = commentRepository.findAllByOwner(id);
			commentRepository.deleteAllById(commentsIds);
			userRepository.deleteById(id);
			return new ResponseEntity<>("Deleted", HttpStatus.OK);
		}
		return validresponseEntity;
	}

	@Override
	@Transactional
	public ResponseEntity<?> addVideo(VideoContent video) {
		serviceRepository.save(video);
		return new ResponseEntity<>(video.convertToDto(), HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<?> updateVideo(VideoContentDto videoContentDto) {
		VideoContent video = serviceRepository.findById(videoContentDto.getId()).get();
		video.update(videoContentDto);
		return new ResponseEntity<>(video.convertToDto(), HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<?> removeVideo(Long videoId) {
		VideoContent video = serviceRepository.findById(videoId).get();
		serviceRepository.findById(videoId).get();
		List<Long> commentsIds = commentRepository.findAllByVideoId(video.getId());
		commentRepository.deleteAllById(commentsIds);
		serviceRepository.deleteById(videoId);
		return new ResponseEntity<>("Video with id %d was removed".formatted(videoId), HttpStatus.OK);
	}

	/**
	 * start's in configuration and generate random users, comments and video
	 * content's
	 * 
	 * @param
	 * @return void method
	 * @throws IOException, ParserConfigurationException, SAXException
	 */
	@Transactional
	public void generateRandomContent() throws IOException, ParserConfigurationException, SAXException {
		if (userRepository.findMaxId() != null)
			return;
		List<Comment> comments = generateRandomComments(1L, 3L);
		UserAccount adminAccount = new UserAccount(1L, "Admin", "Admin", BCrypt.hashpw("Admin", BCrypt.gensalt()),
				"Admin" + "@mail.il", ADMINISTRATOR.toString(), LocalDate.now().minusYears(80),
				RandomRaiting(-5, 5), null, comments);
		userRepository.save(adminAccount);
		comments.forEach(c -> addComment(c));

		List<VideoContent> content = parseUtubeUrl(adminAccount.getId());
		content.forEach(System.out::println);
		serviceRepository.saveAllAndFlush(content);
		for (int i = 0; i < Constants.USERS_COUNT; i++) {
			generateRandomUsers(content);
		}
	}

	/**
	 * generate random raiting in range
	 * 
	 * @param Integer, Integer
	 * @return void method
	 * @throws IOException, ParserConfigurationException, SAXException
	 */
	private Integer RandomRaiting(int min, int max) {
		return new Random().nextInt(min, max);
	}

	/**
	 * generate random content, parse google.com and get videos url's. Create user's
	 * account's with role User and generate some random comments fill SQL database
	 * 
	 * @param List<VideoContent> content
	 * @return void method
	 * @throws IOException, ParserConfigurationException, SAXException
	 */
	@SuppressWarnings("null")
	@Transactional
	private void generateRandomUsers(List<VideoContent> content) {
		String login = randomWord(12, false);
		String password = randomWord(14, true);
		UserAccount user = new UserAccount(null, randomWord(8, false), login, password,
				randomWord(8, true) + "@mail.il", USER.toString(),
				LocalDate.now(), RandomRaiting(0, 10), null, null);

		UserAccountDto response = (UserAccountDto) addUser(user.convertToDto(), password).getBody();
		Long id = response.getId();
		List<VideoContent> randomVideosData = content.subList(0, new Random().nextInt(content.size()));
		randomVideosData.forEach(v -> v.setOwnerId(id));
		serviceRepository.saveAllAndFlush(randomVideosData);
		Long randomCommentVideoId = content.get(new Random().nextInt(0, content.size())).getId();
		List<Comment> comments = generateRandomComments(id, randomCommentVideoId);
		response.setVideos(randomVideosData);
		response.setComments(comments);
		commentRepository.saveAllAndFlush(comments);
		comments.forEach(comment -> comment.setOwnerId(id));
	}

	/**
	 * add new comment
	 * 
	 * @param Comment comment
	 * @return
	 */
	@Override
	@Transactional
	public void addComment(Comment comment) {
		commentRepository.save(comment);
	}

	/**
	 * generate random comment for the user account and video content
	 * 
	 * @param UserAccount userAccount, VideoContent videoContent
	 * @return List Comment
	 * @throws
	 */
	@Transactional
	private List<Comment> generateRandomComments(Long userId, Long videoId) {
		boolean enableDigits = false;
		List<Comment> comments = new ArrayList<>();

		for (int i = 0; i < Constants.COMMENTS_COUNT; i++) {
			Comment comment = new Comment(null, randomWord(Constants.COMMENT_LENGTH, enableDigits), userId, videoId);
			comments.add(comment);
		}
		return comments;
	}

	/**
	 * generate random word
	 * 
	 * @param Integer (length of result), boolean (enable digits)
	 * @return String random generated
	 */
	private String randomWord(int length, boolean numbers) {

		StringBuilder res = new StringBuilder();
		Random rand = new Random();
		for (int j = 0; j < length; j++) {
			char c = (char) rand.nextInt(64, 90);
			if (numbers && new Random().nextBoolean() == true)
				c = (char) rand.nextInt(48, 57);
			res.append(c);
		}
		return res.charAt(0) + res.substring(1, res.length()).toLowerCase();
	}

	/**
	 * parse google.com and get video url's content
	 * 
	 * @param Long (id of some user account)
	 * @return List VideoContent
	 * @throws IOException, ParserConfigurationException, SAXException
	 */
	@Transactional
	public List<VideoContent> parseUtubeUrl(Long id) throws IOException, ParserConfigurationException, SAXException {
		List<VideoContent> res = new ArrayList<>();
		int step = 10;
		String link = Constants.SOURCE_URL;
		for (int i = step; i <= Constants.PAGES_COUNT; i += step) {
			System.out.println("parse link:--->" + link);
			Document doc = Jsoup.connect(link).timeout(Constants.JSOP_TIMEOUT).get();
			doc.outputSettings().charset("ISO-8859-1");
			Document d = Jsoup.parse(doc.toString());
			Elements s = d.select("div.nhaZ2c");
			s.forEach(x -> {
				String title = x.text().replaceAll("[^\\x00-\\xFF]+", " ?");
				String url = x.select("a").attr("href").contains("https://www.youtube.com/watch?")
						? x.select("a").attr("href")
						: "";
				VideoContent video = new VideoContent(null, title, randomWord(30, false), url,
						0, id, null);
				if (!video.getUrl().isBlank())
					res.add(video);
			});
			link = link.replace("&start=" + i, "&start=" + (i + step));
		}
		return res;
	}

	/**
	 * return list of all comments
	 * 
	 * @param
	 * @return List Comment
	 * @throws
	 */
	public List<Comment> getAllComments() {
		return commentRepository.findAll();
	}

	/**
	 * return list of all comments by video id
	 * 
	 * @param Long videoId
	 * @return List Comment
	 * @throws
	 */
	public ResponseEntity<?> getCommentsByVideoId(Long videoId) {
		return new ResponseEntity<>(commentRepository.findVideoCommentsByVideoId(videoId), HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<?> deleteCommentById(Long commentId) {
		commentRepository.deleteById(commentId);
		return new ResponseEntity<>("Comment with id %d was removed".formatted(commentId), HttpStatus.OK);
	}

}
