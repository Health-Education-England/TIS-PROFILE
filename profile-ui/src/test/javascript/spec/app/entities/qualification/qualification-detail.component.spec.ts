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
import {QualificationDetailComponent} from "../../../../../../main/webapp/app/entities/qualification/qualification-detail.component";
import {QualificationService} from "../../../../../../main/webapp/app/entities/qualification/qualification.service";
import {Qualification} from "../../../../../../main/webapp/app/entities/qualification/qualification.model";

describe('Component Tests', () => {

	describe('Qualification Management Detail Component', () => {
		let comp: QualificationDetailComponent;
		let fixture: ComponentFixture<QualificationDetailComponent>;
		let service: QualificationService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [QualificationDetailComponent],
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
					QualificationService
				]
			}).overrideComponent(QualificationDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(QualificationDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(QualificationService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Qualification(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.qualification).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
