package com.teammachine.staffrostering.domain.filters.specs;

import com.teammachine.staffrostering.domain.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecs {

    public static Specification<Employee> findByNameOrCode(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = getLikePattern(searchTerm);
            return criteriaBuilder.or(
                //criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(Employee_.name)), likePattern),
                //criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(Employee_.code)), likePattern)
            );
        };
    }

    private static String getLikePattern(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%";
        } else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }
}
