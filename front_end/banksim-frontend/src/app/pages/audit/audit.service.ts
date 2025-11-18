import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountAuditDTO, UserAuditDTO } from './audit';

@Injectable({
  providedIn: 'root'
})
export class AuditService {

  private ACC_API = 'http://localhost:8080/orch/accounts/audit';
  private USR_API = 'http://localhost:8080/orch/users/audit';

  constructor(private http: HttpClient) {}

  private headers() {
    const token = localStorage.getItem('token');
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    };
  }

  // ACCOUNT AUDIT
  listAccountAudits(): Observable<AccountAuditDTO[]> {
    return this.http.get<AccountAuditDTO[]>(this.ACC_API, this.headers());
  }

  recentAccountAudits(limit: number): Observable<AccountAuditDTO[]> {
    return this.http.get<AccountAuditDTO[]>(`${this.ACC_API}/recent?limit=${limit}`, this.headers());
  }

  accountAuditByOwner(ownerId: number): Observable<AccountAuditDTO[]> {
    return this.http.get<AccountAuditDTO[]>(`${this.ACC_API}/owner/${ownerId}`, this.headers());
  }

  accountAuditByAccountId(accountId: string): Observable<AccountAuditDTO[]> {
    return this.http.get<AccountAuditDTO[]>(`${this.ACC_API}/${accountId}`, this.headers());
  }

  // USER AUDIT
  listUserAudits(): Observable<UserAuditDTO[]> {
    return this.http.get<UserAuditDTO[]>(this.USR_API, this.headers());
  }

  recentUserAudits(limit: number): Observable<UserAuditDTO[]> {
    return this.http.get<UserAuditDTO[]>(`${this.USR_API}/recent?limit=${limit}`, this.headers());
  }

  userAuditByOwner(ownerId: number): Observable<UserAuditDTO[]> {
    return this.http.get<UserAuditDTO[]>(`${this.USR_API}/owner/${ownerId}`, this.headers());
  }
}
