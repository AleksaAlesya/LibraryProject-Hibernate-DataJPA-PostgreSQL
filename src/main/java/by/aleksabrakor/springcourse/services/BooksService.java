package by.aleksabrakor.springcourse.services;

import by.aleksabrakor.springcourse.models.Book;
import by.aleksabrakor.springcourse.models.Person;
import by.aleksabrakor.springcourse.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }


    public List<Book> findWithPagination(int page, int booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }

    public Book findById(int bookId) {
        return booksRepository.findById(bookId).orElse(null);
    }

    public List<Book> findByTitleStartingWith(String titleStartWith) {
        return booksRepository.findByTitleStartingWith(titleStartWith);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(Book bookUpdate, int id) {
        Book bookToBeUpdate = booksRepository.findById(id).get();
        bookUpdate.setId(id);
        bookUpdate.setOwner(bookToBeUpdate.getOwner()); // чтобы не терялась связь при обновлении
        bookUpdate.setTakenAt(bookToBeUpdate.getTakenAt()); // чтобы не терялась связь при обновлении
        booksRepository.save(bookUpdate);
    }

    @Transactional
    public void deleteById(int id) {
        booksRepository.deleteById(id);
    }

    //    получение владельца книги по id книги реализован в PeopleService
    public Person getBookOwner(int id) {
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void assignBook(Person person, int bookId) {
        booksRepository.findById(bookId).ifPresent(book -> {
            book.setOwner(person);
            book.setTakenAt(LocalDateTime.now());
        });
    }

    @Transactional
    public void releaseBook(int bookId) {
        booksRepository.findById(bookId).ifPresent(book -> {
            book.setOwner(null);
            book.setTakenAt(null);
        });
    }
}
