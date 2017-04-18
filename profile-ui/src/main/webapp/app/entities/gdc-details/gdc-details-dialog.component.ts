import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {GdcDetails} from "./gdc-details.model";
import {GdcDetailsPopupService} from "./gdc-details-popup.service";
import {GdcDetailsService} from "./gdc-details.service";
@Component({
	selector: 'jhi-gdc-details-dialog',
	templateUrl: './gdc-details-dialog.component.html'
})
export class GdcDetailsDialogComponent implements OnInit {

	gdcDetails: GdcDetails;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private gdcDetailsService: GdcDetailsService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['gdcDetails']);
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
		if (this.gdcDetails.id !== undefined) {
			this.gdcDetailsService.update(this.gdcDetails)
				.subscribe((res: GdcDetails) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.gdcDetailsService.create(this.gdcDetails)
				.subscribe((res: GdcDetails) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: GdcDetails) {
		this.eventManager.broadcast({name: 'gdcDetailsListModification', content: 'OK'});
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
	selector: 'jhi-gdc-details-popup',
	template: ''
})
export class GdcDetailsPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private gdcDetailsPopupService: GdcDetailsPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.gdcDetailsPopupService
					.open(GdcDetailsDialogComponent, params['id']);
			} else {
				this.modalRef = this.gdcDetailsPopupService
					.open(GdcDetailsDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
