package by.aleksabrakor.springcourse.controllers;


import by.aleksabrakor.springcourse.models.Person;
import by.aleksabrakor.springcourse.services.PeopleService;
import by.aleksabrakor.springcourse.util.PersonValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController implements ControllerI<Person> {
    private final PeopleService peopleService;
    private  final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService,
                            PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String findById(@NotNull @PathVariable("id") int id,
                           Model model) {

        model.addAttribute("person", peopleService.findById(id));
        model.addAttribute("books", peopleService.findBookByOwner(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newForCreate(Model model) {
        model.addAttribute("person", new Person());
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        //валидация на уникальность фио
        personValidator.validate(person,bindingResult);

        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        // в модель уже запишется созданный person c полями
        peopleService.save(person);
        return ("redirect:/people");
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findById(id));
        return ("people/edit");
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        //валидация на уникальность фио
        personValidator.validate(person,bindingResult);

        if (bindingResult.hasErrors())
            return "people/edit";
        peopleService.update(person, id);
        return ("redirect:/people");
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.deleteById(id);
        return ("redirect:/people");
    }

    //Пара ключ-значение добавится в модел каждого метода этого контроллера
    @ModelAttribute("message")
    public String message() {
        return "Message for all";
    }
}
