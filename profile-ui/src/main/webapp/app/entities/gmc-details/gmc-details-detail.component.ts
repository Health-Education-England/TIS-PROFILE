import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {GmcDetails} from "./gmc-details.model";
import {GmcDetailsService} from "./gmc-details.service";

@Component({
	selector: 'jhi-gmc-details-detail',
	templateUrl: './gmc-details-detail.component.html'
})
export class GmcDetailsDetailComponent implements OnInit, OnDestroy {

	gmcDetails: GmcDetails;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gmcDetailsService: GmcDetailsService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['gmcDetails']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.gmcDetailsService.find(id).subscribe(gmcDetails => {
			this.gmcDetails = gmcDetails;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
