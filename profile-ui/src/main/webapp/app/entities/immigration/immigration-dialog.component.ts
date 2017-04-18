import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Immigration} from "./immigration.model";
import {ImmigrationPopupService} from "./immigration-popup.service";
import {ImmigrationService} from "./immigration.service";
@Component({
	selector: 'jhi-immigration-dialog',
	templateUrl: './immigration-dialog.component.html'
})
export class ImmigrationDialogComponent implements OnInit {

	immigration: Immigration;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private immigrationService: ImmigrationService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['immigration']);
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
		if (this.immigration.id !== undefined) {
			this.immigrationService.update(this.immigration)
				.subscribe((res: Immigration) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.immigrationService.create(this.immigration)
				.subscribe((res: Immigration) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Immigration) {
		this.eventManager.broadcast({name: 'immigrationListModification', content: 'OK'});
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
	selector: 'jhi-immigration-popup',
	template: ''
})
export class ImmigrationPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private immigrationPopupService: ImmigrationPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.immigrationPopupService
					.open(ImmigrationDialogComponent, params['id']);
			} else {
				this.modalRef = this.immigrationPopupService
					.open(ImmigrationDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
