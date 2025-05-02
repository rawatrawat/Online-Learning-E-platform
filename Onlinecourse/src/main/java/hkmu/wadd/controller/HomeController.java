package hkmu.wadd.controller;

import hkmu.wadd.model.*;
import hkmu.wadd.service.CourseService;
import hkmu.wadd.service.PollService;
import hkmu.wadd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private PollService pollService;

    @Autowired
    private UserService userService;
    public HomeController(CourseService courseService,
                          PollService pollService,
                          UserService userService) {
        this.courseService = courseService;
        this.pollService = pollService;
        this.userService = userService;
    }
    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal User user) {
        List<Course> courses = courseService.getAllCourses();
        List<Poll> polls = pollService.getAllPolls();
        model.addAttribute("courses", courses);
        model.addAttribute("polls", polls);

        if (user != null) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("user", user);
            return "index-logged-in";
        }
        return "index";
    }

    @GetMapping("/course/new")
    public String showCreateCourseForm(Model model, @AuthenticationPrincipal User user) {
        if (user == null || !"TEACHER".equals(user.getRole())) {
            return "redirect:/";
        }
        List<Course> courses = courseService.getAllCourses();
        List<Poll> polls = pollService.getAllPolls();
        model.addAttribute("courses", courses);
        model.addAttribute("polls", polls);
        model.addAttribute("course", new Course());
        return "create-course";
    }

    @PostMapping("/course/create")
    public String createCourse(@ModelAttribute Course course, @AuthenticationPrincipal User user, Model model) {
        if (user == null || !"TEACHER".equals(user.getRole())) {
            return "redirect:/";
        }
        List<Course> courses = courseService.getAllCourses();
        List<Poll> polls = pollService.getAllPolls();
        model.addAttribute("courses", courses);
        model.addAttribute("polls", polls);

        try {
            courseService.saveCourse(course);
            return "redirect:/?success=Course+created+successfully";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create course: " + e.getMessage());
            return "create-course";
        }
    }

    @GetMapping("/voting-history")
    public String votingHistory(Model model, @AuthenticationPrincipal User user) {
        if (user == null) {
            return "redirect:/login";
        }

        List<Course> courses = courseService.getAllCourses();
        List<Poll> polls = pollService.getAllPolls();
        model.addAttribute("courses", courses);
        model.addAttribute("polls", polls);

        List<Vote> votingHistory = userService.getVotingHistory(user.getId());
        model.addAttribute("votingHistory", votingHistory);
        return "voting-history";
    }

    @GetMapping("/poll-comment-history")
    public String pollCommentHistory(Model model, @AuthenticationPrincipal User user) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        List<Poll> polls = pollService.getAllPolls();
        model.addAttribute("polls", polls);


        if (user == null) {
            return "redirect:/login";
        }

        List<PollComment> pollCommentHistory = userService.getPollCommentHistory(user.getId());
        model.addAttribute("pollCommentHistory", pollCommentHistory);
        return "poll-comment-history";
    }

    @GetMapping("/course-comment-history")
    public String courseCommentHistory(Model model, @AuthenticationPrincipal User user) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);

        List<Poll> polls = pollService.getAllPolls();
        model.addAttribute("polls", polls);


        if (user == null) {
            return "redirect:/login";
        }

        List<Comment> courseCommentHistory = userService.getCommentHistory(user.getId());
        model.addAttribute("courseCommentHistory", courseCommentHistory);
        return "course-comment-history";
    }
}
