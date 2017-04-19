import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {Permission} from "./permission.model";
import {PermissionPopupService} from "./permission-popup.service";
import {PermissionService} from "./permission.service";

@Component({
	selector: 'jhi-permission-delete-dialog',
	templateUrl: './permission-delete-dialog.component.html'
})
export class PermissionDeleteDialogComponent {

	permission: Permission;

	constructor(private jhiLanguageService: JhiLanguageService,
				private permissionService: PermissionService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['permission']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(name: string) {
		this.permissionService.delete(name).subscribe(response => {
			this.eventManager.broadcast({
				name: 'permissionListModification',
				content: 'Deleted an permission'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-permission-delete-popup',
	template: ''
})
export class PermissionDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private permissionPopupService: PermissionPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.permissionPopupService
				.open(PermissionDeleteDialogComponent, params['name']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
