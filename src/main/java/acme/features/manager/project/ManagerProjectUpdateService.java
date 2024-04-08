/*
 * EmployerJobUpdateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.roles.Manager;

@Service
public class ManagerProjectUpdateService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService<Employer, Job> -------------------------------------

	/*
	 * @Override
	 * public void authorise() {
	 * boolean status;
	 * int masterId;
	 * Job job;
	 * Employer employer;
	 * 
	 * masterId = super.getRequest().getData("id", int.class);
	 * job = this.repository.findOneJobById(masterId);
	 * employer = job == null ? null : job.getEmployer();
	 * status = job != null && job.isDraftMode() && super.getRequest().getPrincipal().hasRole(employer);
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
	 * public void bind(final Job object) {
	 * assert object != null;
	 * 
	 * int contractorId;
	 * Company contractor;
	 * 
	 * contractorId = super.getRequest().getData("contractor", int.class);
	 * contractor = this.repository.findOneContractorById(contractorId);
	 * 
	 * super.bind(object, "reference", "title", "deadline", "salary", "score", "moreInfo", "description");
	 * object.setContractor(contractor);
	 * }
	 * 
	 * @Override
	 * public void validate(final Job object) {
	 * assert object != null;
	 * 
	 * if (!super.getBuffer().getErrors().hasErrors("deadline")) {
	 * Date minimumDeadline;
	 * 
	 * minimumDeadline = MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.DAYS);
	 * super.state(MomentHelper.isAfter(object.getDeadline(), minimumDeadline), "deadline", "employer.job.form.error.too-close");
	 * }
	 * 
	 * if (!super.getBuffer().getErrors().hasErrors("reference")) {
	 * Job existing;
	 * 
	 * existing = this.repository.findOneJobByReference(object.getReference());
	 * super.state(existing == null || existing.equals(object), "reference", "employer.job.form.error.duplicated");
	 * }
	 * 
	 * if (!super.getBuffer().getErrors().hasErrors("salary"))
	 * super.state(object.getSalary().getAmount() > 0, "salary", "employer.job.form.error.negative-salary");
	 * }
	 * 
	 * @Override
	 * public void perform(final Job object) {
	 * assert object != null;
	 * 
	 * this.repository.save(object);
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
	 * employerId = super.getRequest().getPrincipal().getActiveRoleId();
	 * contractors = this.repository.findManyContractorsByEmployerId(employerId);
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
