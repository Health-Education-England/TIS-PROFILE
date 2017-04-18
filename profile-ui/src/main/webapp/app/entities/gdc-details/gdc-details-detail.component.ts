import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {GdcDetails} from "./gdc-details.model";
import {GdcDetailsService} from "./gdc-details.service";

@Component({
	selector: 'jhi-gdc-details-detail',
	templateUrl: './gdc-details-detail.component.html'
})
export class GdcDetailsDetailComponent implements OnInit, OnDestroy {

	gdcDetails: GdcDetails;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gdcDetailsService: GdcDetailsService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['gdcDetails']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.gdcDetailsService.find(id).subscribe(gdcDetails => {
			this.gdcDetails = gdcDetails;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
