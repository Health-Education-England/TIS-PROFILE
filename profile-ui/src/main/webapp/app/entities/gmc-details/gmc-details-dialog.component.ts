import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {GmcDetails} from "./gmc-details.model";
import {GmcDetailsPopupService} from "./gmc-details-popup.service";
import {GmcDetailsService} from "./gmc-details.service";
@Component({
	selector: 'jhi-gmc-details-dialog',
	templateUrl: './gmc-details-dialog.component.html'
})
export class GmcDetailsDialogComponent implements OnInit {

	gmcDetails: GmcDetails;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private gmcDetailsService: GmcDetailsService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['gmcDetails']);
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
		if (this.gmcDetails.id !== undefined) {
			this.gmcDetailsService.update(this.gmcDetails)
				.subscribe((res: GmcDetails) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.gmcDetailsService.create(this.gmcDetails)
				.subscribe((res: GmcDetails) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: GmcDetails) {
		this.eventManager.broadcast({name: 'gmcDetailsListModification', content: 'OK'});
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
	selector: 'jhi-gmc-details-popup',
	template: ''
})
export class GmcDetailsPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private gmcDetailsPopupService: GmcDetailsPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.gmcDetailsPopupService
					.open(GmcDetailsDialogComponent, params['id']);
			} else {
				this.modalRef = this.gmcDetailsPopupService
					.open(GmcDetailsDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
