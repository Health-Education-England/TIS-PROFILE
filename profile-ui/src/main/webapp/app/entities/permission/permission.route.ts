import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {PermissionComponent} from "./permission.component";
import {PermissionDetailComponent} from "./permission-detail.component";
import {PermissionPopupComponent} from "./permission-dialog.component";
import {PermissionDeletePopupComponent} from "./permission-delete-dialog.component";

@Injectable()
export class PermissionResolvePagingParams implements Resolve<any> {

	constructor(private paginationUtil: PaginationUtil) {
	}

	resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
		let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
		return {
			page: this.paginationUtil.parsePage(page),
			predicate: this.paginationUtil.parsePredicate(sort),
			ascending: this.paginationUtil.parseAscending(sort)
		};
	}
}

export const permissionRoute: Routes = [
	{
		path: 'permission',
		component: PermissionComponent,
		resolve: {
			'pagingParams': PermissionResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.permission.home.title'
		}
	}, {
		path: 'permission/:id',
		component: PermissionDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.permission.home.title'
		}
	}
];

export const permissionPopupRoute: Routes = [
	{
		path: 'permission-new',
		component: PermissionPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.permission.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'permission/:id/edit',
		component: PermissionPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.permission.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'permission/:id/delete',
		component: PermissionDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.permission.home.title'
		},
		outlet: 'popup'
	}
];
