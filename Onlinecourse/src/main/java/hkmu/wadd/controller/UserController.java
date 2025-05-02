package hkmu.wadd.controller;

import hkmu.wadd.model.User;
import hkmu.wadd.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String role,
            Model model) {
        try {
            User user = new User(username, password, fullName, email, phoneNumber, role);
            userService.register(user);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }


    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal User user) {
        if (user == null) {
            return "redirect:/login";
        }
        User freshUser = userService.getUserById(user.getId());
        model.addAttribute("user", freshUser);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String username,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmPassword,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            if (currentUser == null || !currentUser.getUsername().equals(username)) {
                return "redirect:/login";
            }

            User user = userService.getUserByUsername(username);
            if (user == null) {
                throw new RuntimeException("User not found");
            }

            model.addAttribute("user", user);

            if (newPassword != null && !newPassword.trim().isEmpty()) {
                if (!newPassword.equals(confirmPassword)) {
                    throw new RuntimeException("Password confirmation does not match");
                }
                user.setPassword(newPassword);
            }

            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);

            User updatedUser = userService.updateUser(user);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    updatedUser,
                    updatedUser.getPassword(),
                    updatedUser.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
            return "redirect:/profile";

        } catch (Exception e) {
            // Re-fetch the user to ensure we have all current data
            User freshUser = userService.getUserByUsername(username);
            model.addAttribute("user", freshUser);
            model.addAttribute("error", "Update failed: " + e.getMessage());
            return "profile";
        }
    }

    @GetMapping("/admin/users")
    public String manageUsers(Model model, @AuthenticationPrincipal User currentUser) {
        if (currentUser == null || !"TEACHER".equals(currentUser.getRole())) {
            return "redirect:/";
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin-users";
    }

    @PostMapping("/admin/users/update")
    public String updateUser(
            @RequestParam Long id,
            @RequestParam String username,
            @RequestParam(required = false) String password,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String role,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes) {

        try {

            if (currentUser == null || !"TEACHER".equals(currentUser.getRole())) {
                return "redirect:/";
            }

            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                redirectAttributes.addFlashAttribute("error", "User not found");
                return "redirect:/admin/users";
            }

            if (!existingUser.getUsername().equals(username)) {
                User userWithNewUsername = userService.getUserByUsername(username);
                if (userWithNewUsername != null) {
                    redirectAttributes.addFlashAttribute("error", "Username already exists");
                    return "redirect:/admin/users/edit/" + id;
                }
            }

            existingUser.setUsername(username);
            existingUser.setFullName(fullName);
            existingUser.setEmail(email);
            existingUser.setPhoneNumber(phoneNumber);
            existingUser.setRole(role);


            if (password != null && !password.trim().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(password));
            }

            userService.updateUser(existingUser);

            if (existingUser.getId().equals(currentUser.getId())) {
                Authentication newAuth = new UsernamePasswordAuthenticationToken(
                        existingUser,
                        existingUser.getPassword(),
                        existingUser.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }

            redirectAttributes.addFlashAttribute("success", "User updated successfully");
            return "redirect:/admin/users";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Update failed: " + e.getMessage());
            return "redirect:/admin/users/edit/" + id;
        }
    }
    @GetMapping("/admin/users/new")
    public String newUserForm(Model model, @AuthenticationPrincipal User user) {

        if (user == null || !"TEACHER".equals(user.getRole())) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "admin-user-form";
    }
   //save user
   @PostMapping("/admin/users/save")
   public String saveUser(
           @RequestParam String username,
           @RequestParam String password,
           @RequestParam String fullName,
           @RequestParam String email,
           @RequestParam String phoneNumber,
           @RequestParam String role,
           @AuthenticationPrincipal User currentUser,
           Model model) {

       if (currentUser == null || !"TEACHER".equals(currentUser.getRole())) {
           return "redirect:/";
       }

       try {

           if (userService.getUserByUsername(username) != null) {
               throw new RuntimeException("Username already exists");
           }


           User newUser = new User(username, password, fullName, email, phoneNumber, role);
           userService.register(newUser);
           return "redirect:/admin/users?success=User+created+successfully";

       } catch (Exception e) {
           model.addAttribute("error", "Create failed: " + e.getMessage());
           currentUser = new User(username, "", fullName, email, phoneNumber, role);
           model.addAttribute("user", currentUser);
           return "admin-user-form";
       }
   }

    @GetMapping("/admin/users/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) {
            return "redirect:/login";
        }

        if (!"TEACHER".equals(currentUser.getRole())) {
            return "redirect:/";
        }

        try {

            User userToEdit = userService.getUserById(id);


            model.addAttribute("user", userToEdit);
            model.addAttribute("currentUser", currentUser);

            return "edit-user";

        } catch (Exception e) {

            System.err.println("Error editing user ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return "redirect:/admin/users?error=User+" + id + "+not+found";
        }
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {

        if (currentUser == null || !"TEACHER".equals(currentUser.getRole())) {
            return "redirect:/";
        }


        if (currentUser.getId().equals(id)) {
            return "redirect:/admin/users?error=Cannot delete yourself";
        }

        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

}
