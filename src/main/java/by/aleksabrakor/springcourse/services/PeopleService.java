package by.aleksabrakor.springcourse.services;

import by.aleksabrakor.springcourse.models.Book;
import by.aleksabrakor.springcourse.models.Person;
import by.aleksabrakor.springcourse.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;


    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findById(int personId) {
        return peopleRepository.findById(personId).orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(Person personUpdate, int id) {
        personUpdate.setId(id);
        peopleRepository.save(personUpdate);

    }

    @Transactional
    public void deleteById(int id) {
        peopleRepository.deleteById(id);
    }

    //Для валидации уникальности имени
    public Optional<Person> findByFio(String fio, int id) {
        return peopleRepository.findByFioAndIdNot(fio, id).stream().findAny();

    }

    public Person findPersonByBooksId(int id) {
        return peopleRepository.findPersonByBooksId(id);
    }

    public List<Book> findBookByOwner(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            List<Book> booksByOwner = person.get().getBooks();
            checkForExpired(booksByOwner);
            return booksByOwner;
        }
        return Collections.emptyList();
    }

    private void checkForExpired(List<Book> booksByOwner) {
        booksByOwner.stream()
                .filter(book -> book.getTakenAt().plusDays(10).isBefore(LocalDateTime.now()))
                .forEach(book -> book.setExpired(true));

//        Другие варианты записи:

//        booksByOwner.forEach(book -> {
//            if (book.getTakenAt().plusDays(10).isBefore(LocalDateTime.now())) {
//                book.setExpired(true);
//            }
//        });

//        for (Book book : booksByOwner) {
//            if (book.getTakenAt().plusDays(10).isBefore(LocalDateTime.now())) {
//                book.setExpired(true);
//            }
//        }
    }

}
