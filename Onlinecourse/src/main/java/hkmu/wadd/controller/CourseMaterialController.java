package hkmu.wadd.controller;
import hkmu.wadd.model.Course;
import hkmu.wadd.model.Lecture;
import hkmu.wadd.model.Comment;
import hkmu.wadd.model.User;
import hkmu.wadd.service.CourseService;
import hkmu.wadd.service.LectureService;
import hkmu.wadd.service.CommentService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/courseMaterial")
public class CourseMaterialController {

    private final LectureService lectureService;
    private final CommentService commentService;
    private final CourseService courseService;

    public CourseMaterialController(LectureService lectureService, CommentService commentService, CourseService courseService) {
        this.lectureService = lectureService;
        this.commentService = commentService;
        this.courseService = courseService;
    }

    @GetMapping
    public String showCourseMaterialPage(
            @RequestParam(value = "courseId", required = false) Long courseId,
            @RequestParam(value = "lectureId", required = false) Integer lectureId,
            Model model,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        List<Course> allCourses = courseService.getAllCourses();
        model.addAttribute("allCourses", allCourses);

        if (courseId != null) {
            Course currentCourse = courseService.getCourseById(courseId);
            if (currentCourse == null) {
                return "redirect:/courseMaterial?error=Course+not+found";
            }
            model.addAttribute("currentCourse", currentCourse);

            List<Lecture> lectures = lectureService.getLecturesByCourseId(courseId);
            model.addAttribute("lectures", lectures);
            model.addAttribute("courseId", courseId);

            if (lectureId != null) {
                Lecture lecture = lectureService.getLectureById(lectureId);
                if (lecture != null) {
                    List<Comment> comments = commentService.getCommentsByLectureId(lectureId);
                    model.addAttribute("lecture", lecture);
                    model.addAttribute("comments", comments);
                }
            }
        }
        return "course_material_page";
    }

    @PostMapping("/comments")
    public String addComment(
            @RequestParam("lectureId") int lectureId,
            @RequestParam("courseId") Long courseId,
            @RequestParam("newComment") String content,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return "redirect:/login";
        }

        if (content == null || content.trim().isEmpty()) {
            return "redirect:/courseMaterial?courseId=" + courseId + "&lectureId=" + lectureId;
        }

        commentService.addComment(new Comment(lectureId, user, content));
        return "redirect:/courseMaterial?courseId=" + courseId + "&lectureId=" + lectureId;
    }

    @GetMapping("/manage")
    public String showManageLecturesPage(
            @RequestParam("courseId") Long courseId,
            Model model,
            @AuthenticationPrincipal User user) {

        if (user == null || !"TEACHER".equals(user.getRole())) {
            return "redirect:/courseMaterial";
        }

        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return "redirect:/courseMaterial?error=Course+not+found";
        }

        List<Lecture> lectures = lectureService.getLecturesByCourseId(courseId);
        model.addAttribute("lectures", lectures);
        model.addAttribute("newLecture", new Lecture());
        model.addAttribute("courseId", courseId);
        return "manage_lectures";
    }

    @PostMapping("/add")
    public String addLecture(@ModelAttribute("newLecture") Lecture lecture,
                             @RequestParam("courseId") Long courseId) {
        try {
            lectureService.addLecture(lecture, courseId);
            return "redirect:/courseMaterial/manage?courseId=" + courseId + "&success=Lecture+added";
        } catch (Exception e) {
            return "redirect:/courseMaterial/manage?courseId=" + courseId + "&error=" + e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deleteLecture(@RequestParam("lectureId") int lectureId,
                                @RequestParam("courseId") Long courseId) {
        lectureService.deleteLecture(lectureId);
        return "redirect:/courseMaterial/manage?courseId=" + courseId;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/Upload")
    public String batchUploadFiles(
            @RequestParam("lectureId") int lectureId,
            @RequestParam("courseId") Long courseId,
            @RequestParam(value = "lectureFile", required = false) MultipartFile lectureFile,
            @RequestParam(value = "exerciseFile", required = false) MultipartFile exerciseFile) throws IOException {

        Lecture lecture = lectureService.getLectureById(lectureId);
        if (lecture == null) {
            return "redirect:/courseMaterial/manage?courseId=" + courseId + "&error=Lecture+not+found";
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        if (lectureFile != null && !lectureFile.isEmpty()) {
            String lectureFileName = lectureId + "_lecture.pdf";
            Path lectureFilePath = uploadPath.resolve(lectureFileName);
            Files.copy(lectureFile.getInputStream(), lectureFilePath, StandardCopyOption.REPLACE_EXISTING);
            lecture.setLectureFileName(lectureFileName);
        }

        if (exerciseFile != null && !exerciseFile.isEmpty()) {
            String exerciseFileName = lectureId + "_exercise.pdf";
            Path exerciseFilePath = uploadPath.resolve(exerciseFileName);
            Files.copy(exerciseFile.getInputStream(), exerciseFilePath, StandardCopyOption.REPLACE_EXISTING);
            lecture.setExerciseFileName(exerciseFileName);
        }

        lectureService.updateLectureFiles(lectureId,
                lecture.getLectureFileName(),
                lecture.getExerciseFileName());

        return "redirect:/courseMaterial/manage?courseId=" + courseId + "&success=Files+uploaded+successfully";
    }
    @GetMapping("/download/{lectureId}/{fileType}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Integer lectureId,
            @PathVariable String fileType) throws IOException {

        Lecture lecture = lectureService.getLectureById(lectureId);
        if (lecture == null) {
            return ResponseEntity.notFound().build();
        }

        String fileName = fileType.equals("lecture")
                ? lecture.getLectureFileName()
                : lecture.getExerciseFileName();

        if (fileName == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @PostMapping("/deleteComment")
    public String deleteComment(
            @RequestParam Long commentId,
            @RequestParam Integer lectureId,
            @RequestParam Long courseId,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) {

        if (user == null || !"TEACHER".equals(user.getRole())) {
            return "redirect:/login";
        }

        try {
            commentService.deleteComment(commentId);
            redirectAttributes.addFlashAttribute("success", "Comment deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete comment: " + e.getMessage());
        }

        return "redirect:/courseMaterial?courseId=" + courseId + "&lectureId=" + lectureId;
    }
}