package hkmu.wadd.controller;

import hkmu.wadd.model.Course;
import hkmu.wadd.model.Poll;
import hkmu.wadd.model.User;
import hkmu.wadd.model.Vote;
import hkmu.wadd.repository.VoteRepository;
import hkmu.wadd.service.CourseService;
import hkmu.wadd.service.PollService;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/poll")
public class PollController {

    @Autowired
    private PollService pollService;
    private final CourseService courseService;
    private final VoteRepository voteRepository;
    public PollController(PollService pollService, CourseService courseService, VoteRepository voteRepository) {
        this.pollService = pollService;
        this.courseService = courseService;
        this.voteRepository = voteRepository;
    }
    @GetMapping("/")
    public String pollList(
            @RequestParam(required = false) String success,
            @RequestParam(required = false) String error,
            Model model,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return "redirect:/login";
        }

        List<Poll> polls = pollService.getAllPolls();
        List<Course> courses = courseService.getAllCourses();

        model.addAttribute("polls", polls);
        model.addAttribute("courses", courses);
        model.addAttribute("user", user);

        if (success != null) {
            model.addAttribute("success", success);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }

        return "poll";
    }
    @GetMapping("/new")
    public String newPollForm(Model model, @AuthenticationPrincipal User user) {
        if (user == null) {
            return "redirect:/login";
        }

        List<Course> courses = courseService.getAllCourses();
        List<Poll> polls = pollService.getAllPolls();
        model.addAttribute("courses", courses);
        model.addAttribute("polls", polls);
        model.addAttribute("poll", new Poll());
        return "new-poll";
    }

    @PostMapping("/create")
    public String createPoll(
            @RequestParam String question,
            @RequestParam String option1,
            @RequestParam String option2,
            @RequestParam String option3,
            @RequestParam String option4,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return "redirect:/login";
        }

        List<String> options = List.of(option1, option2, option3, option4);
        List<Integer> votes = List.of(0, 0, 0, 0);

        Poll poll = new Poll(question, options, votes);
        pollService.createPoll(poll);
        return "redirect:/poll/" + poll.getId() + "?success=Poll+created+successfully";
    }

    @GetMapping("/{id}")
    public String showPoll(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        if (user == null) {
            return "redirect:/login";
        }

        List<Course> courses = courseService.getAllCourses();
        List<Poll> polls = pollService.getAllPolls();
        model.addAttribute("courses", courses);
        model.addAttribute("polls", polls);

        Poll poll = pollService.getPollById(id);
        if (poll == null) {
            return "redirect:/poll/?error=Poll+not+found";
        }
        poll = pollService.getPollById(id);
        if (poll != null) {

            Hibernate.initialize(poll.getVotesList());
            Hibernate.initialize(poll.getComments());

            model.addAttribute("poll", poll);
            model.addAttribute("user", user);
        }
        return "poll-detail";
    }

    @PostMapping("/{id}/vote")
    public String vote(
            @PathVariable Long id,
            @RequestParam(required = false) Integer optionIndex,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) {

        try {
            pollService.addVote(id, optionIndex, user);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/poll/" + id;
    }

    @PostMapping("/{id}/remove-vote")
    public String removeVote(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) {

        try {
            pollService.removeVote(id, user);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/poll/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deletePoll(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) {

        if (user == null || !"TEACHER".equals(user.getRole())) {
            redirectAttributes.addFlashAttribute("error", "Only teachers can delete polls");
            return "redirect:/poll/";
        }

        try {
            pollService.deletePoll(id);
            redirectAttributes.addFlashAttribute("success", "Poll deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete poll: " + e.getMessage());
        }

        return "redirect:/poll/";
    }

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam String text, @AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }

        if (user == null) {
            return "redirect:/login";
        }

        if (text == null || text.trim().isEmpty()) {
            model.addAttribute("error", "Please input a comment.");
            model.addAttribute("poll", pollService.getPollById(id));
            model.addAttribute("user", user);
            return "poll-detail";
        }

        pollService.addComment(id, text, user);
        return "redirect:/poll/" + id;
    }

    @PostMapping("/{id}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long id, @PathVariable Long commentId, @AuthenticationPrincipal User user) {
        if (user == null) {
            return "redirect:/login";
        }

        if (user == null) {
            return "redirect:/login";
        }

        try {
            pollService.removeComment(commentId, user);
        } catch (IllegalArgumentException e) {
            return "redirect:/poll/" + id + "?error=" + e.getMessage();
        }

        return "redirect:/poll/" + id;
    }
    @GetMapping("/voting-history")
    public String votingHistory(Model model, @AuthenticationPrincipal User user) {
        if (user == null) {
            return "redirect:/login";
        }

        List<Vote> votingHistory = voteRepository.findByUser(user);
        List<Course> courses = courseService.getAllCourses();
        List<Poll> polls = pollService.getAllPolls();

        model.addAttribute("votingHistory", votingHistory);
        model.addAttribute("courses", courses);
        model.addAttribute("polls", polls);
        model.addAttribute("user", user);

        return "voting-history";
    }
}
