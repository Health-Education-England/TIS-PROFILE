import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {GmcDetailsComponent} from "./gmc-details.component";
import {GmcDetailsDetailComponent} from "./gmc-details-detail.component";
import {GmcDetailsPopupComponent} from "./gmc-details-dialog.component";
import {GmcDetailsDeletePopupComponent} from "./gmc-details-delete-dialog.component";

@Injectable()
export class GmcDetailsResolvePagingParams implements Resolve<any> {

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

export const gmcDetailsRoute: Routes = [
	{
		path: 'gmc-details',
		component: GmcDetailsComponent,
		resolve: {
			'pagingParams': GmcDetailsResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.gmcDetails.home.title'
		}
	}, {
		path: 'gmc-details/:id',
		component: GmcDetailsDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.gmcDetails.home.title'
		}
	}
];

export const gmcDetailsPopupRoute: Routes = [
	{
		path: 'gmc-details-new',
		component: GmcDetailsPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.gmcDetails.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'gmc-details/:id/edit',
		component: GmcDetailsPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.gmcDetails.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'gmc-details/:id/delete',
		component: GmcDetailsDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.gmcDetails.home.title'
		},
		outlet: 'popup'
	}
];
