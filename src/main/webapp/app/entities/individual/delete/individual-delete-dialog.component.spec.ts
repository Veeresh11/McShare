jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IndividualService } from '../service/individual.service';

import { IndividualDeleteDialogComponent } from './individual-delete-dialog.component';

describe('Component Tests', () => {
  describe('Individual Management Delete Component', () => {
    let comp: IndividualDeleteDialogComponent;
    let fixture: ComponentFixture<IndividualDeleteDialogComponent>;
    let service: IndividualService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IndividualDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(IndividualDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IndividualDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(IndividualService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete('ABC');
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith('ABC');
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
