
package acme.features.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.roles.Manager;

@Service
public class ManagerProjectListMineService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	/*
	 * // AbstractService interface ----------------------------------------------
	 * 
	 * 
	 * @Override
	 * public void authorise() {
	 * super.getResponse().setAuthorised(true);
	 * }
	 * 
	 * @Override
	 * public void load() {
	 * Collection<Job> objects;
	 * Principal principal;
	 * 
	 * principal = super.getRequest().getPrincipal();
	 * objects = this.repository.findManyJobsByEmployerId(principal.getActiveRoleId());
	 * 
	 * super.getBuffer().addData(objects);
	 * }
	 * 
	 * @Override
	 * public void unbind(final Job object) {
	 * assert object != null;
	 * 
	 * Dataset dataset;
	 * 
	 * dataset = super.unbind(object, "reference", "title", "deadline");
	 * 
	 * super.getResponse().addData(dataset);
	 * }
	 */

}
