import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {Immigration} from "./immigration.model";
import {ImmigrationPopupService} from "./immigration-popup.service";
import {ImmigrationService} from "./immigration.service";

@Component({
	selector: 'jhi-immigration-delete-dialog',
	templateUrl: './immigration-delete-dialog.component.html'
})
export class ImmigrationDeleteDialogComponent {

	immigration: Immigration;

	constructor(private jhiLanguageService: JhiLanguageService,
				private immigrationService: ImmigrationService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['immigration']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.immigrationService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'immigrationListModification',
				content: 'Deleted an immigration'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-immigration-delete-popup',
	template: ''
})
export class ImmigrationDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private immigrationPopupService: ImmigrationPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.immigrationPopupService
				.open(ImmigrationDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
