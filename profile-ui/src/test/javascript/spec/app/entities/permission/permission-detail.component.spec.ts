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
import {PermissionDetailComponent} from "../../../../../../main/webapp/app/entities/permission/permission-detail.component";
import {PermissionService} from "../../../../../../main/webapp/app/entities/permission/permission.service";
import {Permission} from "../../../../../../main/webapp/app/entities/permission/permission.model";

describe('Component Tests', () => {

	describe('Permission Management Detail Component', () => {
		let comp: PermissionDetailComponent;
		let fixture: ComponentFixture<PermissionDetailComponent>;
		let service: PermissionService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [PermissionDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({name: '123'})
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
					PermissionService
				]
			}).overrideComponent(PermissionDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(PermissionDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(PermissionService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Permission('10')));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith('123');
				expect(comp.permission).toEqual(jasmine.objectContaining({name: '10'}));
			});
		});
	});

});
