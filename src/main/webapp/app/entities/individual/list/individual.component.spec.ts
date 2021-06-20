import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { IndividualService } from '../service/individual.service';

import { IndividualComponent } from './individual.component';

describe('Component Tests', () => {
  describe('Individual Management Component', () => {
    let comp: IndividualComponent;
    let fixture: ComponentFixture<IndividualComponent>;
    let service: IndividualService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IndividualComponent],
      })
        .overrideTemplate(IndividualComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IndividualComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(IndividualService);

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
      expect(comp.individuals?.[0]).toEqual(jasmine.objectContaining({ id: 'ABC' }));
    });
  });
});
