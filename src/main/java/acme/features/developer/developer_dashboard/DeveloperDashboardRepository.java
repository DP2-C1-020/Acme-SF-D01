
package acme.features.developer.developer_dashboard;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface DeveloperDashboardRepository extends AbstractRepository {

	@Query("select count(tM) from TrainingModule tM where tM.developer.id = :developerId and tM.updateMoment is not null")
	Integer getTotalTrainingModulesWithUpdateMomentByDeveloperId(int developerId);

	@Query("select stddev(tM.totalTime) from TrainingModule tM where tM.developer.id = :developerId")
	Optional<Double> getTrainingModulesDeviationTimeByDeveloperId(int developerId);
	//
	@Query("select avg(tM.totalTime) from TrainingModule tM where tM.developer.id = :developerId")
	Optional<Double> getTrainingModulesAverageTimeByDeveloperId(int developerId);

	@Query("select count(tS) from TrainingSession tS where tS.trainingModule.developer.id = :developerId and length(tS.link) > 0")
	Integer getTotalTrainingSessionWithLinkByDeveloperId(int developerId);

	@Query("select min(tM.totalTime) from TrainingModule tM where tM.developer.id = :developerId")
	Optional<Integer> getTrainingModulesMinimumTimeByDeveloperId(int developerId);

	@Query("select max(tM.totalTime) from TrainingModule tM where tM.developer.id = :developerId")
	Optional<Integer> getTrainingModulesMaximumTimeByDeveloperId(int developerId);

	@Query("select count(tM) from TrainingModule tM where tM.developer.id = :developerId")
	Integer getTotalTrainigModulesByDeveloperId(int developerId);

}
