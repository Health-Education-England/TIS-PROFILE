import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Role} from "./role.model";
import {RolePopupService} from "./role-popup.service";
import {RoleService} from "./role.service";
@Component({
	selector: 'jhi-role-dialog',
	templateUrl: './role-dialog.component.html'
})
export class RoleDialogComponent implements OnInit {

	role: Role;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private roleService: RoleService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['role']);
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
		if (this.role.name !== undefined) {
			this.roleService.update(this.role)
				.subscribe((res: Role) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.roleService.create(this.role)
				.subscribe((res: Role) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Role) {
		this.eventManager.broadcast({name: 'roleListModification', content: 'OK'});
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
	selector: 'jhi-role-popup',
	template: ''
})
export class RolePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private rolePopupService: RolePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['name']) {
				this.modalRef = this.rolePopupService
					.open(RoleDialogComponent, params['name']);
			} else {
				this.modalRef = this.rolePopupService
					.open(RoleDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
