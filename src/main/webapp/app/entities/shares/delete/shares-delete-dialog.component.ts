import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IShares } from '../shares.model';
import { SharesService } from '../service/shares.service';

@Component({
  templateUrl: './shares-delete-dialog.component.html',
})
export class SharesDeleteDialogComponent {
  shares?: IShares;

  constructor(protected sharesService: SharesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sharesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
