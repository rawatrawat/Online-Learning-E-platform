package hkmu.wadd.service;

import hkmu.wadd.model.Comment;
import hkmu.wadd.model.PollComment;
import hkmu.wadd.model.User;
import hkmu.wadd.model.Vote;
import hkmu.wadd.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User updateUser(User user) {

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            User existingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setPassword(existingUser.getPassword());
        }
        userRepository.save(user);
        return user;
    }

    @Transactional
    public List<Vote> getVotingHistory(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getVotes();
        }
        return null;
    }

    @Transactional
    public List<PollComment> getPollCommentHistory(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getPollComments();
        }
        return null;
    }

    @Transactional
    public List<Comment> getCommentHistory(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getLectureComments();
        }
        return null;
    }
    public User getUserById(long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
