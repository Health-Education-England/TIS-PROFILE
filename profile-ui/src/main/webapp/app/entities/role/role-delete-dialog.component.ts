import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {Role} from "./role.model";
import {RolePopupService} from "./role-popup.service";
import {RoleService} from "./role.service";

@Component({
	selector: 'jhi-role-delete-dialog',
	templateUrl: './role-delete-dialog.component.html'
})
export class RoleDeleteDialogComponent {

	role: Role;

	constructor(private jhiLanguageService: JhiLanguageService,
				private roleService: RoleService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['role']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(name: string) {
		this.roleService.delete(name).subscribe(response => {
			this.eventManager.broadcast({
				name: 'roleListModification',
				content: 'Deleted an role'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-role-delete-popup',
	template: ''
})
export class RoleDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private rolePopupService: RolePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.rolePopupService
				.open(RoleDeleteDialogComponent, params['name']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}