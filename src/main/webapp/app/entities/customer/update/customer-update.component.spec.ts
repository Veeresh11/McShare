jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CustomerService } from '../service/customer.service';
import { ICustomer, Customer } from '../customer.model';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';
import { IShares } from 'app/entities/shares/shares.model';
import { SharesService } from 'app/entities/shares/service/shares.service';

import { CustomerUpdateComponent } from './customer-update.component';

describe('Component Tests', () => {
  describe('Customer Management Update Component', () => {
    let comp: CustomerUpdateComponent;
    let fixture: ComponentFixture<CustomerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let customerService: CustomerService;
    let addressService: AddressService;
    let sharesService: SharesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CustomerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CustomerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      customerService = TestBed.inject(CustomerService);
      addressService = TestBed.inject(AddressService);
      sharesService = TestBed.inject(SharesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call address query and add missing value', () => {
        const customer: ICustomer = { id: 'CBA' };
        const address: IAddress = { id: 63267 };
        customer.address = address;

        const addressCollection: IAddress[] = [{ id: 61727 }];
        spyOn(addressService, 'query').and.returnValue(of(new HttpResponse({ body: addressCollection })));
        const expectedCollection: IAddress[] = [address, ...addressCollection];
        spyOn(addressService, 'addAddressToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ customer });
        comp.ngOnInit();

        expect(addressService.query).toHaveBeenCalled();
        expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(addressCollection, address);
        expect(comp.addressesCollection).toEqual(expectedCollection);
      });

      it('Should call share query and add missing value', () => {
        const customer: ICustomer = { id: 'CBA' };
        const share: IShares = { id: 49921 };
        customer.share = share;

        const shareCollection: IShares[] = [{ id: 22272 }];
        spyOn(sharesService, 'query').and.returnValue(of(new HttpResponse({ body: shareCollection })));
        const expectedCollection: IShares[] = [share, ...shareCollection];
        spyOn(sharesService, 'addSharesToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ customer });
        comp.ngOnInit();

        expect(sharesService.query).toHaveBeenCalled();
        expect(sharesService.addSharesToCollectionIfMissing).toHaveBeenCalledWith(shareCollection, share);
        expect(comp.sharesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const customer: ICustomer = { id: 'CBA' };
        const address: IAddress = { id: 95271 };
        customer.address = address;
        const share: IShares = { id: 10694 };
        customer.share = share;

        activatedRoute.data = of({ customer });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(customer));
        expect(comp.addressesCollection).toContain(address);
        expect(comp.sharesCollection).toContain(share);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const customer = { id: 'ABC' };
        spyOn(customerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ customer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: customer }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(customerService.update).toHaveBeenCalledWith(customer);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const customer = new Customer();
        spyOn(customerService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ customer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: customer }));
        saveSubject.complete();

        // THEN
        expect(customerService.create).toHaveBeenCalledWith(customer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const customer = { id: 'ABC' };
        spyOn(customerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ customer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(customerService.update).toHaveBeenCalledWith(customer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAddressById', () => {
        it('Should return tracked Address primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAddressById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSharesById', () => {
        it('Should return tracked Shares primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSharesById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
