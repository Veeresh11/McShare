import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CorporateDetailComponent } from './corporate-detail.component';

describe('Component Tests', () => {
  describe('Corporate Management Detail Component', () => {
    let comp: CorporateDetailComponent;
    let fixture: ComponentFixture<CorporateDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CorporateDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ corporate: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(CorporateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CorporateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load corporate on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.corporate).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
