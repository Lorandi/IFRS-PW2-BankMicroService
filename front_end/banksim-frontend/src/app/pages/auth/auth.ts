import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from './auth.service';
import { RouterModule } from '@angular/router';

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

  constructor(private auth: AuthService) {}

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
