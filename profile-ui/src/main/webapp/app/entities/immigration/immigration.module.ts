import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ProfileSharedModule} from "../../shared";
import {
	ImmigrationService,
	ImmigrationPopupService,
	ImmigrationComponent,
	ImmigrationDetailComponent,
	ImmigrationDialogComponent,
	ImmigrationPopupComponent,
	ImmigrationDeletePopupComponent,
	ImmigrationDeleteDialogComponent,
	immigrationRoute,
	immigrationPopupRoute,
	ImmigrationResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...immigrationRoute,
	...immigrationPopupRoute,
];

@NgModule({
	imports: [
		ProfileSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		ImmigrationComponent,
		ImmigrationDetailComponent,
		ImmigrationDialogComponent,
		ImmigrationDeleteDialogComponent,
		ImmigrationPopupComponent,
		ImmigrationDeletePopupComponent,
	],
	entryComponents: [
		ImmigrationComponent,
		ImmigrationDialogComponent,
		ImmigrationPopupComponent,
		ImmigrationDeleteDialogComponent,
		ImmigrationDeletePopupComponent,
	],
	providers: [
		ImmigrationService,
		ImmigrationPopupService,
		ImmigrationResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProfileImmigrationModule {
}
