import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountDTO, UserMinDTO } from './admin';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private ACC_API = 'http://localhost:8080/orch/accounts/admin';
  private USR_API = 'http://localhost:8080/orch/users/admin';

  constructor(private http: HttpClient) {}

  private getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    };
  }

  // =====================================
  //               CONTAS
  // =====================================
  listAccounts(): Observable<AccountDTO[]> {
    return this.http.get<AccountDTO[]>(this.ACC_API, this.getAuthHeaders());
  }

  toggleAccountStatus(accountId: string): Observable<any> {
    return this.http.patch(
      `${this.ACC_API}/${accountId}/account-status-toggle`,
      {},
      this.getAuthHeaders()
    );
  }

  // =====================================
  //               USUÁRIOS
  // =====================================
  listUsers(): Observable<UserMinDTO[]> {
    return this.http.get<UserMinDTO[]>(this.USR_API, this.getAuthHeaders());
  }

  getUserByUsername(username: string): Observable<UserMinDTO> {
    return this.http.get<UserMinDTO>(
      `${this.USR_API}/username/${username}`,
      this.getAuthHeaders()
    );
  }

  changeUserRole(userId: number, newRole: string): Observable<any> {
    return this.http.patch(
      `${this.USR_API}/${userId}/role?role=${newRole}`,
      {},
      this.getAuthHeaders()
    );
  }
}
