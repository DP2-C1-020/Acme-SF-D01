
package acme.features.any.codeAudit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.CodeAudit;
import acme.entities.audits.Mark;
import acme.entities.project.Project;

public interface AnyCodeAuditRepository extends AbstractRepository {

	@Query("select cA from CodeAudit cA where cA.id = :id")
	CodeAudit findOneCodeAuditById(int id);

	@Query("select cA from CodeAudit cA where cA.draftMode = false")
	Collection<CodeAudit> findAllPublishedCodeAudits();

	@Query("select aR.mark from AuditRecord aR where aR.codeAudit.id = :auditId")
	Collection<Mark> findMarksByAuditId(int auditId);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

}
