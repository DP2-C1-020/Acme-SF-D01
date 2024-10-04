
package acme.features.developer.training_sessions;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.training_module.TrainingModule;
import acme.entities.training_session.TrainingSession;

@Repository
public interface DeveloperTrainingSessionRepository extends AbstractRepository {

	@Query("select tS from TrainingSession tS where tS.trainingModule.id = :trainingModuleId")
	Collection<TrainingSession> findAllTrainingSessionsByTrainingModuleId(int trainingModuleId);

	@Query("select tS.code from TrainingSession tS")
	Collection<String> findAllTrainingSessionsCodes();

	@Query("select tS from TrainingSession tS where tS.id = :id")
	TrainingSession findTrainingSessionById(int id);

	@Query("select tM from TrainingModule tM where tM.id = :trainingModuleId")
	TrainingModule findTrainingModuleById(int trainingModuleId);

	@Query("select tS from TrainingSession tS where tS.code = :code")
	TrainingSession findTrainingSessionByCode(String code);

	@Query("select tM from TrainingModule tM where tM.developer.id = :developerId")
	Collection<TrainingModule> findAllTrainingModulesByDeveloperId(int developerId);
}
