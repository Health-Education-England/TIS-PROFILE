import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Qualification} from "./qualification.model";
import {QualificationService} from "./qualification.service";

@Component({
	selector: 'jhi-qualification-detail',
	templateUrl: './qualification-detail.component.html'
})
export class QualificationDetailComponent implements OnInit, OnDestroy {

	qualification: Qualification;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private qualificationService: QualificationService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['qualification']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.qualificationService.find(id).subscribe(qualification => {
			this.qualification = qualification;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
