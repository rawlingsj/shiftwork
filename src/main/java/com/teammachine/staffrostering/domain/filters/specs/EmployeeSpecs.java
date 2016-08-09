package com.teammachine.staffrostering.domain.filters.specs;

import org.springframework.data.jpa.domain.Specification;

import com.teammachine.staffrostering.domain.Employee;
import com.teammachine.staffrostering.domain.Employee_;

public class EmployeeSpecs {

	public static Specification<Employee> findByNameOrCode(String searchTerm) {

		Specification<Employee> specs = ((root, query, cb) -> {
			String containsLikePattern = containsPattern(searchTerm);

			return cb.or(cb.like(cb.lower(root.<String> get(Employee_.name)), containsLikePattern),
					cb.like(cb.lower(root.<String> get(Employee_.code)), containsLikePattern));
		});
		return specs;

	}

	private static String containsPattern(String searchTerm) {
		if (searchTerm == null || searchTerm.isEmpty()) {
			return "%";
		} else {
			StringBuilder builder = new StringBuilder();
			builder.append("%");
			builder.append(searchTerm.toLowerCase());
			builder.append("%");
			return builder.toString();
		}
	}

}
