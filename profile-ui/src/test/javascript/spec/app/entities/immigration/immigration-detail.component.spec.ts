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
import {ImmigrationDetailComponent} from "../../../../../../main/webapp/app/entities/immigration/immigration-detail.component";
import {ImmigrationService} from "../../../../../../main/webapp/app/entities/immigration/immigration.service";
import {Immigration} from "../../../../../../main/webapp/app/entities/immigration/immigration.model";

describe('Component Tests', () => {

	describe('Immigration Management Detail Component', () => {
		let comp: ImmigrationDetailComponent;
		let fixture: ComponentFixture<ImmigrationDetailComponent>;
		let service: ImmigrationService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [ImmigrationDetailComponent],
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
					ImmigrationService
				]
			}).overrideComponent(ImmigrationDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(ImmigrationDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(ImmigrationService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Immigration(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.immigration).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
