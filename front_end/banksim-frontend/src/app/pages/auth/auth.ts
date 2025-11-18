import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from './auth.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './auth.html',
  styleUrls: ['./auth.css']
})
export class AuthComponent {

  mode: 'login' | 'signup' = 'login';

  // LOGIN — campos corretos
  loginData = {
    userId: 0,
    password: ''
  };

  // SIGNUP — campos corretos
  signupData = {
    username: '',
    password: '',
    cpf: ''
  };

  loginError = '';
  signupError = '';

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  setMode(mode: 'login' | 'signup') {
    this.mode = mode;
    this.loginError = '';
    this.signupError = '';
  }

  isLoading = false;
  // LOGIN
  login() {
    this.isLoading = true;
    this.loginError = '';

    this.auth.login(this.loginData).subscribe({
      next: (res) => {
        this.loginError = '';
        this.isLoading = false;
        localStorage.setItem('token', res.token);
         this.redirectByRole();
      },
      error: (err: any) => {
          this.isLoading = false;
          this.loginError =
          err.error?.error   // mensagem principal do ORCH
          || err.error?.message
          || err.error       // caso venha string JSON pura
          || 'Erro ao fazer login.';

      }
    });
  }

  redirectByRole() {
  const token = localStorage.getItem('token');
  if (!token) return;

  const payload = JSON.parse(atob(token.split('.')[1]));
  const groups = payload.groups || [];

  if (groups.includes('ADMIN')) {
    this.router.navigate(['/admin']);
    return;
  }

  if (groups.includes('AUDITOR')) {
    this.router.navigate(['/audit']);
    return;
  }

  if (groups.includes('CUSTOMER')) {
    this.router.navigate(['/customer']);
    return;
  }
}

  // SIGNUP
  signup() {
    this.auth.signup(this.signupData).subscribe({
      next: () => {
        alert('Conta criada! Agora faça login.');
        this.mode = 'login';
      },
      error: (err: any) => {this.signupError =
          err.error?.error   // mensagem principal do ORCH
          || err.error?.message
          || err.error       // caso venha string JSON pura
          || 'Erro ao criar conta.';
      }
    });
  }
}
