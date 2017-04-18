import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {EqualityAndDiversityComponent} from "./equality-and-diversity.component";
import {EqualityAndDiversityDetailComponent} from "./equality-and-diversity-detail.component";
import {EqualityAndDiversityPopupComponent} from "./equality-and-diversity-dialog.component";
import {EqualityAndDiversityDeletePopupComponent} from "./equality-and-diversity-delete-dialog.component";

@Injectable()
export class EqualityAndDiversityResolvePagingParams implements Resolve<any> {

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

export const equalityAndDiversityRoute: Routes = [
	{
		path: 'equality-and-diversity',
		component: EqualityAndDiversityComponent,
		resolve: {
			'pagingParams': EqualityAndDiversityResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.equalityAndDiversity.home.title'
		}
	}, {
		path: 'equality-and-diversity/:id',
		component: EqualityAndDiversityDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.equalityAndDiversity.home.title'
		}
	}
];

export const equalityAndDiversityPopupRoute: Routes = [
	{
		path: 'equality-and-diversity-new',
		component: EqualityAndDiversityPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.equalityAndDiversity.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'equality-and-diversity/:id/edit',
		component: EqualityAndDiversityPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.equalityAndDiversity.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'equality-and-diversity/:id/delete',
		component: EqualityAndDiversityDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'profileApp.equalityAndDiversity.home.title'
		},
		outlet: 'popup'
	}
];
