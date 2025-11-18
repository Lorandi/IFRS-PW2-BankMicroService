import { Routes } from '@angular/router';
import { AuthComponent } from './pages/auth/auth';
import { DepositComponent } from './pages/deposit/deposit';
import { CustomerComponent } from './pages/customer/customer';
import { AdminComponent } from './pages/admin/admin';
import { AuditComponent } from './pages/audit/audit';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: AuthComponent },
  { path: 'deposit', component: DepositComponent },
  { path: 'customer', component: CustomerComponent },
  { path: 'admin', component: AdminComponent },
  { path: 'audit', component: AuditComponent },
  
  // fallback (opcional mas recomendado)
  { path: '**', redirectTo: 'login' }
];  