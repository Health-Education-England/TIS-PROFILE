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
import {EqualityAndDiversityDetailComponent} from "../../../../../../main/webapp/app/entities/equality-and-diversity/equality-and-diversity-detail.component";
import {EqualityAndDiversityService} from "../../../../../../main/webapp/app/entities/equality-and-diversity/equality-and-diversity.service";
import {EqualityAndDiversity} from "../../../../../../main/webapp/app/entities/equality-and-diversity/equality-and-diversity.model";

describe('Component Tests', () => {

	describe('EqualityAndDiversity Management Detail Component', () => {
		let comp: EqualityAndDiversityDetailComponent;
		let fixture: ComponentFixture<EqualityAndDiversityDetailComponent>;
		let service: EqualityAndDiversityService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [EqualityAndDiversityDetailComponent],
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
					EqualityAndDiversityService
				]
			}).overrideComponent(EqualityAndDiversityDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(EqualityAndDiversityDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(EqualityAndDiversityService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new EqualityAndDiversity(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.equalityAndDiversity).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
