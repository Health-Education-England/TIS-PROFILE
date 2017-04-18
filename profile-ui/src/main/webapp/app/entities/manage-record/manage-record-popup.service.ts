import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {ManageRecord} from "./manage-record.model";
import {ManageRecordService} from "./manage-record.service";
@Injectable()
export class ManageRecordPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private manageRecordService: ManageRecordService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.manageRecordService.find(id).subscribe(manageRecord => {
				if (manageRecord.inactiveFrom) {
					manageRecord.inactiveFrom = {
						year: manageRecord.inactiveFrom.getFullYear(),
						month: manageRecord.inactiveFrom.getMonth() + 1,
						day: manageRecord.inactiveFrom.getDate()
					};
				}
				if (manageRecord.inactiveDate) {
					manageRecord.inactiveDate = {
						year: manageRecord.inactiveDate.getFullYear(),
						month: manageRecord.inactiveDate.getMonth() + 1,
						day: manageRecord.inactiveDate.getDate()
					};
				}
				this.manageRecordModalRef(component, manageRecord);
			});
		} else {
			return this.manageRecordModalRef(component, new ManageRecord());
		}
	}

	manageRecordModalRef(component: Component, manageRecord: ManageRecord): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.manageRecord = manageRecord;
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
