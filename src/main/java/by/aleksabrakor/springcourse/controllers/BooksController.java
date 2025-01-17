package by.aleksabrakor.springcourse.controllers;


import by.aleksabrakor.springcourse.models.Book;
import by.aleksabrakor.springcourse.models.Person;
import by.aleksabrakor.springcourse.services.BooksService;
import by.aleksabrakor.springcourse.services.PeopleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BooksController implements ControllerI<Book> {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService,
                           PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    //возможные варианты запроса:
    //http://localhost:8080/books
    //http://localhost:8080/books?sort_by_year=true
    //http://localhost:8080/books?page=0&books_per_page=3
    //http://localhost:8080/books?page=1&books_per_page=3&sort_by_year=true
    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage) {
        if (page == null || booksPerPage == null) {
            model.addAttribute("books", booksService.findAll(sortByYear)); //выдача всех книг
        } else {
            model.addAttribute("books", booksService.findWithPagination(page, booksPerPage, sortByYear));
        }
        return "books/index";
    }

    @GetMapping("/{id}")
    public String findById(@NotNull @PathVariable("id") int id,
                           Model model,
                           @ModelAttribute("personNew") Person person) {

        model.addAttribute("people", peopleService.findAll());
        model.addAttribute("book", booksService.findById(id));
        model.addAttribute("person", peopleService.findPersonByBooksId(id));
        return "books/show";
    }

    @GetMapping("/new")
    public String newForCreate(Model model) {
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "books/new";
        // в модель  запишется созданный book c полями
        booksService.save(book);
        return ("redirect:/books");
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findById(id));
        return ("books/edit");
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(book, id);
        return ("redirect:/books");
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.deleteById(id);
        return ("redirect:/books");
    }

    /*   На странице книги, если книга свободна, должен быть выпадающий список (<select>)
       со всеми людьми и кнопка "Назначить книгу". Эта кнопка нажимается библиотекарем
       тогда, когда читатель хочет забрать эту книгу домой. После нажатия на эту кнопку, книга
       должна начать принадлежать выбранному человеку и должна появится в его списке
       книг.
       */
    @PatchMapping("/appoint/{id}")
    public String assignBook(@ModelAttribute("person") Person person,
                             @PathVariable("id") int bookId) {
        booksService.assignBook(person, bookId);
        return ("redirect:/books/" + bookId);
    }

    /*На странице книги, если книга взята человеком, рядом с его именем должна быть кнопка
"Освободить книгу". Эта кнопка нажимается библиотекарем тогда, когда читатель
возвращает эту книгу обратно в библиотеку. После нажатия на эту кнопку книга снова
становится свободно и пропадает из списка книг человека.*/
    @PatchMapping("/releaseBook{id}")
    public String releaseBook(@PathVariable("id") int bookId) {
        booksService.releaseBook(bookId);
        return ("redirect:/books/" + bookId);
    }

    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }

    @PostMapping("/search")
    public String makSearch(Model model,
                            @RequestParam(value = "title_start_with", required = false) String titleStartWith) {
        model.addAttribute("books", booksService.findByTitleStartingWith(titleStartWith));
        return "books/search";
    }
}
