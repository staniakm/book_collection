package com.mariusz.book_collection.repository;

import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.entity.Shelf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ShelfRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ShelfRepository shelfRepository;

    @Test
    public void existingShelfShouldBeReturned(){
        Shelf shelf = createShelf();
        Shelf testShelfSavedInDb = testEntityManager.persist(shelf);

        Optional<Shelf> find = shelfRepository.findById(testShelfSavedInDb.getId());
        assertThat(find.isPresent()).isTrue();
        testEntityManager.flush();
    }


    @Test
    public void existingShelfShouldBeReturnedAndContainAllData(){
        Shelf shelf = createShelf();
        Shelf testShelfSavedInDb = testEntityManager.persist(shelf);

        Shelf find = shelfRepository.findById(testShelfSavedInDb.getId()).orElse(new Shelf());

        assertThat(find.getId()).isEqualTo(testShelfSavedInDb.getId());
        assertThat(find.getDescription()).isEqualTo(shelf.getDescription());
        testEntityManager.flush();
    }


    @Test
    public void returnEmptyOptionalIfShelfDoesNotExistsById(){
        Shelf shelf = createShelf();
        Shelf testShelfSavedInDb = testEntityManager.persist(shelf);
        Optional<Shelf> find = shelfRepository.findById(testShelfSavedInDb.getId()+11);
        assertThat(find.isPresent()).isFalse();
        testEntityManager.flush();
    }

    @Test
    public void existingShelfShouldBeReturnedByDescription(){
        Shelf shelf = createShelf();
        Shelf testShelfSavedInDb = testEntityManager.persist(shelf);

        Optional<Shelf> find = shelfRepository.findByDescription(testShelfSavedInDb.getDescription());
        assertThat(find.isPresent()).isTrue();
        testEntityManager.flush();
    }

    @Test
    public void returnEmptyOptionalIfBookDoesNotExistsByDescription(){
        Shelf shelf = createShelf();
        Shelf testShelfSavedInDb = testEntityManager.persist(shelf);

        Optional<Shelf> find = shelfRepository.findByDescription("Shelf without description");
        assertThat(find.isPresent()).isFalse();
        testEntityManager.flush();
    }

    @Test
    public void saveShelfShouldReturnShelfWithId(){
        Shelf shelf = createShelf();
        Shelf testShelfSavedInDb = testEntityManager.persist(shelf);
        Optional<Shelf> savedInDb = shelfRepository.findById(testShelfSavedInDb.getId());
        Shelf savedShelf = savedInDb.orElse(new Shelf());
        assertThat(savedShelf).isEqualTo(testShelfSavedInDb);
        testEntityManager.flush();
    }

    @Test
    public void updatedShelfShouldReturnShelfWithIdAndChangedData(){
        final String SHELF_NEW_DESCRIPTION="Living room shelf.";

        Shelf shelf = createShelf();
        Shelf testShelfSavedInDb = testEntityManager.persist(shelf);
        Optional<Shelf> savedInDb = shelfRepository.findById(testShelfSavedInDb.getId());
        Shelf savedShelf = savedInDb.orElse(new Shelf());

        savedShelf.setDescription(SHELF_NEW_DESCRIPTION);
        testEntityManager.persist(savedShelf);

        Optional<Shelf> savedInDbNewDescription = shelfRepository.findById(testShelfSavedInDb.getId());
        Shelf shelfWithNewDescription = savedInDbNewDescription.orElse(new Shelf());

        assertThat(savedShelf.getDescription()).isEqualTo(shelf.getDescription());
        assertThat(savedShelf.getId()).isEqualTo(shelfWithNewDescription.getId());
        assertThat(shelfWithNewDescription.getDescription()).isEqualTo(SHELF_NEW_DESCRIPTION);
        testEntityManager.flush();
    }


    private Book createBook(){
        Book book = new Book();
        book.setIsbn("1236547899");
        book.setDescription("No id book");
        book.setTitle("Book without ID");
        return book;
    }

    private Shelf createShelf() {
        Shelf shelf = new Shelf();
        shelf.setDescription("Shelf in bedroom");
        return shelf;
    }

}