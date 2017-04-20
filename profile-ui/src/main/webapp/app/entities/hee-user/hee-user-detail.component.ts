import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {HeeUser} from "./hee-user.model";
import {HeeUserService} from "./hee-user.service";

@Component({
	selector: 'jhi-hee-user-detail',
	templateUrl: './hee-user-detail.component.html'
})
export class HeeUserDetailComponent implements OnInit, OnDestroy {

	heeUser: HeeUser;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private heeUserService: HeeUserService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['heeUser']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['name']);
		});
	}

	load(name) {
		this.heeUserService.find(name).subscribe(heeUser => {
			this.heeUser = heeUser;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
