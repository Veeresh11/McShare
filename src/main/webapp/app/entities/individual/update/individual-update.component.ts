import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IIndividual, Individual } from '../individual.model';
import { IndividualService } from '../service/individual.service';

@Component({
  selector: 'jhi-individual-update',
  templateUrl: './individual-update.component.html',
})
export class IndividualUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    dob: [],
  });

  constructor(protected individualService: IndividualService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ individual }) => {
      if (individual.id === undefined) {
        const today = dayjs().startOf('day');
        individual.dob = today;
      }

      this.updateForm(individual);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const individual = this.createFromForm();
    if (individual.id !== undefined) {
      this.subscribeToSaveResponse(this.individualService.update(individual));
    } else {
      this.subscribeToSaveResponse(this.individualService.create(individual));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIndividual>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(individual: IIndividual): void {
    this.editForm.patchValue({
      id: individual.id,
      dob: individual.dob ? individual.dob.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IIndividual {
    return {
      ...new Individual(),
      id: this.editForm.get(['id'])!.value,
      dob: this.editForm.get(['dob'])!.value ? dayjs(this.editForm.get(['dob'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
