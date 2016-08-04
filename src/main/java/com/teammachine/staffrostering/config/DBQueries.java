package com.teammachine.staffrostering.config;

public class DBQueries {

	public static String EMPLOYEE_FILTER_QUERY = "select e from Employee e where lower(e.name) like lower(concat('%', :name, '%'))"
			+ " or lower(e.code) like lower(concat('%', :code, '%')) order by e.name";

}
