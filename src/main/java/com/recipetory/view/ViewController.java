package com.recipetory.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ViewController {
    @GetMapping("/")
    public String showIndex() {
        return "/view/index.html";
    }

    @GetMapping("/view/recipes")
    public String showRecipeSearchResults() {
        return "/view/recipe-search.html";
    }

    @GetMapping("/view/recipes/tags")
    public String showTagSearchResults() {
        return "/view/tag-search.html";
    }

    @GetMapping("/view/recipes/new")
    public String showCreateRecipeForm() {
        return "/view/recipe-new.html";
    }

    @GetMapping("/view/recipes/{recipeId}")
    public String showRecipe(@PathVariable("recipeId") Long recipeId) {
        return "/view/recipe-show.html";
    }

    @GetMapping("/view/login")
    public String showLogInForm() {
        return "/view/login.html";
    }

    @GetMapping("/view/users/{userId}")
    public String showUser(@PathVariable("userId") Long userId) {
        return "/view/user-show.html";
    }

    @GetMapping("/favicon.ico")
    public String getFavicon() {
        return "/view/favicon.ico";
    }
}
