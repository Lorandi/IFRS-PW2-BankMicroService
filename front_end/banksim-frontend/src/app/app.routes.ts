import { Routes } from '@angular/router';
import { AuthComponent } from './pages/auth/auth';
import { DepositComponent } from './pages/deposit/deposit';
import { CustomerComponent } from './pages/customer/customer';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: AuthComponent },
  { path: 'deposit', component: DepositComponent },
  { path: 'customer', component: CustomerComponent },
  
  // fallback (opcional mas recomendado)
  { path: '**', redirectTo: 'login' }
];  