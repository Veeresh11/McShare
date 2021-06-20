import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIndividual } from '../individual.model';

@Component({
  selector: 'jhi-individual-detail',
  templateUrl: './individual-detail.component.html',
})
export class IndividualDetailComponent implements OnInit {
  individual: IIndividual | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ individual }) => {
      this.individual = individual;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
