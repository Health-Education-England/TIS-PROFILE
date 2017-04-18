import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Permission} from "./permission.model";
import {PermissionPopupService} from "./permission-popup.service";
import {PermissionService} from "./permission.service";
@Component({
	selector: 'jhi-permission-dialog',
	templateUrl: './permission-dialog.component.html'
})
export class PermissionDialogComponent implements OnInit {

	permission: Permission;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private permissionService: PermissionService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['permission']);
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
		if (this.permission.id !== undefined) {
			this.permissionService.update(this.permission)
				.subscribe((res: Permission) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.permissionService.create(this.permission)
				.subscribe((res: Permission) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Permission) {
		this.eventManager.broadcast({name: 'permissionListModification', content: 'OK'});
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
	selector: 'jhi-permission-popup',
	template: ''
})
export class PermissionPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private permissionPopupService: PermissionPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.permissionPopupService
					.open(PermissionDialogComponent, params['id']);
			} else {
				this.modalRef = this.permissionPopupService
					.open(PermissionDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
