import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IShares } from '../shares.model';

@Component({
  selector: 'jhi-shares-detail',
  templateUrl: './shares-detail.component.html',
})
export class SharesDetailComponent implements OnInit {
  shares: IShares | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shares }) => {
      this.shares = shares;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
