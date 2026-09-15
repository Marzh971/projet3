import { Component, inject } from '@angular/core';
import { SectionComponent } from '../../../../shared/components/section/section.component';
import { CardComponent } from '../../../../shared/components/card/card.component';
import { AdminService } from '../services/admin.service';
import { AsyncPipe } from '@angular/common';
import { map, startWith } from 'rxjs';

@Component({
  selector: 'admin-dashboard',
  standalone: true,
  templateUrl: 'admin-dashboard.page.html',
  imports: [SectionComponent, CardComponent, AsyncPipe],
})
export class AdminDashboardPage {
  private adminService = inject(AdminService);

  countOfAllBooks = this.adminService.countAllBooks().pipe(
    map((count) => count.toString()),
    startWith('0'),
  );

  countOfAllPendingLoans = this.adminService.countAllPending().pipe(
    map((count) => count.toString()),
  )

  countOverdueLoans = this.adminService.countOverdueLoans().pipe(
    map((count) => count.toString()),
  )
}
