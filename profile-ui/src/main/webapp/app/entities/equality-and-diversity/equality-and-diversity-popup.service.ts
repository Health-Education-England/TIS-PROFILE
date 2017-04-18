import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EqualityAndDiversity} from "./equality-and-diversity.model";
import {EqualityAndDiversityService} from "./equality-and-diversity.service";
@Injectable()
export class EqualityAndDiversityPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private equalityAndDiversityService: EqualityAndDiversityService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.equalityAndDiversityService.find(id).subscribe(equalityAndDiversity => {
				if (equalityAndDiversity.dateOfBirth) {
					equalityAndDiversity.dateOfBirth = {
						year: equalityAndDiversity.dateOfBirth.getFullYear(),
						month: equalityAndDiversity.dateOfBirth.getMonth() + 1,
						day: equalityAndDiversity.dateOfBirth.getDate()
					};
				}
				this.equalityAndDiversityModalRef(component, equalityAndDiversity);
			});
		} else {
			return this.equalityAndDiversityModalRef(component, new EqualityAndDiversity());
		}
	}

	equalityAndDiversityModalRef(component: Component, equalityAndDiversity: EqualityAndDiversity): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.equalityAndDiversity = equalityAndDiversity;
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
