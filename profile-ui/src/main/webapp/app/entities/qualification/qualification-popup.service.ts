import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Qualification} from "./qualification.model";
import {QualificationService} from "./qualification.service";
@Injectable()
export class QualificationPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private qualificationService: QualificationService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.qualificationService.find(id).subscribe(qualification => {
				if (qualification.dateAttained) {
					qualification.dateAttained = {
						year: qualification.dateAttained.getFullYear(),
						month: qualification.dateAttained.getMonth() + 1,
						day: qualification.dateAttained.getDate()
					};
				}
				this.qualificationModalRef(component, qualification);
			});
		} else {
			return this.qualificationModalRef(component, new Qualification());
		}
	}

	qualificationModalRef(component: Component, qualification: Qualification): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.qualification = qualification;
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
