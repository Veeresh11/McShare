import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IShares, Shares } from '../shares.model';
import { SharesService } from '../service/shares.service';

@Component({
  selector: 'jhi-shares-update',
  templateUrl: './shares-update.component.html',
})
export class SharesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    numShares: [],
    sharePrice: [],
  });

  constructor(protected sharesService: SharesService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shares }) => {
      this.updateForm(shares);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shares = this.createFromForm();
    if (shares.id !== undefined) {
      this.subscribeToSaveResponse(this.sharesService.update(shares));
    } else {
      this.subscribeToSaveResponse(this.sharesService.create(shares));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShares>>): void {
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

  protected updateForm(shares: IShares): void {
    this.editForm.patchValue({
      id: shares.id,
      numShares: shares.numShares,
      sharePrice: shares.sharePrice,
    });
  }

  protected createFromForm(): IShares {
    return {
      ...new Shares(),
      id: this.editForm.get(['id'])!.value,
      numShares: this.editForm.get(['numShares'])!.value,
      sharePrice: this.editForm.get(['sharePrice'])!.value,
    };
  }
}
