package com.transformuk.hee.tis.profile.filters;

import com.transformuk.hee.tis.profile.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

/**
 * Spring data JPA Specification to do dynamic queries on Revalidation entity.
 */
public class UserSpecification {

	public static Specification<User> active() {
		return (root, query, cb) -> cb.equal(root.get("active"), true);
	}

	public static Specification<User> withDBCs(Set<String> designatedBodyCodes) {
		return (root, query, cb) -> root.joinSet("designatedBodyCodes").in(designatedBodyCodes);
	}

	public static Specification<User> withPermissions(List<String> permissions) {
		return (root, query, cb) -> root.join("roles").join("permissions").get("name").in(permissions);
	}
}
