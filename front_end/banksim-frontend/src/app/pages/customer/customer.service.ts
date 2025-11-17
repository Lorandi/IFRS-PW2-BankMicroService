import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private baseUrl = 'http://localhost:8080/orch/accounts/customer';
  private userUrl = 'http://localhost:8080/orch/users/user';

  constructor(private http: HttpClient) {}

  private getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    };
  }

  getAccount(): Observable<any> {
    return this.http.get(`${this.baseUrl}/owner`, this.getAuthHeaders());
  }

  createAccount(): Observable<any> {
    return this.http.post(this.baseUrl, {}, this.getAuthHeaders());
  }

  withdraw(accountId: string, amount: number): Observable<any> {
    return this.http.patch(
      `${this.baseUrl}/${accountId}/withdraw?amount=${amount}`,
      {},
      this.getAuthHeaders()
    );
  }

  transfer(sourceAccountId: string, amount: number, targetAccountId: string): Observable<any> {
  const body = {
    targetAccountId,
    amount
  };

  return this.http.patch(
    `${this.baseUrl}/${sourceAccountId}/transfer`,
    body,
    this.getAuthHeaders()
  );
}

  changePassword(data: any): Observable<any> {
    return this.http.patch(`${this.userUrl}/change-password`, data, this.getAuthHeaders());
}
}
