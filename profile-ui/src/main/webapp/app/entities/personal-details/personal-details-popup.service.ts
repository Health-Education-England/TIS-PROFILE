import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {PersonalDetails} from "./personal-details.model";
import {PersonalDetailsService} from "./personal-details.service";
@Injectable()
export class PersonalDetailsPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private personalDetailsService: PersonalDetailsService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.personalDetailsService.find(id).subscribe(personalDetails => {
				this.personalDetailsModalRef(component, personalDetails);
			});
		} else {
			return this.personalDetailsModalRef(component, new PersonalDetails());
		}
	}

	personalDetailsModalRef(component: Component, personalDetails: PersonalDetails): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.personalDetails = personalDetails;
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
