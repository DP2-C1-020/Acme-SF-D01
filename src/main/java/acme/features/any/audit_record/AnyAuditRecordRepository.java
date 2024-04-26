
package acme.features.any.audit_record;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.AuditRecord;
import acme.entities.audits.CodeAudit;

@Repository
public interface AnyAuditRecordRepository extends AbstractRepository {

	@Query("select aR from AuditRecord aR where aR.codeAudit.id = :auditId")
	Collection<AuditRecord> findManyAuditRecordsByCodeAuditId(int auditId);

	@Query("select aR from AuditRecord aR where aR.id = :auditId")
	AuditRecord findOneAuditRecordById(int auditId);

	@Query("select cA from CodeAudit cA")
	Collection<CodeAudit> findAllCodeAudits();

	@Query("select cA from CodeAudit cA where cA.id = :auditId")
	CodeAudit findOneCodeAuditById(int auditId);

}
