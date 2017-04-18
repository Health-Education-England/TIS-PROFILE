import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ProfileSharedModule} from "../../shared";
import {
	GdcDetailsService,
	GdcDetailsPopupService,
	GdcDetailsComponent,
	GdcDetailsDetailComponent,
	GdcDetailsDialogComponent,
	GdcDetailsPopupComponent,
	GdcDetailsDeletePopupComponent,
	GdcDetailsDeleteDialogComponent,
	gdcDetailsRoute,
	gdcDetailsPopupRoute,
	GdcDetailsResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...gdcDetailsRoute,
	...gdcDetailsPopupRoute,
];

@NgModule({
	imports: [
		ProfileSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		GdcDetailsComponent,
		GdcDetailsDetailComponent,
		GdcDetailsDialogComponent,
		GdcDetailsDeleteDialogComponent,
		GdcDetailsPopupComponent,
		GdcDetailsDeletePopupComponent,
	],
	entryComponents: [
		GdcDetailsComponent,
		GdcDetailsDialogComponent,
		GdcDetailsPopupComponent,
		GdcDetailsDeleteDialogComponent,
		GdcDetailsDeletePopupComponent,
	],
	providers: [
		GdcDetailsService,
		GdcDetailsPopupService,
		GdcDetailsResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProfileGdcDetailsModule {
}
