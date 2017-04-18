import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {GdcDetailsComponent} from "./gdc-details.component";
import {GdcDetailsDetailComponent} from "./gdc-details-detail.component";
import {GdcDetailsPopupComponent} from "./gdc-details-dialog.component";
import {GdcDetailsDeletePopupComponent} from "./gdc-details-delete-dialog.component";

@Injectable()
export class GdcDetailsResolvePagingParams implements Resolve<any> {

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

export const gdcDetailsRoute: Routes = [
	{
		path: 'gdc-details',
		component: GdcDetailsComponent,
		resolve: {
			'pagingParams': GdcDetailsResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.gdcDetails.home.title'
		}
	}, {
		path: 'gdc-details/:id',
		component: GdcDetailsDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.gdcDetails.home.title'
		}
	}
];

export const gdcDetailsPopupRoute: Routes = [
	{
		path: 'gdc-details-new',
		component: GdcDetailsPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.gdcDetails.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'gdc-details/:id/edit',
		component: GdcDetailsPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.gdcDetails.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'gdc-details/:id/delete',
		component: GdcDetailsDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.gdcDetails.home.title'
		},
		outlet: 'popup'
	}
];
