import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        data: { pageTitle: 'Customers' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'individual',
        data: { pageTitle: 'Individuals' },
        loadChildren: () => import('./individual/individual.module').then(m => m.IndividualModule),
      },
      {
        path: 'corporate',
        data: { pageTitle: 'Corporates' },
        loadChildren: () => import('./corporate/corporate.module').then(m => m.CorporateModule),
      },
      {
        path: 'address',
        data: { pageTitle: 'Addresses' },
        loadChildren: () => import('./address/address.module').then(m => m.AddressModule),
      },
      {
        path: 'shares',
        data: { pageTitle: 'Shares' },
        loadChildren: () => import('./shares/shares.module').then(m => m.SharesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
