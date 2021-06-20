import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IndividualDetailComponent } from './individual-detail.component';

describe('Component Tests', () => {
  describe('Individual Management Detail Component', () => {
    let comp: IndividualDetailComponent;
    let fixture: ComponentFixture<IndividualDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IndividualDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ individual: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(IndividualDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IndividualDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load individual on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.individual).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
