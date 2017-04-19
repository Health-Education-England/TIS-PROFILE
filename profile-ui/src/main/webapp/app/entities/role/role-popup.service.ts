import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Role} from "./role.model";
import {RoleService} from "./role.service";
@Injectable()
export class RolePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private roleService: RoleService) {
	}

	open(component: Component, name?: string | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (name) {
			this.roleService.find(name).subscribe(role => {
				this.roleModalRef(component, role);
			});
		} else {
			return this.roleModalRef(component, new Role());
		}
	}

	roleModalRef(component: Component, role: Role): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.role = role;
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
