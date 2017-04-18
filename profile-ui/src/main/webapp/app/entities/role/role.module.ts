import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ProfileSharedModule} from "../../shared";
import {
	RoleService,
	RolePopupService,
	RoleComponent,
	RoleDetailComponent,
	RoleDialogComponent,
	RolePopupComponent,
	RoleDeletePopupComponent,
	RoleDeleteDialogComponent,
	roleRoute,
	rolePopupRoute,
	RoleResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...roleRoute,
	...rolePopupRoute,
];

@NgModule({
	imports: [
		ProfileSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		RoleComponent,
		RoleDetailComponent,
		RoleDialogComponent,
		RoleDeleteDialogComponent,
		RolePopupComponent,
		RoleDeletePopupComponent,
	],
	entryComponents: [
		RoleComponent,
		RoleDialogComponent,
		RolePopupComponent,
		RoleDeleteDialogComponent,
		RoleDeletePopupComponent,
	],
	providers: [
		RoleService,
		RolePopupService,
		RoleResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProfileRoleModule {
}
