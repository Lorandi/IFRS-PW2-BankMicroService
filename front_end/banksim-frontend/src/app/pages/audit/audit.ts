import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuditService } from './audit.service';

@Component({
  selector: 'app-audit',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './audit.html',
  styleUrls: ['./audit.css']
})
export class AuditComponent implements OnInit {

  groups: string[] = [];
  userId: number | null = null;

  extractGroupsFromToken() {
    const token = localStorage.getItem('token');
    if (!token) return;

    const payload = JSON.parse(atob(token.split('.')[1]));
    this.groups = payload.groups || [];
    this.userId = payload.userId || payload.sub || payload.id || null;
  }

  isAudit() { return this.groups.includes('AUDITOR'); }

  // MENSAGENS
  errorMessage = '';
  successMessage = '';

  clearMessages() {
    this.errorMessage = '';
    this.successMessage = '';
  }

  // ================= ACCOUNT AUDIT =================
  accountAudits: AccountAuditDTO[] = [];
  loadingAccounts = false;

  searchAccountId = '';
  searchOwnerId: number | null = null;
  limitAccount = 10;

  // ================= USER AUDIT ====================
  userAudits: UserAuditDTO[] = [];
  loadingUsers = false;

  limitUser = 10;
  searchOwnerUserId: number | null = null;

  constructor(private service: AuditService) {}

  ngOnInit() {
    this.extractGroupsFromToken();
    this.loadAccountAudits();
    this.loadUserAudits();
  }

  // ================= ACCOUNT AUDIT =================

  loadAccountAudits() {
    this.loadingAccounts = true;
    this.clearMessages();

    this.service.listAccountAudits().subscribe({
      next: res => {
        this.accountAudits = res;
        this.loadingAccounts = false;
      },
      error: err => {
        this.loadingAccounts = false;
        this.errorMessage = this.extractErrorMessage(err);
      }
    });
  }

  loadRecentAccountAudits() {
    this.loadingAccounts = true;
    this.clearMessages();

    this.service.recentAccountAudits(this.limitAccount).subscribe({
      next: res => {
        this.accountAudits = res;
        this.loadingAccounts = false;
      },
      error: err => {
        this.loadingAccounts = false;
        this.errorMessage = this.extractErrorMessage(err);
      }
    });
  }

  loadAccountAuditByOwner() {
    if (!this.searchOwnerId) return;

    this.loadingAccounts = true;
    this.clearMessages();

    this.service.accountAuditByOwner(this.searchOwnerId).subscribe({
      next: res => {
        this.accountAudits = res;
        this.loadingAccounts = false;
      },
      error: err => {
        this.loadingAccounts = false;
        this.errorMessage = this.extractErrorMessage(err);
      }
    });
  }

  loadAccountAuditByAccountId() {
    if (!this.searchAccountId.trim()) return;

    this.loadingAccounts = true;
    this.clearMessages();

    this.service.accountAuditByAccountId(this.searchAccountId).subscribe({
      next: res => {
        this.accountAudits = res;
        this.loadingAccounts = false;
      },
      error: err => {
        this.loadingAccounts = false;
        this.errorMessage = this.extractErrorMessage(err);
      }
    });
  }

  // ================= USER AUDIT =================

  loadUserAudits() {
    this.loadingUsers = true;
    this.clearMessages();

    this.service.listUserAudits().subscribe({
      next: res => {
        this.userAudits = res;
        this.loadingUsers = false;
      },
      error: err => {
        this.loadingUsers = false;
        this.errorMessage = this.extractErrorMessage(err);
      }
    });
  }

  loadRecentUserAudits() {
    this.loadingUsers = true;
    this.clearMessages();

    this.service.recentUserAudits(this.limitUser).subscribe({
      next: res => {
        this.userAudits = res;
        this.loadingUsers = false;
      },
      error: err => {
        this.loadingUsers = false;
        this.errorMessage = this.extractErrorMessage(err);
      }
    });
  }

  loadUserAuditByOwner() {
    if (!this.searchOwnerUserId) return;

    this.loadingUsers = true;
    this.clearMessages();

    this.service.userAuditByOwner(this.searchOwnerUserId).subscribe({
      next: res => {
        this.userAudits = res;
        this.loadingUsers = false;
      },
      error: err => {
        this.loadingUsers = false;
        this.errorMessage = this.extractErrorMessage(err);
      }
    });
  }

  // ================= ERROR MESSAGE =================

  private extractErrorMessage(err: any): string {
    if (!err) return 'Erro desconhecido.';
    if (typeof err === 'string') return err;
    if (typeof err.error === 'string') return err.error;

    if (err.error?.message) return err.error.message;
    if (err.error?.error) return err.error.error;

    return 'Operação não pôde ser concluída.';
  }
}


// ===============================
//  DTOs Atualizados
// ===============================

export interface AccountAuditDTO {
  id: number;
  operationId: string;
  action: string;
  ownerId: number;
  sourceAccountId: string;
  targetAccountId: string;
  amount: string;
  result: string;
  timestamp: string;
  errorMessage: string;
}

export interface UserAuditDTO {
  id: number;
  operationId: string;
  action: string;
  userId: number;
  result: string;
  timestamp: string;
  errorMessage: string;
}
