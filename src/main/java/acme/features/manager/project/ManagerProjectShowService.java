
package acme.features.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.roles.Manager;

@Service
public class ManagerProjectShowService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	/*
	 * // AbstractService interface ----------------------------------------------
	 * 
	 * 
	 * @Override
	 * public void authorise() {
	 * boolean status;
	 * int masterId;
	 * Job job;
	 * Employer employer;
	 * Date currentMoment;
	 * 
	 * masterId = super.getRequest().getData("id", int.class);
	 * job = this.repository.findOneJobById(masterId);
	 * employer = job == null ? null : job.getEmployer();
	 * currentMoment = MomentHelper.getCurrentMoment();
	 * status = super.getRequest().getPrincipal().hasRole(employer) || job != null && !job.isDraftMode() && MomentHelper.isAfter(job.getDeadline(), currentMoment);
	 * 
	 * super.getResponse().setAuthorised(status);
	 * }
	 * 
	 * @Override
	 * public void load() {
	 * Job object;
	 * int id;
	 * 
	 * id = super.getRequest().getData("id", int.class);
	 * object = this.repository.findOneJobById(id);
	 * 
	 * super.getBuffer().addData(object);
	 * }
	 * 
	 * @Override
	 * public void unbind(final Job object) {
	 * assert object != null;
	 * 
	 * int employerId;
	 * Collection<Company> contractors;
	 * SelectChoices choices;
	 * Dataset dataset;
	 * 
	 * if (!object.isDraftMode())
	 * contractors = this.repository.findAllContractors();
	 * else {
	 * employerId = super.getRequest().getPrincipal().getActiveRoleId();
	 * contractors = this.repository.findManyContractorsByEmployerId(employerId);
	 * }
	 * choices = SelectChoices.from(contractors, "name", object.getContractor());
	 * 
	 * dataset = super.unbind(object, "reference", "title", "deadline", "salary", "score", "moreInfo", "description", "draftMode");
	 * dataset.put("contractor", choices.getSelected().getKey());
	 * dataset.put("contractors", choices);
	 * 
	 * super.getResponse().addData(dataset);
	 * }
	 */
}
