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
import {PersonDetailComponent} from "../../../../../../main/webapp/app/entities/person/person-detail.component";
import {PersonService} from "../../../../../../main/webapp/app/entities/person/person.service";
import {Person} from "../../../../../../main/webapp/app/entities/person/person.model";

describe('Component Tests', () => {

	describe('Person Management Detail Component', () => {
		let comp: PersonDetailComponent;
		let fixture: ComponentFixture<PersonDetailComponent>;
		let service: PersonService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [PersonDetailComponent],
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
					PersonService
				]
			}).overrideComponent(PersonDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(PersonDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(PersonService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Person(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.person).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
