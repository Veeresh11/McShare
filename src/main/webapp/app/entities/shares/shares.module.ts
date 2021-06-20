import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SharesComponent } from './list/shares.component';
import { SharesDetailComponent } from './detail/shares-detail.component';
import { SharesUpdateComponent } from './update/shares-update.component';
import { SharesDeleteDialogComponent } from './delete/shares-delete-dialog.component';
import { SharesRoutingModule } from './route/shares-routing.module';

@NgModule({
  imports: [SharedModule, SharesRoutingModule],
  declarations: [SharesComponent, SharesDetailComponent, SharesUpdateComponent, SharesDeleteDialogComponent],
  entryComponents: [SharesDeleteDialogComponent],
})
export class SharesModule {}
