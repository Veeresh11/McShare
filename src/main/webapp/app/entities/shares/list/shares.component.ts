import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IShares } from '../shares.model';
import { SharesService } from '../service/shares.service';
import { SharesDeleteDialogComponent } from '../delete/shares-delete-dialog.component';

@Component({
  selector: 'jhi-shares',
  templateUrl: './shares.component.html',
})
export class SharesComponent implements OnInit {
  shares?: IShares[];
  isLoading = false;

  constructor(protected sharesService: SharesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sharesService.query().subscribe(
      (res: HttpResponse<IShares[]>) => {
        this.isLoading = false;
        this.shares = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IShares): number {
    return item.id!;
  }

  delete(shares: IShares): void {
    const modalRef = this.modalService.open(SharesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.shares = shares;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
