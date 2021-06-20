import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICorporate } from '../corporate.model';

@Component({
  selector: 'jhi-corporate-detail',
  templateUrl: './corporate-detail.component.html',
})
export class CorporateDetailComponent implements OnInit {
  corporate: ICorporate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ corporate }) => {
      this.corporate = corporate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
