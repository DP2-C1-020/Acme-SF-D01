<%--
- menu.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar code="master.menu.home">
	<acme:menu-left>
		
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link" action="http://www.example.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-guipacrod" action="https://www.youtube.com/watch?v=dQw4w9WgXcQ"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-gabvacgoy" action="https://www.youtube.com/watch?v=1s4gpb2uoRM&ab_channel=PratiksEspa%C3%B1a"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-alvcarber1" action="https://www.instagram.com/salseodelaljarafe/?hl=es"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-jesmarver" action="https://www.youtube.com/watch?v=3o0ThauuN1E"/>
		    <acme:menu-suboption code="master.menu.anonymous.favourite-link-diegonqui" action="https://www.youtube.com/watch?v=AWM5ZNdWlqw"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.any">
			<acme:menu-suboption code="master.menu.any.codeaudits" action="/any/code-audit/list"/>
			<acme:menu-suboption code="master.menu.any.projects" action="/any/project/list"/>
			<acme:menu-suboption code="master.menu.any.contract" action="/any/contract/list"/>
			<acme:menu-suboption code="master.menu.any.sponsorship" action="/any/sponsorship/list"/>
			<acme:menu-suboption code="master.menu.any.claim" action="/any/claim/list"/>	
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRole('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.create-objective" action="/administrator/objective/create"/>		
			<acme:menu-separator/>	
			<acme:menu-suboption code="master.menu.administrator.user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-down" action="/administrator/system/shut-down"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-banner" action="/administrator/banner/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRole('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRole('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.client" access="hasRole('Client')">
			<acme:menu-suboption code="master.menu.client.my-contracts" action="/client/contract/list"/>	
			<acme:menu-suboption code="master.menu.client.client-dashboard" action="/client/client-dashboard/show"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.auditor" access="hasRole('Auditor')">
			<acme:menu-suboption code="master.menu.auditor.my-code_audits" action="/auditor/code-audit/list"/>
			<acme:menu-suboption code="master.menu.auditor.auditor-dashboard" action="/auditor/auditor-dashboard/show"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.manager" access="hasRole('Manager')">
			<acme:menu-suboption code="master.menu.manager.my-projects" action="/manager/project/list-mine"/>	
			<acme:menu-suboption code="master.menu.manager.list-my-user-stories" action="/manager/user-story/list-mine"/>
			<acme:menu-suboption code="master.menu.manager.show-dashboard" action="/manager/manager-dashboard/show"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.developer" access="hasRole('Developer')">
			<acme:menu-suboption code="master.menu.developer.my-training-modules" action="/developer/training-module/list"/>
			<acme:menu-suboption code="master.menu.developer.dashboard" action="/developer/developer-dashboard/show"/>	
			
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.sponsor" access="hasRole('Sponsor')">
			<acme:menu-suboption code="master.menu.sponsor.list-sponsorships" action="/sponsor/sponsorship/list"/>
			<acme:menu-suboption code="master.menu.sponsor.show-dashboard" action="/sponsor/sponsor-dashboard/show"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.authenticated" access="isAuthenticated">
			<acme:menu-suboption code="master.menu.authenticated.list-objectives" action="/authenticated/objective/list"/>
			<acme:menu-suboption code="master.menu.authenticated.list-risks" action="/authenticated/risk/list"/>
		</acme:menu-option>
		
		
	</acme:menu-left>

	<acme:menu-right>
		<acme:menu-option code="master.menu.sign-up" action="/anonymous/user-account/create" access="isAnonymous()"/>
		<acme:menu-option code="master.menu.sign-in" action="/anonymous/system/sign-in" access="isAnonymous()"/>

		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-data" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRole('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider" action="/authenticated/provider/update" access="hasRole('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRole('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer" action="/authenticated/consumer/update" access="hasRole('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-client" action="/authenticated/client/create" access="!hasRole('Client')"/>
			<acme:menu-suboption code="master.menu.user-account.client" action="/authenticated/client/update" access="hasRole('Client')"/>
			<acme:menu-suboption code="master.menu.user-account.become-auditor" action="/authenticated/auditor/create" access="!hasRole('Auditor')"/>
			<acme:menu-suboption code="master.menu.user-account.auditor" action="/authenticated/auditor/update" access="hasRole('Auditor')"/>
			<acme:menu-suboption code="master.menu.user-account.become-manager" action="/authenticated/manager/create" access="!hasRole('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.manager" action="/authenticated/manager/update" access="hasRole('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.become-developer" action="/authenticated/developer/create" access="!hasRole('Developer')"/>
			<acme:menu-suboption code="master.menu.user-account.developer" action="/authenticated/developer/update" access="hasRole('Developer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-sponsor" action="/authenticated/sponsor/create" access="!hasRole('Sponsor')"/>
			<acme:menu-suboption code="master.menu.user-account.sponsor" action="/authenticated/sponsor/update" access="hasRole('Sponsor')"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.sign-out" action="/authenticated/system/sign-out" access="isAuthenticated()"/>
	</acme:menu-right>
</acme:menu-bar>