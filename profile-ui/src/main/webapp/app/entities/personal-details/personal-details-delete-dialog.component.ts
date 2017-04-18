import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {PersonalDetails} from "./personal-details.model";
import {PersonalDetailsPopupService} from "./personal-details-popup.service";
import {PersonalDetailsService} from "./personal-details.service";

@Component({
	selector: 'jhi-personal-details-delete-dialog',
	templateUrl: './personal-details-delete-dialog.component.html'
})
export class PersonalDetailsDeleteDialogComponent {

	personalDetails: PersonalDetails;

	constructor(private jhiLanguageService: JhiLanguageService,
				private personalDetailsService: PersonalDetailsService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['personalDetails']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.personalDetailsService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'personalDetailsListModification',
				content: 'Deleted an personalDetails'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-personal-details-delete-popup',
	template: ''
})
export class PersonalDetailsDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private personalDetailsPopupService: PersonalDetailsPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.personalDetailsPopupService
				.open(PersonalDetailsDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
