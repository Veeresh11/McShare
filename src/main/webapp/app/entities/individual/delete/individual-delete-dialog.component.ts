import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIndividual } from '../individual.model';
import { IndividualService } from '../service/individual.service';

@Component({
  templateUrl: './individual-delete-dialog.component.html',
})
export class IndividualDeleteDialogComponent {
  individual?: IIndividual;

  constructor(protected individualService: IndividualService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.individualService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
