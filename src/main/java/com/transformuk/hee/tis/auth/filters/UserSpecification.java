package com.transformuk.hee.tis.auth.filters;

import com.transformuk.hee.tis.auth.model.User;
import org.springframework.data.jpa.domain.Specification;

/**
 * Spring data JPA Specification to do dynamic queries on Revalidation entity.
 */
public class UserSpecification {

	/**
	 * Creates a {@Specification} with NotNull {@Predicate}
	 *
	 * @param attributeName name of user attribute
	 * @param value value of user attribute
	 * @return a {@link Specification}
	 */
	public static Specification<User> isEqualTo(String attributeName, String value) {
		return (root, query, cb) -> cb.equal(root.get(attributeName), value);
	}
}
