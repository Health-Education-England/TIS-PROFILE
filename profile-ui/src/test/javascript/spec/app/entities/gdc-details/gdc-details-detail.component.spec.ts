import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {GdcDetailsDetailComponent} from "../../../../../../main/webapp/app/entities/gdc-details/gdc-details-detail.component";
import {GdcDetailsService} from "../../../../../../main/webapp/app/entities/gdc-details/gdc-details.service";
import {GdcDetails} from "../../../../../../main/webapp/app/entities/gdc-details/gdc-details.model";

describe('Component Tests', () => {

	describe('GdcDetails Management Detail Component', () => {
		let comp: GdcDetailsDetailComponent;
		let fixture: ComponentFixture<GdcDetailsDetailComponent>;
		let service: GdcDetailsService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [GdcDetailsDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					GdcDetailsService
				]
			}).overrideComponent(GdcDetailsDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(GdcDetailsDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(GdcDetailsService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new GdcDetails(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.gdcDetails).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
