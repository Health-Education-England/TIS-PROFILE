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
import {ManageRecordDetailComponent} from "../../../../../../main/webapp/app/entities/manage-record/manage-record-detail.component";
import {ManageRecordService} from "../../../../../../main/webapp/app/entities/manage-record/manage-record.service";
import {ManageRecord} from "../../../../../../main/webapp/app/entities/manage-record/manage-record.model";

describe('Component Tests', () => {

	describe('ManageRecord Management Detail Component', () => {
		let comp: ManageRecordDetailComponent;
		let fixture: ComponentFixture<ManageRecordDetailComponent>;
		let service: ManageRecordService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [ManageRecordDetailComponent],
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
					ManageRecordService
				]
			}).overrideComponent(ManageRecordDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(ManageRecordDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(ManageRecordService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new ManageRecord(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.manageRecord).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});