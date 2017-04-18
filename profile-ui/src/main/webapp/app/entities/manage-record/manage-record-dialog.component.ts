import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {ManageRecord} from "./manage-record.model";
import {ManageRecordPopupService} from "./manage-record-popup.service";
import {ManageRecordService} from "./manage-record.service";
@Component({
	selector: 'jhi-manage-record-dialog',
	templateUrl: './manage-record-dialog.component.html'
})
export class ManageRecordDialogComponent implements OnInit {

	manageRecord: ManageRecord;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private manageRecordService: ManageRecordService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['manageRecord']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.manageRecord.id !== undefined) {
			this.manageRecordService.update(this.manageRecord)
				.subscribe((res: ManageRecord) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.manageRecordService.create(this.manageRecord)
				.subscribe((res: ManageRecord) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: ManageRecord) {
		this.eventManager.broadcast({name: 'manageRecordListModification', content: 'OK'});
		this.isSaving = false;
		this.activeModal.dismiss(result);
	}

	private onSaveError(error) {
		this.isSaving = false;
		this.onError(error);
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}

@Component({
	selector: 'jhi-manage-record-popup',
	template: ''
})
export class ManageRecordPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private manageRecordPopupService: ManageRecordPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.manageRecordPopupService
					.open(ManageRecordDialogComponent, params['id']);
			} else {
				this.modalRef = this.manageRecordPopupService
					.open(ManageRecordDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
