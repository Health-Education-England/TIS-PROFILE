import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {ImmigrationComponent} from "./immigration.component";
import {ImmigrationDetailComponent} from "./immigration-detail.component";
import {ImmigrationPopupComponent} from "./immigration-dialog.component";
import {ImmigrationDeletePopupComponent} from "./immigration-delete-dialog.component";

@Injectable()
export class ImmigrationResolvePagingParams implements Resolve<any> {

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

export const immigrationRoute: Routes = [
	{
		path: 'immigration',
		component: ImmigrationComponent,
		resolve: {
			'pagingParams': ImmigrationResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.immigration.home.title'
		}
	}, {
		path: 'immigration/:id',
		component: ImmigrationDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.immigration.home.title'
		}
	}
];

export const immigrationPopupRoute: Routes = [
	{
		path: 'immigration-new',
		component: ImmigrationPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.immigration.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'immigration/:id/edit',
		component: ImmigrationPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.immigration.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'immigration/:id/delete',
		component: ImmigrationDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.immigration.home.title'
		},
		outlet: 'popup'
	}
];
