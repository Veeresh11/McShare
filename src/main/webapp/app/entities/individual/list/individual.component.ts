import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIndividual } from '../individual.model';
import { IndividualService } from '../service/individual.service';
import { IndividualDeleteDialogComponent } from '../delete/individual-delete-dialog.component';

@Component({
  selector: 'jhi-individual',
  templateUrl: './individual.component.html',
})
export class IndividualComponent implements OnInit {
  individuals?: IIndividual[];
  isLoading = false;

  constructor(protected individualService: IndividualService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.individualService.query().subscribe(
      (res: HttpResponse<IIndividual[]>) => {
        this.isLoading = false;
        this.individuals = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IIndividual): string {
    return item.id!;
  }

  delete(individual: IIndividual): void {
    const modalRef = this.modalService.open(IndividualDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.individual = individual;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
