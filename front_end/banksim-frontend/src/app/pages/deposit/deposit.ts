import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DepositService } from './deposit.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-deposit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './deposit.html',
  styleUrls: ['./deposit.css']
})
export class DepositComponent {

  accountId = '';
  amount: number | null = null;

  successMessage = '';
  errorMessage = '';
  loading = false;

  constructor(private depositService: DepositService) {}

  handleDeposit() {
    this.successMessage = '';
    this.errorMessage = '';

    if (!this.accountId || !this.amount) {
      this.errorMessage = 'Preencha todos os campos.';
      return;
    }

    this.loading = true;

    this.depositService.deposit(this.accountId, this.amount).subscribe({
      next: (res) => {
        this.successMessage = res.message || 'Depósito realizado com sucesso!';
        this.loading = false;
      },
      error: (err) => {
        
        this.errorMessage = err.error?.error   // mensagem principal do ORCH
                            || err.error?.message
                            || err.error       // caso venha string JSON pura
                            || 'Erro ao realizar depósito.';

        this.loading = false;
      }
    });
  }
}
