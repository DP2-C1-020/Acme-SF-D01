
package acme.features.auditor.audit_record;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.AuditRecord;
import acme.entities.audits.CodeAudit;

@Repository
public interface AuditorAuditRecordRepository extends AbstractRepository {

	@Query("select cA from CodeAudit cA where cA.id = :auditId")
	CodeAudit findOneCodeAuditById(int auditId);

	@Query("select aR from AuditRecord aR where aR.codeAudit.id = :codeAuditId")
	Collection<AuditRecord> findAllAuditRecordsByCodeAuditId(int codeAuditId);

	@Query("select aR from AuditRecord aR where aR.id = :id")
	AuditRecord findOneAuditRecordById(int id);

	@Query("select aR from AuditRecord aR where aR.code = :code")
	AuditRecord findAuditRecordByCode(String code);

	@Query("select cA from CodeAudit cA")
	Collection<CodeAudit> findAllCodeAudits();

	@Query("select aR.code from AuditRecord aR")
	Collection<String> findAllAuditRecordsCode();

}
