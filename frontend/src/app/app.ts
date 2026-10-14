import {Component, OnInit, inject,ChangeDetectorRef} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {
  Employee,
  EmployeeService,
  EmployeePage
} from './services/employee';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  private readonly employeeService = inject(EmployeeService);
  private readonly cdr = inject(ChangeDetectorRef);
  employees: Employee[] = [];
  loading = false;
  errorMessage = '';

  search = '';
  country = '';
  department = '';

  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  countries = ['Canada', 'Germany', 'India', 'UK', 'USA'];

  departments = [
    'Engineering',
    'Finance',
    'HR',
    'Operations',
    'Sales',
  ];

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {


    this.loading = true;
    this.errorMessage = '';

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
            this.cdr.markForCheck();


            console.log('State after update:', {
              loading: this.loading,
              count: this.employees.length,
              total: this.totalElements
            });
          }
        ,

        error: (error) => {
          console.error('Employee API error:', error);

          this.errorMessage =
            'Unable to load employees. Please check the backend connection.';

          this.loading = false;
          this.cdr.markForCheck();

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
    if (
      page >= 0 &&
      page < this.totalPages &&
      !this.loading
    ) {
      this.currentPage = page;
      this.loadEmployees();
    }
  }
}
