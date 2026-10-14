
import {
  Component,
  OnInit,
  ChangeDetectorRef,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  Employee,
  EmployeeService,
  EmployeePage
} from './services/employee';

import {
  InsightsService,
  CountrySalaryInsight,
  JobTitleSalaryInsight
} from './services/insights';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  private readonly employeeService = inject(EmployeeService);
  private readonly insightsService = inject(InsightsService);
  private readonly cdr = inject(ChangeDetectorRef);

  // Navigation
  activeView: 'dashboard' | 'insights' = 'dashboard';

  // Employee data
  employees: Employee[] = [];
  loading = false;
  errorMessage = '';

  // Search and filters
  search = '';
  country = '';
  department = '';

  countries = ['Canada', 'Germany', 'India', 'UK', 'USA'];

  departments = [
    'Engineering',
    'Finance',
    'HR',
    'Operations',
    'Sales',
  ];

  // Pagination
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  // Salary editor
  selectedEmployee: Employee | null = null;
  newSalary: number | null = null;
  effectiveDate = new Date().toISOString().slice(0, 10);
  salarySaving = false;
  salaryMessage = '';
  salaryError = '';

  // Salary insights
  countryInsights: CountrySalaryInsight[] = [];
  jobTitleInsights: JobTitleSalaryInsight[] = [];
  insightsCountry = 'India';
  insightsLoading = false;
  insightsError = '';

  ngOnInit(): void {
    this.loadEmployees();
    this.loadCountryInsights();
    this.loadJobTitleInsights();
  }

  setView(view: 'dashboard' | 'insights'): void {
    this.activeView = view;

    if (view === 'insights') {
      this.loadCountryInsights();
      this.loadJobTitleInsights();
    }

    this.cdr.detectChanges();
  }

  // Employee list
  loadEmployees(): void {
    this.loading = true;
    this.errorMessage = '';
    this.cdr.markForCheck();

    this.employeeService
      .getEmployees(
        this.currentPage,
        this.pageSize,
        this.search,
        this.country,
        this.department
      )
      .subscribe({
        next: (response: EmployeePage) => {
          this.employees = response.content ?? [];
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.currentPage = response.number;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.error('Employee API error:', error);
          this.errorMessage =
            'Unable to load employees. Please check the backend connection.';
          this.loading = false;
          this.cdr.detectChanges();
        },
      });
  }

  applyFilters(): void {
    this.currentPage = 0;
    this.loadEmployees();
  }

  clearFilters(): void {
    this.search = '';
    this.country = '';
    this.department = '';
    this.currentPage = 0;
    this.loadEmployees();
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages && !this.loading) {
      this.currentPage = page;
      this.loadEmployees();
    }
  }

  // Salary editing
  openSalaryEditor(employee: Employee): void {
    this.selectedEmployee = employee;
    this.newSalary = employee.salary;
    this.effectiveDate = new Date().toISOString().slice(0, 10);
    this.salaryMessage = '';
    this.salaryError = '';
    this.cdr.detectChanges();
  }

  closeSalaryEditor(): void {
    if (this.salarySaving) {
      return;
    }

    this.selectedEmployee = null;
    this.newSalary = null;
    this.salaryError = '';
    this.cdr.detectChanges();
  }

  saveSalary(): void {
    if (
      !this.selectedEmployee ||
      this.newSalary === null ||
      !Number.isFinite(this.newSalary) ||
      this.newSalary <= 0 ||
      !this.effectiveDate
    ) {
      this.salaryError = 'Enter a valid salary and effective date.';
      this.cdr.detectChanges();
      return;
    }

    this.salarySaving = true;
    this.salaryMessage = '';
    this.salaryError = '';

    const employee = this.selectedEmployee;
    const salary = this.newSalary;
    const effectiveDate = this.effectiveDate;

    this.cdr.detectChanges();

    this.employeeService
      .updateSalary(
        employee.id,
        salary,
        employee.currency,
        effectiveDate
      )
      .subscribe({
        next: (updatedEmployee) => {
          this.employees = this.employees.map((item) =>
            item.id === updatedEmployee.id ? updatedEmployee : item
          );

          this.salarySaving = false;
          this.selectedEmployee = null;
          this.newSalary = null;
          this.salaryMessage = 'Salary updated successfully.';

          this.cdr.detectChanges();
          this.loadEmployees();

          // Refresh insights after a successful salary change.
          this.loadCountryInsights();
          this.loadJobTitleInsights();
        },
        error: (error) => {
          console.error('Salary update failed:', error);
          this.salarySaving = false;
          this.salaryError =
            error.error?.message ??
            'Unable to update salary. Please try again.';
          this.cdr.detectChanges();
        },
      });
  }

  // Country salary insights
  loadCountryInsights(): void {
    this.insightsError = '';

    this.insightsService.getCountrySalaries().subscribe({
      next: (response) => {
        this.countryInsights = response ?? [];
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Country insights error:', error);
        this.insightsError = 'Unable to load salary insights.';
        this.cdr.detectChanges();
      },
    });
  }

  // Job-title salary insights
  loadJobTitleInsights(): void {
    if (!this.insightsCountry) {
      this.jobTitleInsights = [];
      return;
    }

    this.insightsLoading = true;
    this.insightsError = '';

    this.insightsService
      .getJobTitleSalaries(this.insightsCountry)
      .subscribe({
        next: (response) => {
          this.jobTitleInsights = response ?? [];
          this.insightsLoading = false;
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.error('Job-title insights error:', error);
          this.insightsLoading = false;
          this.insightsError = 'Unable to load job-title salary data.';
          this.cdr.detectChanges();
        },
      });
  }

  onInsightsCountryChange(): void {
    this.loadJobTitleInsights();
  }
}
