import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CorporateService } from '../service/corporate.service';

import { CorporateComponent } from './corporate.component';

describe('Component Tests', () => {
  describe('Corporate Management Component', () => {
    let comp: CorporateComponent;
    let fixture: ComponentFixture<CorporateComponent>;
    let service: CorporateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CorporateComponent],
      })
        .overrideTemplate(CorporateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CorporateComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CorporateService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 'ABC' }],
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
      expect(comp.corporates?.[0]).toEqual(jasmine.objectContaining({ id: 'ABC' }));
    });
  });
});
