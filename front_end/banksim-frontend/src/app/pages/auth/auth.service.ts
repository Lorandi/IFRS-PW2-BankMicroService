import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiBase = 'http://localhost:8080/orch/users/login';

  constructor(private http: HttpClient) {}
  
  login(data: { userId: number; password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiBase}/login`, data);
  }

  signup(data: { username: string; password: string; cpf: string }): Observable<any> {
    return this.http.post<any>(`${this.apiBase}/signup`, data);
  }
}
