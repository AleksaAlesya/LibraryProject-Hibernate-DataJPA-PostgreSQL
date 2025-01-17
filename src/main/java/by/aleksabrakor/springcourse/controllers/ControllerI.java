package by.aleksabrakor.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

public interface ControllerI<E> {

    @GetMapping("/new")
    public String newForCreate(Model model);

    @PostMapping()
    public String create(@ModelAttribute("e") @Valid E e, BindingResult bindingResult);

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id);

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("e") @Valid E e, BindingResult bindingResult,
                         @PathVariable("id") int id);

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id);
}
