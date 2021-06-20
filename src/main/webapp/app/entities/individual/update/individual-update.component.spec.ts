jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IndividualService } from '../service/individual.service';
import { IIndividual, Individual } from '../individual.model';

import { IndividualUpdateComponent } from './individual-update.component';

describe('Component Tests', () => {
  describe('Individual Management Update Component', () => {
    let comp: IndividualUpdateComponent;
    let fixture: ComponentFixture<IndividualUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let individualService: IndividualService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IndividualUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(IndividualUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IndividualUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      individualService = TestBed.inject(IndividualService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const individual: IIndividual = { id: 'CBA' };

        activatedRoute.data = of({ individual });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(individual));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const individual = { id: 'ABC' };
        spyOn(individualService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ individual });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: individual }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(individualService.update).toHaveBeenCalledWith(individual);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const individual = new Individual();
        spyOn(individualService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ individual });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: individual }));
        saveSubject.complete();

        // THEN
        expect(individualService.create).toHaveBeenCalledWith(individual);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const individual = { id: 'ABC' };
        spyOn(individualService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ individual });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(individualService.update).toHaveBeenCalledWith(individual);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
