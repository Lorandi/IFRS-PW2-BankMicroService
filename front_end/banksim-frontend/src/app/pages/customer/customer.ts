import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerService } from './customer.service';

@Component({
  selector: 'app-customer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer.html',
  styleUrls: ['./customer.css']
})
export class CustomerComponent implements OnInit {

  account: any = null;
  loading = false;
  errorMessage = '';
  successMessage = '';

  groups: string[] = [];

  // withdraw
  withdrawAmount!: number;

  // transfer
  amount!: number;
  targetAccountId!: string;

  // password
  oldPassword = '';
  newPassword = '';
  confirmNewPassword = '';

  constructor(private service: CustomerService) {}

  ngOnInit() {
    this.extractGroupsFromToken();
    this.loadAccount();
  }

  userId: number | null = null;

  extractGroupsFromToken() {
    const token = localStorage.getItem('token');
    if (!token) return;

    const payload = JSON.parse(atob(token.split('.')[1]));

    this.groups = payload.groups || [];

    this.userId = payload.userId || payload.sub || payload.id || null;
  }

  isCustomer() { return this.groups.includes('CUSTOMER'); }
  isAdmin() { return this.groups.includes('ADMIN'); }
  isAudit() { return this.groups.includes('AUDITOR'); }

  loadAccount() {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.service.getAccount().subscribe({
      next: (res) => {
        this.account = res;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;

        if (err.status === 404) {
          this.account = null;
          return;
        }
        this.errorMessage =
          err.error?.error   // mensagem principal do ORCH
          || err.error?.message
          || err.error       // caso venha string JSON pura
          || 'Erro ao carregar conta.';
      }
    });
  }

  createAccount() {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.service.createAccount().subscribe({
      next: () => {
        this.successMessage = 'Conta criada com sucesso!';
        this.loadAccount();
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage =
          err.error?.error   // mensagem principal do ORCH
          || err.error?.message
          || err.error       // caso venha string JSON pura
          || 'Erro ao criar conta.';
      }
    });
  }

  handleWithdraw() {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.service.withdraw(this.account.accountId, this.withdrawAmount).subscribe({
      next: (res) => {
        this.successMessage = res.message;
        this.loadAccount();
      },
      error: (err) => {
        this.loading = false;
         this.errorMessage =
          err.error?.error   // mensagem principal do ORCH
          || err.error?.message
          || err.error       // caso venha string JSON pura
          || 'Erro ao sacar.';
      }
    });
  }

  handleTransfer() {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.service.transfer(this.account.accountId, this.amount, this.targetAccountId).subscribe({
      next: (res) => {
        this.successMessage = res.message;
        this.loadAccount();
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage =
          err.error?.error   // mensagem principal do ORCH
          || err.error?.message
          || err.error       // caso venha string JSON pura
          || 'Erro ao transferir.';
      }
    });
  }

  handleChangePassword() {
    this.errorMessage = '';
    this.successMessage = '';

    if (this.newPassword !== this.confirmNewPassword) {
      this.errorMessage = 'As senhas não conferem.';
      return;
    }

    this.loading = true;

    const payload = {
      userId: this.userId,
      oldPassword: this.oldPassword,
      newPassword: this.newPassword
    };

    this.service.changePassword(payload).subscribe({
      next: () => {
        this.successMessage = 'Senha alterada com sucesso!';
        this.loading = false;
        this.oldPassword = '';
        this.newPassword = '';
        this.confirmNewPassword = '';
      },
      error: (err) => {
        this.loading = false; 
        this.errorMessage =
          err.error?.error   // mensagem principal do ORCH
          || err.error?.message
          || err.error       // caso venha string JSON pura
          || 'Erro ao alterar senha.';
      }
    });
  }
}
