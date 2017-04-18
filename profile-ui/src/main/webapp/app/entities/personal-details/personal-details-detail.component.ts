import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {PersonalDetails} from "./personal-details.model";
import {PersonalDetailsService} from "./personal-details.service";

@Component({
	selector: 'jhi-personal-details-detail',
	templateUrl: './personal-details-detail.component.html'
})
export class PersonalDetailsDetailComponent implements OnInit, OnDestroy {

	personalDetails: PersonalDetails;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private personalDetailsService: PersonalDetailsService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['personalDetails']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.personalDetailsService.find(id).subscribe(personalDetails => {
			this.personalDetails = personalDetails;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
