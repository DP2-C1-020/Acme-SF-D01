
package acme.features.auditor.auditorDashboard;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.CodeAuditType;

@Repository
public interface AuditorDashboardRepository extends AbstractRepository {

	@Query("select c.type from CodeAudit c where c.auditor.id = :auditorId")
	Collection<CodeAuditType> findcodeAuditsAsAuditTypes(int auditorId);

	default Map<CodeAuditType, Integer> totalAuditTypes(final int managerId) {
		Map<CodeAuditType, Integer> result = new EnumMap<>(CodeAuditType.class);
		Collection<CodeAuditType> auditTypes = this.findcodeAuditsAsAuditTypes(managerId);

		for (CodeAuditType type : CodeAuditType.values())
			result.put(type, 0);

		for (CodeAuditType type : auditTypes)
			result.put(type, result.get(type) + 1);

		return result;
	}

	@Query("select (select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId")
	Collection<Double> auditingRecordsPerAudit(int auditorId);

	@Query("select avg(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId")
	Double averageAuditingRecords(int auditorId);

	@Query("select max(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId")
	Integer maxAuditingRecords(int auditorId);

	@Query("select min(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId")
	Integer minAuditingRecords(int auditorId);

	@Query("select avg(time_to_sec(timediff(ar.finalMoment, ar.initialMoment)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId")
	Double averageRecordPeriod(int auditorId);

	@Query("select stddev(time_to_sec(timediff(ar.finalMoment, ar.initialMoment)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId")
	Double deviationRecordPeriod(int auditorId);

	@Query("select min(time_to_sec(timediff(ar.finalMoment, ar.initialMoment)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId")
	Double minimumRecordPeriod(int auditorId);

	@Query("select max(time_to_sec(timediff(ar.finalMoment, ar.initialMoment)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId")
	Double maximumRecordPeriod(int auditorId);

}
