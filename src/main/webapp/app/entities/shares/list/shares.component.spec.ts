import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SharesService } from '../service/shares.service';

import { SharesComponent } from './shares.component';

describe('Component Tests', () => {
  describe('Shares Management Component', () => {
    let comp: SharesComponent;
    let fixture: ComponentFixture<SharesComponent>;
    let service: SharesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SharesComponent],
      })
        .overrideTemplate(SharesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SharesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SharesService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.shares?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
