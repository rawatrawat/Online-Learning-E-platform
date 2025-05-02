package hkmu.wadd.controller;

import hkmu.wadd.model.User;
import hkmu.wadd.service.CourseService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/delete")
    public String deleteCourse(
            @RequestParam("courseId") Long courseId,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes) {

        if (currentUser == null) {
            return "redirect:/login";
        }

        if (!"TEACHER".equals(currentUser.getRole())) {
            redirectAttributes.addFlashAttribute("error", "Only teachers can delete courses");
            return "redirect:/courseMaterial";
        }

        try {
            if (courseService.deleteCourse(courseId)) {
                redirectAttributes.addFlashAttribute("success", "Course deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to delete course");
            }
            return "redirect:/courseMaterial";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/courseMaterial";
        }
    }
}
