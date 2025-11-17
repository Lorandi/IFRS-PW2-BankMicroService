import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class DepositService {

  private baseUrl = 'http://localhost:8080/orch/accounts/deposit';

  constructor(private http: HttpClient) {}

  deposit(accountId: string, amount: number): Observable<any> {
    const url = `${this.baseUrl}/${accountId}/deposit?amount=${amount}`;
    return this.http.patch(url, {});
  }
}