
package acme.features.developer.training_modules;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.training_module.TrainingModule;

@Repository
public interface DeveloperTrainingModuleRepository extends AbstractRepository {

	@Query("select tM from TrainingModule tM where tM.developer.id = :developerId")
	Collection<TrainingModule> findAllTrainingModulesByDeveloperId(int developerId);

	@Query("select a from TrainingModule a where a.id = :trainingModuleId")
	TrainingModule findTrainingModuleById(int trainingModuleId);

}
