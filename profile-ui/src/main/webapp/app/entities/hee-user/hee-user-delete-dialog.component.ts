import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {HeeUser} from "./hee-user.model";
import {HeeUserPopupService} from "./hee-user-popup.service";
import {HeeUserService} from "./hee-user.service";

@Component({
	selector: 'jhi-hee-user-delete-dialog',
	templateUrl: './hee-user-delete-dialog.component.html'
})
export class HeeUserDeleteDialogComponent {

	heeUser: HeeUser;

	constructor(private jhiLanguageService: JhiLanguageService,
				private heeUserService: HeeUserService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['heeUser']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(name: string) {
		this.heeUserService.delete(name).subscribe(response => {
			this.eventManager.broadcast({
				name: 'heeUserListModification',
				content: 'Deleted an heeUser'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-hee-user-delete-popup',
	template: ''
})
export class HeeUserDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private heeUserPopupService: HeeUserPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.heeUserPopupService
				.open(HeeUserDeleteDialogComponent, params['name']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
