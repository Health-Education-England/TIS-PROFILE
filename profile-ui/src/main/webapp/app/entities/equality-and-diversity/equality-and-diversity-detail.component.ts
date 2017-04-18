import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {EqualityAndDiversity} from "./equality-and-diversity.model";
import {EqualityAndDiversityService} from "./equality-and-diversity.service";

@Component({
	selector: 'jhi-equality-and-diversity-detail',
	templateUrl: './equality-and-diversity-detail.component.html'
})
export class EqualityAndDiversityDetailComponent implements OnInit, OnDestroy {

	equalityAndDiversity: EqualityAndDiversity;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private equalityAndDiversityService: EqualityAndDiversityService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['equalityAndDiversity']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.equalityAndDiversityService.find(id).subscribe(equalityAndDiversity => {
			this.equalityAndDiversity = equalityAndDiversity;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
