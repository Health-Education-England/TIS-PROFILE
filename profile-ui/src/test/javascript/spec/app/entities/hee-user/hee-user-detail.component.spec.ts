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
import {HeeUserDetailComponent} from "../../../../../../main/webapp/app/entities/hee-user/hee-user-detail.component";
import {HeeUserService} from "../../../../../../main/webapp/app/entities/hee-user/hee-user.service";
import {HeeUser} from "../../../../../../main/webapp/app/entities/hee-user/hee-user.model";

describe('Component Tests', () => {

	describe('HeeUser Management Detail Component', () => {
		let comp: HeeUserDetailComponent;
		let fixture: ComponentFixture<HeeUserDetailComponent>;
		let service: HeeUserService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [HeeUserDetailComponent],
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
					HeeUserService
				]
			}).overrideComponent(HeeUserDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(HeeUserDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(HeeUserService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new HeeUser(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.heeUser).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
