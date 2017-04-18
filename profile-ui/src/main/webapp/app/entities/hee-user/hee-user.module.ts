import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ProfileSharedModule} from "../../shared";
import {
	HeeUserService,
	HeeUserPopupService,
	HeeUserComponent,
	HeeUserDetailComponent,
	HeeUserDialogComponent,
	HeeUserPopupComponent,
	HeeUserDeletePopupComponent,
	HeeUserDeleteDialogComponent,
	heeUserRoute,
	heeUserPopupRoute,
	HeeUserResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...heeUserRoute,
	...heeUserPopupRoute,
];

@NgModule({
	imports: [
		ProfileSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		HeeUserComponent,
		HeeUserDetailComponent,
		HeeUserDialogComponent,
		HeeUserDeleteDialogComponent,
		HeeUserPopupComponent,
		HeeUserDeletePopupComponent,
	],
	entryComponents: [
		HeeUserComponent,
		HeeUserDialogComponent,
		HeeUserPopupComponent,
		HeeUserDeleteDialogComponent,
		HeeUserDeletePopupComponent,
	],
	providers: [
		HeeUserService,
		HeeUserPopupService,
		HeeUserResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProfileHeeUserModule {
}
