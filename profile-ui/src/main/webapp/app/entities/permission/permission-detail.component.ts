import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Permission} from "./permission.model";
import {PermissionService} from "./permission.service";

@Component({
	selector: 'jhi-permission-detail',
	templateUrl: './permission-detail.component.html'
})
export class PermissionDetailComponent implements OnInit, OnDestroy {

	permission: Permission;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private permissionService: PermissionService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['permission']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['name']);
		});
	}

	load(name) {
		this.permissionService.find(name).subscribe(permission => {
			this.permission = permission;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
