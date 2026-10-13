
package com.incubyte.salary_management.projection;

import java.math.BigDecimal;

public interface JobTitleSalarySummary {

    String getCountry();

    String getCurrency();

    String getJobTitle();

    BigDecimal getAverageSalary();

    Long getEmployeeCount();
}
