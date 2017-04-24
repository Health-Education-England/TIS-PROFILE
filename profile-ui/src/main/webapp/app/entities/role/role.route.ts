import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {RoleComponent} from "./role.component";
import {RoleDetailComponent} from "./role-detail.component";
import {RolePopupComponent} from "./role-dialog.component";
import {RoleDeletePopupComponent} from "./role-delete-dialog.component";

@Injectable()
export class RoleResolvePagingParams implements Resolve<any> {

	constructor(private paginationUtil: PaginationUtil) {
	}

	resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
		let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'name,asc';
		return {
			page: this.paginationUtil.parsePage(page),
			predicate: this.paginationUtil.parsePredicate(sort),
			ascending: this.paginationUtil.parseAscending(sort)
		};
	}
}

export const roleRoute: Routes = [
	{
		path: 'role',
		component: RoleComponent,
		resolve: {
			'pagingParams': RoleResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.role.home.title'
		}
	}, {
		path: 'role/:name',
		component: RoleDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.role.home.title'
		}
	}
];

export const rolePopupRoute: Routes = [
	{
		path: 'role-new',
		component: RolePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.role.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'role/:name/edit',
		component: RolePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.role.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'role/:name/delete',
		component: RoleDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.role.home.title'
		},
		outlet: 'popup'
	}
];