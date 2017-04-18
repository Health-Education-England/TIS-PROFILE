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
import {GmcDetailsDetailComponent} from "../../../../../../main/webapp/app/entities/gmc-details/gmc-details-detail.component";
import {GmcDetailsService} from "../../../../../../main/webapp/app/entities/gmc-details/gmc-details.service";
import {GmcDetails} from "../../../../../../main/webapp/app/entities/gmc-details/gmc-details.model";

describe('Component Tests', () => {

	describe('GmcDetails Management Detail Component', () => {
		let comp: GmcDetailsDetailComponent;
		let fixture: ComponentFixture<GmcDetailsDetailComponent>;
		let service: GmcDetailsService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [GmcDetailsDetailComponent],
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
					GmcDetailsService
				]
			}).overrideComponent(GmcDetailsDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(GmcDetailsDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(GmcDetailsService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new GmcDetails(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.gmcDetails).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
