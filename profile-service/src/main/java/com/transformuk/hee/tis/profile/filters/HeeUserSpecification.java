package com.transformuk.hee.tis.profile.filters;

import com.transformuk.hee.tis.profile.domain.HeeUser;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.domain.Specification;

/**
 * Spring data JPA Specification to do dynamic queries on Revalidation entity.
 */
public class HeeUserSpecification {

  public static Specification<HeeUser> active() {
    return (root, query, cb) -> cb.equal(root.get("active"), true);
  }

  public static Specification<HeeUser> withDBCs(Set<String> designatedBodyCodes) {
    return (root, query, cb) -> root.joinSet("designatedBodyCodes").in(designatedBodyCodes);
  }

  public static Specification<HeeUser> withPermissions(List<String> permissions) {
    return (root, query, cb) -> root.join("roles").join("permissions").get("name").in(permissions);
  }
}
