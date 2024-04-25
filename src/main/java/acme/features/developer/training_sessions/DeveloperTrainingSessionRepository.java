
package acme.features.developer.training_sessions;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.training_session.TrainingSession;

@Repository
public interface DeveloperTrainingSessionRepository extends AbstractRepository {

	@Query("select tS from TrainingSession tS where tS.trainingModule.id = :trainingModuleId")
	Collection<TrainingSession> findAllTrainingSessionsByTrainingModuleId(int trainingModuleId);

	@Query("select tS from TrainingSession tS where tS.id = :id")
	TrainingSession findTrainingSessionById(int id);

}
