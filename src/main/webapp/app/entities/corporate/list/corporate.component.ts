import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICorporate } from '../corporate.model';
import { CorporateService } from '../service/corporate.service';
import { CorporateDeleteDialogComponent } from '../delete/corporate-delete-dialog.component';

@Component({
  selector: 'jhi-corporate',
  templateUrl: './corporate.component.html',
})
export class CorporateComponent implements OnInit {
  corporates?: ICorporate[];
  isLoading = false;

  constructor(protected corporateService: CorporateService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.corporateService.query().subscribe(
      (res: HttpResponse<ICorporate[]>) => {
        this.isLoading = false;
        this.corporates = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICorporate): string {
    return item.id!;
  }

  delete(corporate: ICorporate): void {
    const modalRef = this.modalService.open(CorporateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.corporate = corporate;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
