import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SharesComponent } from '../list/shares.component';
import { SharesDetailComponent } from '../detail/shares-detail.component';
import { SharesUpdateComponent } from '../update/shares-update.component';
import { SharesRoutingResolveService } from './shares-routing-resolve.service';

const sharesRoute: Routes = [
  {
    path: '',
    component: SharesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SharesDetailComponent,
    resolve: {
      shares: SharesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SharesUpdateComponent,
    resolve: {
      shares: SharesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SharesUpdateComponent,
    resolve: {
      shares: SharesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sharesRoute)],
  exports: [RouterModule],
})
export class SharesRoutingModule {}
