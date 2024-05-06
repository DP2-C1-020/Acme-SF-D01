/*
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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.audits.CodeAudit;
import acme.entities.contracts.Contract;
import acme.entities.project.Project;
import acme.entities.project.ProjectUserStory;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.training_module.TrainingModule;
import acme.roles.Manager;

@Service
public class ManagerProjectDeleteService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Project project;
		Manager manager;

		masterId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(masterId);
		manager = project == null ? null : project.getManager();
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;

		super.bind(object, "code", "title", "abstracto", "fatalErrors", "cost", "link");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(object.isDraftMode(), "draftMode", "manager.project.form.error.already-published");

		{
			Collection<CodeAudit> codeAudits;
			Collection<Contract> contracts;
			Collection<Sponsorship> sponsorships;
			Collection<TrainingModule> trainingModules;

			codeAudits = this.repository.findManyCodeAuditsByProjectId(object.getId());
			super.state(codeAudits.isEmpty(), "*", "manager.project.form.error.children-audits");

			contracts = this.repository.findManyContractsByProjectId(object.getId());
			super.state(contracts.isEmpty(), "*", "manager.project.form.error.children-contracts");

			sponsorships = this.repository.findManySponsorshipsByProjectId(object.getId());
			super.state(sponsorships.isEmpty(), "*", "manager.project.form.error.children-sponsorships");

			trainingModules = this.repository.findManyTrainingModulesByProjectId(object.getId());
			super.state(trainingModules.isEmpty(), "*", "manager.project.form.error.children-training-modules");
		}
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		Collection<ProjectUserStory> projectUserStoryTables;

		projectUserStoryTables = this.repository.findManyProjectUserStoryTablesByProjectId(object.getId());
		this.repository.deleteAll(projectUserStoryTables);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "draftMode", "code", "title", "abstracto", "fatalErrors", "cost", "link");

		super.getResponse().addData(dataset);
	}

}
