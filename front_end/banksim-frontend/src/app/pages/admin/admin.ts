import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from './admin.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.html',
  styleUrls: ['./admin.css']
})
export class AdminComponent implements OnInit {

  // ===========================
  //   TOKEN E GRUPOS
  // ===========================
  groups: string[] = [];
  userId: number | null = null;

  extractGroupsFromToken() {
    const token = localStorage.getItem('token');
    if (!token) return;

    const payload = JSON.parse(atob(token.split('.')[1]));

    this.groups = payload.groups || [];

    // mesmo padrão do CustomerComponent
    this.userId = payload.userId || payload.sub || payload.id || null;
  }

  isCustomer() { return this.groups.includes('CUSTOMER'); }
  isAdmin() { return this.groups.includes('ADMIN'); }
  isAudit() { return this.groups.includes('AUDITOR'); }

  // ===========================
  //   CONTAS
  // ===========================
  accounts: AccountDTO[] = [];
  loadingAccounts = false;

  // ===========================
  //   USUÁRIOS
  // ===========================
  users: UserMinDTO[] = [];
  loadingUsers = false;
  searchUsername = '';

  // ===========================
  //   MENSAGENS
  // ===========================
  errorMessage = '';
  successMessage = '';

  constructor(private service: AdminService) {}

  ngOnInit() {
    this.extractGroupsFromToken();
    this.loadAccounts();
    this.loadUsers();
  }

  clearMessages() {
    this.errorMessage = '';
    this.successMessage = '';
  }

  // =======================================
  //               CONTAS
  // =======================================
  loadAccounts() {
    this.loadingAccounts = true;
    this.clearMessages();

    this.service.listAccounts().subscribe({
      next: res => {
        this.accounts = res;
        this.loadingAccounts = false;
      },
      error: err => {
        this.loadingAccounts = false;
        this.errorMessage =
            err.error?.error ||
            err.error?.message ||
            err.error ||
            'Erro ao carregar contas.';
      }
    });
  }

  toggleStatus(acc: AccountDTO) {
    acc.processing = true;
    this.clearMessages();

    this.service.toggleAccountStatus(acc.accountId).subscribe({
      next: res => {
        acc.status = res.status;
        acc.processing = false;
        this.successMessage = `Status da conta ${acc.accountId} agora é ${res.status}`;
      },
      error: err => {
        acc.processing = false;
        this.errorMessage =
            err.error?.error ||
            err.error?.message ||
            err.error ||
            'Erro ao alterar status.';
      }
    });
  }

  // =======================================
  //               USUÁRIOS
  // =======================================
  loadUsers() {
    this.loadingUsers = true;
    this.clearMessages();

    this.service.listUsers().subscribe({
      next: res => {
        this.users = res;
        this.loadingUsers = false;
      },
      error: err => {
        this.loadingUsers = false;
        this.errorMessage =
            err.error?.error ||
            err.error?.message ||
            err.error ||
            'Erro ao carregar usuários.';
      }
    });
  }

  searchByUsername() {
    if (!this.searchUsername.trim()) return;

    this.clearMessages();

    this.service.getUserByUsername(this.searchUsername).subscribe({
      next: res => {
        this.users = [res];
      },
      error: err => {
          this.errorMessage = this.extractErrorMessage(err) || 'Usuário não encontrado.';
      }
    });
  }

  changeRole(id: number, newRole: string) {
    this.clearMessages();

    this.service.changeUserRole(id, newRole).subscribe({
      next: () => {
        this.successMessage = `Role do usuário ${id} alterada para ${newRole}`;
        this.loadUsers();
      },
      error: err => {
        this.errorMessage =
            err.error?.error ||
            err.error?.message ||
            err.error ||
            'Erro ao alterar role.';
      }
    });
  }

  private extractErrorMessage(err: any): string {
  if (!err) return 'Erro desconhecido.';

  // string pura
  if (typeof err === 'string') return err;

  // backend retorna string em err.error
  if (typeof err.error === 'string') return err.error;

  // backend envia JSON com message
  if (err.error?.message) return err.error.message;

  // backend envia JSON com error
  if (err.error?.error) return err.error.error;

  // objeto desconhecido
  return 'Operação não pôde ser concluída.';
}

}


// ===============================
//  TIPOS (iguais ao Customer)
// ===============================
export interface AccountDTO {
  id: number;
  accountId: string;
  balance: number;
  status: string;
  ownerId: number;
  processing?: boolean;
}

export interface UserMinDTO {
  id: number;
  username: string;
  role: string;
}
