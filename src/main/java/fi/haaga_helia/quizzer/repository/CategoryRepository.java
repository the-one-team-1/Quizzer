package fi.haaga_helia.quizzer.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.query.Param;
import fi.haaga_helia.quizzer.model.Category;

@RepositoryRestResource
public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findByName(@Param("name") String name);
}
