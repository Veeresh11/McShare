import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICorporate, Corporate } from '../corporate.model';
import { CorporateService } from '../service/corporate.service';

@Component({
  selector: 'jhi-corporate-update',
  templateUrl: './corporate-update.component.html',
})
export class CorporateUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    dateIncorp: [],
    regNo: [],
  });

  constructor(protected corporateService: CorporateService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ corporate }) => {
      if (corporate.id === undefined) {
        const today = dayjs().startOf('day');
        corporate.dateIncorp = today;
      }

      this.updateForm(corporate);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const corporate = this.createFromForm();
    if (corporate.id !== undefined) {
      this.subscribeToSaveResponse(this.corporateService.update(corporate));
    } else {
      this.subscribeToSaveResponse(this.corporateService.create(corporate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICorporate>>): void {
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

  protected updateForm(corporate: ICorporate): void {
    this.editForm.patchValue({
      id: corporate.id,
      dateIncorp: corporate.dateIncorp ? corporate.dateIncorp.format(DATE_TIME_FORMAT) : null,
      regNo: corporate.regNo,
    });
  }

  protected createFromForm(): ICorporate {
    return {
      ...new Corporate(),
      id: this.editForm.get(['id'])!.value,
      dateIncorp: this.editForm.get(['dateIncorp'])!.value ? dayjs(this.editForm.get(['dateIncorp'])!.value, DATE_TIME_FORMAT) : undefined,
      regNo: this.editForm.get(['regNo'])!.value,
    };
  }
}
