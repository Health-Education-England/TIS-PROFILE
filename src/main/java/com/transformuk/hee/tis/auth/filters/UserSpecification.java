package com.transformuk.hee.tis.auth.filters;

import com.transformuk.hee.tis.auth.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Spring data JPA Specification to do dynamic queries on Revalidation entity.
 */
public class UserSpecification {

	public static Specification<User> active() {
		return (root, query, cb) -> cb.equal(root.get("active"), true);
	}

	public static Specification<User> withDBC(String designatedBodyCode) {
		return (root, query, cb) -> cb.equal(root.get("designatedBodyCode"), designatedBodyCode);
	}

	public static Specification<User> withPermissions(List<String> permissions) {
		return (root, query, cb) -> root.join("roles").join("permissions").get("name").in(permissions);
	}
}
