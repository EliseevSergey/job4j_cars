package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.dto.PostForm;
import ru.job4j.model.*;
import ru.job4j.service.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private final PostFacade postFacade;
    private final PostService postService;
    private final CarService carService;
    private final BrandService brandService;
    private final ColorService colorService;
    private final FuelService fuelService;
    private final ModelService modelService;
    private final EngineService engineService;
    private final FileService fileService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("posts", postService.findAll());
        model.addAttribute("filter", "all");
        return "posts/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("fuels", fuelService.findAll());
        return "posts/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("postForm") PostForm form, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }
        postFacade.createFromForm(form, user);
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String getViewPageById(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<Post> postOpt = postService.findById(id);
        if (postOpt.isEmpty()) {
            return "redirect:/posts";
        }
        Post post = postOpt.get();
        model.addAttribute("post", post);
        User loggedUser = (User) session.getAttribute("user");
        boolean isAuthor = loggedUser != null && post.getUser() != null
                && loggedUser.getId().equals(post.getUser().getId());
        model.addAttribute("isAuthor", isAuthor);
        return "posts/view";
    }

    @PostMapping("/{id}/sold")
    public String markAsSold(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttrs) {
        return changeSoldStatus(id, true, session, redirectAttrs);
    }

    @PostMapping("/{id}/unsold")
    public String markAsUnsold(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttrs) {
        return changeSoldStatus(id, false, session, redirectAttrs);
    }

    private String changeSoldStatus(Integer id, boolean sold, HttpSession session, RedirectAttributes redirectAttrs) {
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        Optional<Post> postOpt = postService.findById(id);
        if (postOpt.isEmpty()) {
            return "redirect:/posts";
        }

        Post post = postOpt.get();

        if (post.getUser() == null || !post.getUser().getId().equals(loggedUser.getId())) {
            redirectAttrs.addFlashAttribute("error", "Вы не автор объявления");
            return "redirect:/posts/" + id;
        }
        postService.setSold(id, sold);
        redirectAttrs.addFlashAttribute("success",
                sold ? "Отмечено как проданное" : "Возвращено в продажу");
        return "redirect:/posts/" + id;
    }

    @GetMapping("/{id}/edit")
    public String getEditPageById(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<Post> postOpt = postService.findById(id);
        if (postOpt.isEmpty()) {
            return "redirect:/posts";
        }
        Post post = postOpt.get();

        User loggedUser = (User) session.getAttribute("user");
        boolean isAuthor = loggedUser != null && post.getUser() != null
                && loggedUser.getId().equals(post.getUser().getId());
        if (!isAuthor) {
            return "redirect:/posts/";
        }

        model.addAttribute("post", post);
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("fuels", fuelService.findAll());
        return "posts/edit";
    }




    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, @ModelAttribute("postForm") PostForm form,
                       HttpSession session, RedirectAttributes redirectAttributes) {
        // 1. Аутентификация
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        // 2. Существование
        Optional<Post> postOpt = postService.findById(id);
        if (postOpt.isEmpty()) {
            return "redirect:/posts";
        }
        Post post = postOpt.get();

        // 3. Авторизация
        if (post.getUser() == null || !loggedUser.getId().equals(post.getUser().getId())) {
            redirectAttributes.addFlashAttribute("error", "Вы не автор объявления");
            return "redirect:/posts/" + id;
        }
        Car car = post.getCar();

        // 4. Обновление Engine
        Fuel postFuel = fuelService.findById(form.getFuelId()).
                orElseThrow(() -> new IllegalArgumentException("Fuel not found " + form.getFuelId()));

        Engine engine = car.getEngine();
        engine.setFuel(postFuel);
        engine.setHorsePower(form.getHorsePower());
        engine.setVolume(form.getVolume());

        // 5. Обновление Car

        car.setVin(form.getVin());

        Brand postBrand = brandService.findById(form.getBrandId()).
                orElseThrow(() -> new IllegalArgumentException("Brand not found " + form.getBrandId()));
        car.setBrand(postBrand);

        Color postColor = colorService.findById(form.getColorId()).
                orElseThrow(() -> new IllegalArgumentException("Color not found " + form.getColorId()));
        car.setColor(postColor);

        ru.job4j.model.Model postModel = modelService.findById(form.getModelId()).
                orElseThrow(() -> new IllegalArgumentException("Model not found " + form.getModelId()));
        car.setModel(postModel);

        // 6. Обновление Post
        post.setDescription(form.getDescription());


        // 7. Фото — только если новое прикреплено
        if (form.getPhoto() != null && !form.getPhoto().isEmpty()) {
            String newPhoto = fileService.save(form.getPhoto());
            if (newPhoto != null) {
                post.setPhoto(newPhoto);
            }
        }

        // 8. Сохранение — ВСЁ в одной транзакции через фасад
        postFacade.update(post, car, engine);

        // 9. Flash + редирект

        redirectAttributes.addFlashAttribute("success", "Объявление обновлено");
        return "redirect:/posts/" + id;
    }

    @GetMapping("/last-day")
    public String getLastDayList(Model model) {
        model.addAttribute("posts", postService.findLastDay());
        model.addAttribute("filter", "last-day");
        return "posts/list";
    }

    @GetMapping("/with-photo")
    public String getWithPhoto(Model model) {
        model.addAttribute("posts", postService.findWithPhoto());
        model.addAttribute("filter", "with-photo");
        return "posts/list";
    }

}
