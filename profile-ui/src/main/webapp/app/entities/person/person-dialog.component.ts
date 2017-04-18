import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Person} from "./person.model";
import {PersonPopupService} from "./person-popup.service";
import {PersonService} from "./person.service";
@Component({
	selector: 'jhi-person-dialog',
	templateUrl: './person-dialog.component.html'
})
export class PersonDialogComponent implements OnInit {

	person: Person;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private personService: PersonService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['person']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.person.id !== undefined) {
			this.personService.update(this.person)
				.subscribe((res: Person) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.personService.create(this.person)
				.subscribe((res: Person) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Person) {
		this.eventManager.broadcast({name: 'personListModification', content: 'OK'});
		this.isSaving = false;
		this.activeModal.dismiss(result);
	}

	private onSaveError(error) {
		this.isSaving = false;
		this.onError(error);
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}

@Component({
	selector: 'jhi-person-popup',
	template: ''
})
export class PersonPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private personPopupService: PersonPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.personPopupService
					.open(PersonDialogComponent, params['id']);
			} else {
				this.modalRef = this.personPopupService
					.open(PersonDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
