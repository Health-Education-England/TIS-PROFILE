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
import {PersonalDetailsDetailComponent} from "../../../../../../main/webapp/app/entities/personal-details/personal-details-detail.component";
import {PersonalDetailsService} from "../../../../../../main/webapp/app/entities/personal-details/personal-details.service";
import {PersonalDetails} from "../../../../../../main/webapp/app/entities/personal-details/personal-details.model";

describe('Component Tests', () => {

	describe('PersonalDetails Management Detail Component', () => {
		let comp: PersonalDetailsDetailComponent;
		let fixture: ComponentFixture<PersonalDetailsDetailComponent>;
		let service: PersonalDetailsService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [PersonalDetailsDetailComponent],
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
					PersonalDetailsService
				]
			}).overrideComponent(PersonalDetailsDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(PersonalDetailsDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(PersonalDetailsService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new PersonalDetails(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.personalDetails).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
