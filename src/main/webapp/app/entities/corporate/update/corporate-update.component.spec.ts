jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CorporateService } from '../service/corporate.service';
import { ICorporate, Corporate } from '../corporate.model';

import { CorporateUpdateComponent } from './corporate-update.component';

describe('Component Tests', () => {
  describe('Corporate Management Update Component', () => {
    let comp: CorporateUpdateComponent;
    let fixture: ComponentFixture<CorporateUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let corporateService: CorporateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CorporateUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CorporateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CorporateUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      corporateService = TestBed.inject(CorporateService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const corporate: ICorporate = { id: 'CBA' };

        activatedRoute.data = of({ corporate });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(corporate));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const corporate = { id: 'ABC' };
        spyOn(corporateService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ corporate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: corporate }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(corporateService.update).toHaveBeenCalledWith(corporate);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const corporate = new Corporate();
        spyOn(corporateService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ corporate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: corporate }));
        saveSubject.complete();

        // THEN
        expect(corporateService.create).toHaveBeenCalledWith(corporate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const corporate = { id: 'ABC' };
        spyOn(corporateService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ corporate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(corporateService.update).toHaveBeenCalledWith(corporate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
