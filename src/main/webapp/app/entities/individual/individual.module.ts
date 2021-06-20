import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { IndividualComponent } from './list/individual.component';
import { IndividualDetailComponent } from './detail/individual-detail.component';
import { IndividualUpdateComponent } from './update/individual-update.component';
import { IndividualDeleteDialogComponent } from './delete/individual-delete-dialog.component';
import { IndividualRoutingModule } from './route/individual-routing.module';

@NgModule({
  imports: [SharedModule, IndividualRoutingModule],
  declarations: [IndividualComponent, IndividualDetailComponent, IndividualUpdateComponent, IndividualDeleteDialogComponent],
  entryComponents: [IndividualDeleteDialogComponent],
})
export class IndividualModule {}
