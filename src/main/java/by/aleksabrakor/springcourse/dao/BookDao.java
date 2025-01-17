package by.aleksabrakor.springcourse.dao;

import by.aleksabrakor.springcourse.models.Book;
import by.aleksabrakor.springcourse.models.Person;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BookDao {
    private final EntityManager entityManager;

    @Autowired
    public BookDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    //класс и методы не используются, оставлены для примера использования hibernate без Data JPA
    @Transactional
    public void assignBook(Person owner, int bookId) {
        Session session = entityManager.unwrap(Session.class);
        Book book = session.get(Book.class, bookId);
        book.setOwner(owner);
        session.update(book);
    }

    @Transactional
    public void releaseBook(int bookId) {
        Session session = entityManager.unwrap(Session.class);
        Book book = session.get(Book.class, bookId);
        book.setOwner(null);
        session.update(book);
    }
}
