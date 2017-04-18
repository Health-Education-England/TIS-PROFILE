import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {ManageRecord} from "./manage-record.model";
import {ManageRecordPopupService} from "./manage-record-popup.service";
import {ManageRecordService} from "./manage-record.service";

@Component({
	selector: 'jhi-manage-record-delete-dialog',
	templateUrl: './manage-record-delete-dialog.component.html'
})
export class ManageRecordDeleteDialogComponent {

	manageRecord: ManageRecord;

	constructor(private jhiLanguageService: JhiLanguageService,
				private manageRecordService: ManageRecordService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['manageRecord']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.manageRecordService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'manageRecordListModification',
				content: 'Deleted an manageRecord'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-manage-record-delete-popup',
	template: ''
})
export class ManageRecordDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private manageRecordPopupService: ManageRecordPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.manageRecordPopupService
				.open(ManageRecordDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
