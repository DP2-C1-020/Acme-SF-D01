
package acme.features.developer.training_modules;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.training_module.TrainingModule;
import acme.entities.training_session.TrainingSession;

@Repository
public interface DeveloperTrainingModuleRepository extends AbstractRepository {

	@Query("select tM from TrainingModule tM where tM.developer.id = :developerId")
	Collection<TrainingModule> findAllTrainingModulesByDeveloperId(int developerId);

	@Query("select a from TrainingModule a where a.id = :trainingModuleId")
	TrainingModule findTrainingModuleById(int trainingModuleId);

	@Query("select tS from TrainingSession tS where tS.trainingModule.id = :trainingModuleId")
	Collection<TrainingSession> findTrainingSessionsByTrainingModuleId(int trainingModuleId);

}
