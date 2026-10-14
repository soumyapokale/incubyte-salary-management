
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CountrySalaryInsight {
  country: string;
  currency: string;
  employeeCount: number;
  averageSalary: number;
  minSalary: number;
  maxSalary: number;
}

export interface JobTitleSalaryInsight {
  country: string;
  currency: string;
  employeeCount: number;
  averageSalary: number;
  jobTitle: string;
}

@Injectable({
  providedIn: 'root'
})
export class InsightsService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/insights';

  getCountrySalaries(): Observable<CountrySalaryInsight[]> {
    return this.http.get<CountrySalaryInsight[]>(
      `${this.apiUrl}/country-salaries`
    );
  }

  getJobTitleSalaries(
    country: string
  ): Observable<JobTitleSalaryInsight[]> {
    const params = new HttpParams().set('country', country);

    return this.http.get<JobTitleSalaryInsight[]>(
      `${this.apiUrl}/job-title-salaries`,
      { params }
    );
  }
}
