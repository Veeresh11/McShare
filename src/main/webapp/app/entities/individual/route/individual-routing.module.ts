import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IndividualComponent } from '../list/individual.component';
import { IndividualDetailComponent } from '../detail/individual-detail.component';
import { IndividualUpdateComponent } from '../update/individual-update.component';
import { IndividualRoutingResolveService } from './individual-routing-resolve.service';

const individualRoute: Routes = [
  {
    path: '',
    component: IndividualComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IndividualDetailComponent,
    resolve: {
      individual: IndividualRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IndividualUpdateComponent,
    resolve: {
      individual: IndividualRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IndividualUpdateComponent,
    resolve: {
      individual: IndividualRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(individualRoute)],
  exports: [RouterModule],
})
export class IndividualRoutingModule {}
