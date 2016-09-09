package com.transformuk.hee.tis.auth.service;

import com.transformuk.hee.tis.auth.model.Permission;
import com.transformuk.hee.tis.auth.model.Role;
import com.transformuk.hee.tis.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Service for role based permissions
 */
@Service
public class PermissionsService {

	private RoleRepository repository;

	@Autowired
	public PermissionsService(RoleRepository repository) {
		this.repository = repository;
	}

	/**
	 * @param roleName Name of the role
	 * @return {@link Permission} or null if no role found for given roleId
	 */
	public Object getPermissions(String roleName) {
		Role role = repository.findByName(roleName);
		return role != null ? role.getPermissions() : format("Role %s not found", roleName);
	}

	/**
	 * @param roleNames the role names
	 * @return an aggregated set of the permissions for all the role names
	 */
	public Set<String> getPermissions(Set<String> roleNames) {
		Set<String> ret = new TreeSet<>();
		List<Role> roles = repository.findByNameIn(roleNames);
		if (!roles.isEmpty()) {
			roles.forEach(r -> ret.addAll(
					r.getPermissions().stream().map(p -> p.getName()).collect(Collectors.toList())));
		}
		return ret;
	}
}
