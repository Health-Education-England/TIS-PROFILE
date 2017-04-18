import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {GmcDetails} from "./gmc-details.model";
import {GmcDetailsService} from "./gmc-details.service";
@Injectable()
export class GmcDetailsPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private gmcDetailsService: GmcDetailsService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.gmcDetailsService.find(id).subscribe(gmcDetails => {
				if (gmcDetails.gmcStartDate) {
					gmcDetails.gmcStartDate = {
						year: gmcDetails.gmcStartDate.getFullYear(),
						month: gmcDetails.gmcStartDate.getMonth() + 1,
						day: gmcDetails.gmcStartDate.getDate()
					};
				}
				if (gmcDetails.gmcExpiryDate) {
					gmcDetails.gmcExpiryDate = {
						year: gmcDetails.gmcExpiryDate.getFullYear(),
						month: gmcDetails.gmcExpiryDate.getMonth() + 1,
						day: gmcDetails.gmcExpiryDate.getDate()
					};
				}
				this.gmcDetailsModalRef(component, gmcDetails);
			});
		} else {
			return this.gmcDetailsModalRef(component, new GmcDetails());
		}
	}

	gmcDetailsModalRef(component: Component, gmcDetails: GmcDetails): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.gmcDetails = gmcDetails;
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
