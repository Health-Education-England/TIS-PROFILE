import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {EqualityAndDiversity} from "./equality-and-diversity.model";
import {EqualityAndDiversityPopupService} from "./equality-and-diversity-popup.service";
import {EqualityAndDiversityService} from "./equality-and-diversity.service";
@Component({
	selector: 'jhi-equality-and-diversity-dialog',
	templateUrl: './equality-and-diversity-dialog.component.html'
})
export class EqualityAndDiversityDialogComponent implements OnInit {

	equalityAndDiversity: EqualityAndDiversity;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private equalityAndDiversityService: EqualityAndDiversityService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['equalityAndDiversity']);
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
		if (this.equalityAndDiversity.id !== undefined) {
			this.equalityAndDiversityService.update(this.equalityAndDiversity)
				.subscribe((res: EqualityAndDiversity) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.equalityAndDiversityService.create(this.equalityAndDiversity)
				.subscribe((res: EqualityAndDiversity) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: EqualityAndDiversity) {
		this.eventManager.broadcast({name: 'equalityAndDiversityListModification', content: 'OK'});
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
	selector: 'jhi-equality-and-diversity-popup',
	template: ''
})
export class EqualityAndDiversityPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private equalityAndDiversityPopupService: EqualityAndDiversityPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.equalityAndDiversityPopupService
					.open(EqualityAndDiversityDialogComponent, params['id']);
			} else {
				this.modalRef = this.equalityAndDiversityPopupService
					.open(EqualityAndDiversityDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
