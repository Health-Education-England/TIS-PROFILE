import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {HeeUser} from "./hee-user.model";
import {HeeUserService} from "./hee-user.service";
@Injectable()
export class HeeUserPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private heeUserService: HeeUserService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.heeUserService.find(id).subscribe(heeUser => {
				this.heeUserModalRef(component, heeUser);
			});
		} else {
			return this.heeUserModalRef(component, new HeeUser());
		}
	}

	heeUserModalRef(component: Component, heeUser: HeeUser): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.heeUser = heeUser;
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
