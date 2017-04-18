import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {HeeUser} from "./hee-user.model";
import {HeeUserPopupService} from "./hee-user-popup.service";
import {HeeUserService} from "./hee-user.service";
@Component({
	selector: 'jhi-hee-user-dialog',
	templateUrl: './hee-user-dialog.component.html'
})
export class HeeUserDialogComponent implements OnInit {

	heeUser: HeeUser;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private heeUserService: HeeUserService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['heeUser']);
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
		if (this.heeUser.id !== undefined) {
			this.heeUserService.update(this.heeUser)
				.subscribe((res: HeeUser) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.heeUserService.create(this.heeUser)
				.subscribe((res: HeeUser) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: HeeUser) {
		this.eventManager.broadcast({name: 'heeUserListModification', content: 'OK'});
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
	selector: 'jhi-hee-user-popup',
	template: ''
})
export class HeeUserPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private heeUserPopupService: HeeUserPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.heeUserPopupService
					.open(HeeUserDialogComponent, params['id']);
			} else {
				this.modalRef = this.heeUserPopupService
					.open(HeeUserDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
