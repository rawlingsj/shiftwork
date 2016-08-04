package com.teammachine.staffrostering.domain.filters.specs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.teammachine.staffrostering.domain.Employee_;
import com.teammachine.staffrostering.domain.Employee;

public class EmployeeSpecs {

	public static Specification<Employee> findByNameOrCode(String searchTerm) {
		return new Specification<Employee>() {

			@Override
			public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				String containsLikePattern = containsPattern(searchTerm);
				System.out.println(containsLikePattern);
				return cb.or(cb.like(cb.lower(root.<String> get(Employee_.name)), containsLikePattern),
						cb.like(cb.lower(root.<String> get(Employee_.code)), containsLikePattern));

			}
		};
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
