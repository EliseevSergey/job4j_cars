package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.model.User;
import ru.job4j.service.UserService;
import ru.job4j.utility.TimeUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("zones", TimeUtility.getAllTimeZones());
        model.addAttribute("defaultZone", TimeUtility.getDefaultTimeZone());
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        if (user.getTimezone() == null || user.getTimezone().isEmpty()) {
            user.setTimezone(TimeZone.getDefault().getID());
        }
        User savedUser = userService.create(user);
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, HttpServletRequest request, Model model) {
        Optional<User> foundUser = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (foundUser.isEmpty()) {
            model.addAttribute("error", "неверный логин или пароль");
            return "users/login";
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", foundUser.get());
        return "redirect:/posts";
    }

    @GetMapping("/logout")
    public String logOutUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/users/login";
    }

    private static List<TimeZone> getAllTimeZones() {
        var zones = new ArrayList<TimeZone>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            zones.add(TimeZone.getTimeZone(timeId));
        }
        return zones;
    }
}
