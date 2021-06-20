jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SharesService } from '../service/shares.service';
import { IShares, Shares } from '../shares.model';

import { SharesUpdateComponent } from './shares-update.component';

describe('Component Tests', () => {
  describe('Shares Management Update Component', () => {
    let comp: SharesUpdateComponent;
    let fixture: ComponentFixture<SharesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sharesService: SharesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SharesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SharesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SharesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sharesService = TestBed.inject(SharesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const shares: IShares = { id: 456 };

        activatedRoute.data = of({ shares });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(shares));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const shares = { id: 123 };
        spyOn(sharesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ shares });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: shares }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sharesService.update).toHaveBeenCalledWith(shares);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const shares = new Shares();
        spyOn(sharesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ shares });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: shares }));
        saveSubject.complete();

        // THEN
        expect(sharesService.create).toHaveBeenCalledWith(shares);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const shares = { id: 123 };
        spyOn(sharesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ shares });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sharesService.update).toHaveBeenCalledWith(shares);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
