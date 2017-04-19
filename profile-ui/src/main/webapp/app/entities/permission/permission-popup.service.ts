import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Permission} from "./permission.model";
import {PermissionService} from "./permission.service";
@Injectable()
export class PermissionPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private permissionService: PermissionService) {
	}

	open(component: Component, name?: string | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (name) {
			this.permissionService.find(name).subscribe(permission => {
				this.permissionModalRef(component, permission);
			});
		} else {
			return this.permissionModalRef(component, new Permission());
		}
	}

	permissionModalRef(component: Component, permission: Permission): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.permission = permission;
		modalRef.result.then(result => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		}, (reason) => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		});
		return modalRef;
	}
}
