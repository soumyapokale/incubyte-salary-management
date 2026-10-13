
package com.incubyte.salary_management.projection;

import java.math.BigDecimal;

public interface CountrySalarySummary {

    String getCountry();

    String getCurrency();

    BigDecimal getMinSalary();

    BigDecimal getMaxSalary();

    BigDecimal getAverageSalary();

    Long getEmployeeCount();
}
