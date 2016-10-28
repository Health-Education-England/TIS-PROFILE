package com.transformuk.hee.tis.auth.service;

import com.transformuk.hee.tis.auth.model.Role;
import com.transformuk.hee.tis.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
