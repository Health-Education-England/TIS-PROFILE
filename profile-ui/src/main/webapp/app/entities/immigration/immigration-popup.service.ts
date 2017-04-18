import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Immigration} from "./immigration.model";
import {ImmigrationService} from "./immigration.service";
@Injectable()
export class ImmigrationPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private immigrationService: ImmigrationService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.immigrationService.find(id).subscribe(immigration => {
				if (immigration.visaIssued) {
					immigration.visaIssued = {
						year: immigration.visaIssued.getFullYear(),
						month: immigration.visaIssued.getMonth() + 1,
						day: immigration.visaIssued.getDate()
					};
				}
				if (immigration.visaValidTo) {
					immigration.visaValidTo = {
						year: immigration.visaValidTo.getFullYear(),
						month: immigration.visaValidTo.getMonth() + 1,
						day: immigration.visaValidTo.getDate()
					};
				}
				this.immigrationModalRef(component, immigration);
			});
		} else {
			return this.immigrationModalRef(component, new Immigration());
		}
	}

	immigrationModalRef(component: Component, immigration: Immigration): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.immigration = immigration;
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
