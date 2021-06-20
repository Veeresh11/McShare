import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SharesDetailComponent } from './shares-detail.component';

describe('Component Tests', () => {
  describe('Shares Management Detail Component', () => {
    let comp: SharesDetailComponent;
    let fixture: ComponentFixture<SharesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SharesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ shares: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SharesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SharesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load shares on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.shares).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
