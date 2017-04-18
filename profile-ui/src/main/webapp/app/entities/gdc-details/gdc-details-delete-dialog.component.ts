import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {GdcDetails} from "./gdc-details.model";
import {GdcDetailsPopupService} from "./gdc-details-popup.service";
import {GdcDetailsService} from "./gdc-details.service";

@Component({
	selector: 'jhi-gdc-details-delete-dialog',
	templateUrl: './gdc-details-delete-dialog.component.html'
})
export class GdcDetailsDeleteDialogComponent {

	gdcDetails: GdcDetails;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gdcDetailsService: GdcDetailsService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['gdcDetails']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.gdcDetailsService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'gdcDetailsListModification',
				content: 'Deleted an gdcDetails'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-gdc-details-delete-popup',
	template: ''
})
export class GdcDetailsDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private gdcDetailsPopupService: GdcDetailsPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.gdcDetailsPopupService
				.open(GdcDetailsDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
