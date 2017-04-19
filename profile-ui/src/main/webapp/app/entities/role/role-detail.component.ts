import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Role} from "./role.model";
import {RoleService} from "./role.service";

@Component({
	selector: 'jhi-role-detail',
	templateUrl: './role-detail.component.html'
})
export class RoleDetailComponent implements OnInit, OnDestroy {

	role: Role;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private roleService: RoleService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['role']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['name']);
		});
	}

	load(name) {
		this.roleService.find(name).subscribe(role => {
			this.role = role;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
