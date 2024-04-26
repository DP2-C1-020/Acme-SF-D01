
package acme.features.developer.developer_dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface DeveloperDashboardRepository extends AbstractRepository {

	@Query("select count(tM) from TrainingModule tM where tM.developer.id = :developerId and tM.updateMoment is not null")
	Integer getTotalTrainingModulesWithUpdateMomentByDeveloperId(int developerId);

	@Query("select stddev(tM.totalTime) from TrainingModule tM where tM.developer.id = :developerId")
	Double getTrainingModulesDeviationTimeByDeveloperId(int developerId);
	//
	@Query("select avg(tM.totalTime) from TrainingModule tM where tM.developer.id = :developerId")
	Double getTrainingModulesAverageTimeByDeveloperId(int developerId);

	@Query("select count(tS) from TrainingSession tS where tS.trainingModule.developer.id = :developerId and tS.link is not null")
	Integer getTotalTrainingSessionWithLinkByDeveloperId(int developerId);

	@Query("select min(tM.totalTime) from TrainingModule tM where tM.developer.id = :developerId")
	Integer getTrainingModulesMinimumTimeByDeveloperId(int developerId);

	@Query("select max(tM.totalTime) from TrainingModule tM where tM.developer.id = :developerId")
	Integer getTrainingModulesMaximumTimeByDeveloperId(int developerId);

}
