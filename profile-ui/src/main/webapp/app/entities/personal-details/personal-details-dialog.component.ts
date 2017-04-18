import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {PersonalDetails} from "./personal-details.model";
import {PersonalDetailsPopupService} from "./personal-details-popup.service";
import {PersonalDetailsService} from "./personal-details.service";
@Component({
	selector: 'jhi-personal-details-dialog',
	templateUrl: './personal-details-dialog.component.html'
})
export class PersonalDetailsDialogComponent implements OnInit {

	personalDetails: PersonalDetails;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private personalDetailsService: PersonalDetailsService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['personalDetails']);
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
		if (this.personalDetails.id !== undefined) {
			this.personalDetailsService.update(this.personalDetails)
				.subscribe((res: PersonalDetails) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.personalDetailsService.create(this.personalDetails)
				.subscribe((res: PersonalDetails) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: PersonalDetails) {
		this.eventManager.broadcast({name: 'personalDetailsListModification', content: 'OK'});
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
	selector: 'jhi-personal-details-popup',
	template: ''
})
export class PersonalDetailsPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private personalDetailsPopupService: PersonalDetailsPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.personalDetailsPopupService
					.open(PersonalDetailsDialogComponent, params['id']);
			} else {
				this.modalRef = this.personalDetailsPopupService
					.open(PersonalDetailsDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
