
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Employee {
  id: number;
  employeeCode: string;
  firstName: string;
  lastName: string;
  email: string;
  department: string;
  country: string;
  jobTitle: string;
  salary: number;
  currency: string;
  effectiveDate: string;
}

export interface EmployeePage {
  content: Employee[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  numberOfElements: number;
}

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/employees';


  updateSalary(
    id: number,
    salary: number,
    currency: string,
    effectiveDate: string
  ): Observable<Employee> {
    return this.http.put<Employee>(
      `${this.apiUrl}/${id}/salary`,
      {
        salary,
        currency,
        effectiveDate
      }
    );
  }

  getEmployees(
    page: number = 0,
    size: number = 10,
    search: string = '',
    country: string = '',
    department: string = ''
  ): Observable<EmployeePage> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (search.trim()) {
      params = params.set('search', search.trim());
    }

    if (country) {
      params = params.set('country', country);
    }

    if (department) {
      params = params.set('department', department);
    }

    return this.http.get<EmployeePage>(this.apiUrl, { params });
  }
}
