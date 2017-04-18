import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {PersonalDetailsComponent} from "./personal-details.component";
import {PersonalDetailsDetailComponent} from "./personal-details-detail.component";
import {PersonalDetailsPopupComponent} from "./personal-details-dialog.component";
import {PersonalDetailsDeletePopupComponent} from "./personal-details-delete-dialog.component";

@Injectable()
export class PersonalDetailsResolvePagingParams implements Resolve<any> {

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

export const personalDetailsRoute: Routes = [
	{
		path: 'personal-details',
		component: PersonalDetailsComponent,
		resolve: {
			'pagingParams': PersonalDetailsResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.personalDetails.home.title'
		}
	}, {
		path: 'personal-details/:id',
		component: PersonalDetailsDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.personalDetails.home.title'
		}
	}
];

export const personalDetailsPopupRoute: Routes = [
	{
		path: 'personal-details-new',
		component: PersonalDetailsPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.personalDetails.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'personal-details/:id/edit',
		component: PersonalDetailsPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.personalDetails.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'personal-details/:id/delete',
		component: PersonalDetailsDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.personalDetails.home.title'
		},
		outlet: 'popup'
	}
];
