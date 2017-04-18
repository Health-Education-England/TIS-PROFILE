import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ProfileSharedModule} from "../../shared";
import {
	GmcDetailsService,
	GmcDetailsPopupService,
	GmcDetailsComponent,
	GmcDetailsDetailComponent,
	GmcDetailsDialogComponent,
	GmcDetailsPopupComponent,
	GmcDetailsDeletePopupComponent,
	GmcDetailsDeleteDialogComponent,
	gmcDetailsRoute,
	gmcDetailsPopupRoute,
	GmcDetailsResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...gmcDetailsRoute,
	...gmcDetailsPopupRoute,
];

@NgModule({
	imports: [
		ProfileSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		GmcDetailsComponent,
		GmcDetailsDetailComponent,
		GmcDetailsDialogComponent,
		GmcDetailsDeleteDialogComponent,
		GmcDetailsPopupComponent,
		GmcDetailsDeletePopupComponent,
	],
	entryComponents: [
		GmcDetailsComponent,
		GmcDetailsDialogComponent,
		GmcDetailsPopupComponent,
		GmcDetailsDeleteDialogComponent,
		GmcDetailsDeletePopupComponent,
	],
	providers: [
		GmcDetailsService,
		GmcDetailsPopupService,
		GmcDetailsResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProfileGmcDetailsModule {
}
