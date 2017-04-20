import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {HeeUserComponent} from "./hee-user.component";
import {HeeUserDetailComponent} from "./hee-user-detail.component";
import {HeeUserPopupComponent} from "./hee-user-dialog.component";
import {HeeUserDeletePopupComponent} from "./hee-user-delete-dialog.component";

@Injectable()
export class HeeUserResolvePagingParams implements Resolve<any> {

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

export const heeUserRoute: Routes = [
	{
		path: 'hee-user',
		component: HeeUserComponent,
		resolve: {
			'pagingParams': HeeUserResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.heeUser.home.title'
		}
	}, {
		path: 'hee-user/:name',
		component: HeeUserDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.heeUser.home.title'
		}
	}
];

export const heeUserPopupRoute: Routes = [
	{
		path: 'hee-user-new',
		component: HeeUserPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.heeUser.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'hee-user/:name/edit',
		component: HeeUserPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.heeUser.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'hee-user/:name/delete',
		component: HeeUserDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.heeUser.home.title'
		},
		outlet: 'popup'
	}
];
