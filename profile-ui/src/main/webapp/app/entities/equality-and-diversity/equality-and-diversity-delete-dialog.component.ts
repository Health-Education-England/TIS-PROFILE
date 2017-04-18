import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {EqualityAndDiversity} from "./equality-and-diversity.model";
import {EqualityAndDiversityPopupService} from "./equality-and-diversity-popup.service";
import {EqualityAndDiversityService} from "./equality-and-diversity.service";

@Component({
	selector: 'jhi-equality-and-diversity-delete-dialog',
	templateUrl: './equality-and-diversity-delete-dialog.component.html'
})
export class EqualityAndDiversityDeleteDialogComponent {

	equalityAndDiversity: EqualityAndDiversity;

	constructor(private jhiLanguageService: JhiLanguageService,
				private equalityAndDiversityService: EqualityAndDiversityService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['equalityAndDiversity']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.equalityAndDiversityService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'equalityAndDiversityListModification',
				content: 'Deleted an equalityAndDiversity'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-equality-and-diversity-delete-popup',
	template: ''
})
export class EqualityAndDiversityDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private equalityAndDiversityPopupService: EqualityAndDiversityPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.equalityAndDiversityPopupService
				.open(EqualityAndDiversityDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
