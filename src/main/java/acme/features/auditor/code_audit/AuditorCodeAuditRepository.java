
package acme.features.auditor.code_audit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.AuditRecord;
import acme.entities.audits.CodeAudit;
import acme.entities.audits.Mark;
import acme.entities.project.Project;
import acme.roles.Auditor;

@Repository
public interface AuditorCodeAuditRepository extends AbstractRepository {

	@Query("select ca from CodeAudit ca")
	Collection<CodeAudit> findAllCodeAudits();

	@Query("select ca from CodeAudit ca where ca.id = :id")
	CodeAudit findOneCodeAuditById(int id);

	@Query("select ca from CodeAudit ca where ca.code = :code")
	CodeAudit findOneCodeAuditByCode(String code);

	@Query("select ca from CodeAudit ca where ca.auditor.userAccount.id = :id")
	Collection<CodeAudit> findCodeAuditsByAuditorId(int id);

	@Query("select p from Project p where p.id = :id")
	Project findProjectById(int id);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select aR from AuditRecord aR where aR.id = :id")
	Collection<AuditRecord> findAllAuditRecordsByCodeAuditId(int id);

	@Query("select a from Auditor a where a.id = :id")
	Auditor findAuditorById(int id);

	@Query("select cA.code from CodeAudit cA")
	Collection<String> findAllCodeAuditsCode();

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findAllProjectsWithoutDraftMode();

	@Query("select aR.mark from AuditRecord aR where aR.codeAudit.id = :codeAuditId")
	Collection<Mark> findMarksByCodeAuditId(int codeAuditId);

}
