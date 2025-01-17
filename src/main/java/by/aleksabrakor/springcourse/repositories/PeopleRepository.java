package by.aleksabrakor.springcourse.repositories;


import by.aleksabrakor.springcourse.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository <Person, Integer> {
    Optional<Person> findByFioAndIdNot(String fio, int id);

    Person findPersonByBooksId(int id);
}
