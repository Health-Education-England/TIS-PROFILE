import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ProfileSharedModule} from "../../shared";
import {
	PersonService,
	PersonPopupService,
	PersonComponent,
	PersonDetailComponent,
	PersonDialogComponent,
	PersonPopupComponent,
	PersonDeletePopupComponent,
	PersonDeleteDialogComponent,
	personRoute,
	personPopupRoute,
	PersonResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...personRoute,
	...personPopupRoute,
];

@NgModule({
	imports: [
		ProfileSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		PersonComponent,
		PersonDetailComponent,
		PersonDialogComponent,
		PersonDeleteDialogComponent,
		PersonPopupComponent,
		PersonDeletePopupComponent,
	],
	entryComponents: [
		PersonComponent,
		PersonDialogComponent,
		PersonPopupComponent,
		PersonDeleteDialogComponent,
		PersonDeletePopupComponent,
	],
	providers: [
		PersonService,
		PersonPopupService,
		PersonResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProfilePersonModule {
}
