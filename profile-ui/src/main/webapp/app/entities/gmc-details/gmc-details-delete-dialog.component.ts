import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {GmcDetails} from "./gmc-details.model";
import {GmcDetailsPopupService} from "./gmc-details-popup.service";
import {GmcDetailsService} from "./gmc-details.service";

@Component({
	selector: 'jhi-gmc-details-delete-dialog',
	templateUrl: './gmc-details-delete-dialog.component.html'
})
export class GmcDetailsDeleteDialogComponent {

	gmcDetails: GmcDetails;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gmcDetailsService: GmcDetailsService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['gmcDetails']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.gmcDetailsService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'gmcDetailsListModification',
				content: 'Deleted an gmcDetails'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-gmc-details-delete-popup',
	template: ''
})
export class GmcDetailsDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private gmcDetailsPopupService: GmcDetailsPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.gmcDetailsPopupService
				.open(GmcDetailsDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
