import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Immigration} from "./immigration.model";
import {ImmigrationService} from "./immigration.service";

@Component({
	selector: 'jhi-immigration-detail',
	templateUrl: './immigration-detail.component.html'
})
export class ImmigrationDetailComponent implements OnInit, OnDestroy {

	immigration: Immigration;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private immigrationService: ImmigrationService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['immigration']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.immigrationService.find(id).subscribe(immigration => {
			this.immigration = immigration;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
