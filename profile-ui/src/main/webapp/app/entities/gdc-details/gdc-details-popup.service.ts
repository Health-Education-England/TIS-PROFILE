import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {GdcDetails} from "./gdc-details.model";
import {GdcDetailsService} from "./gdc-details.service";
@Injectable()
export class GdcDetailsPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private gdcDetailsService: GdcDetailsService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.gdcDetailsService.find(id).subscribe(gdcDetails => {
				if (gdcDetails.gdcStartDate) {
					gdcDetails.gdcStartDate = {
						year: gdcDetails.gdcStartDate.getFullYear(),
						month: gdcDetails.gdcStartDate.getMonth() + 1,
						day: gdcDetails.gdcStartDate.getDate()
					};
				}
				this.gdcDetailsModalRef(component, gdcDetails);
			});
		} else {
			return this.gdcDetailsModalRef(component, new GdcDetails());
		}
	}

	gdcDetailsModalRef(component: Component, gdcDetails: GdcDetails): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.gdcDetails = gdcDetails;
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
