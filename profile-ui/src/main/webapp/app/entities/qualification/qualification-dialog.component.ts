import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Qualification} from "./qualification.model";
import {QualificationPopupService} from "./qualification-popup.service";
import {QualificationService} from "./qualification.service";
@Component({
	selector: 'jhi-qualification-dialog',
	templateUrl: './qualification-dialog.component.html'
})
export class QualificationDialogComponent implements OnInit {

	qualification: Qualification;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private qualificationService: QualificationService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['qualification']);
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
		if (this.qualification.id !== undefined) {
			this.qualificationService.update(this.qualification)
				.subscribe((res: Qualification) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.qualificationService.create(this.qualification)
				.subscribe((res: Qualification) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Qualification) {
		this.eventManager.broadcast({name: 'qualificationListModification', content: 'OK'});
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
	selector: 'jhi-qualification-popup',
	template: ''
})
export class QualificationPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private qualificationPopupService: QualificationPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.qualificationPopupService
					.open(QualificationDialogComponent, params['id']);
			} else {
				this.modalRef = this.qualificationPopupService
					.open(QualificationDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
