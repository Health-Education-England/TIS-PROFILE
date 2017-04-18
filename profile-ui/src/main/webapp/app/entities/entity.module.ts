import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {ProfileManageRecordModule} from "./manage-record/manage-record.module";
import {ProfileEqualityAndDiversityModule} from "./equality-and-diversity/equality-and-diversity.module";
import {ProfileGmcDetailsModule} from "./gmc-details/gmc-details.module";
import {ProfileHeeUserModule} from "./hee-user/hee-user.module";
import {ProfileImmigrationModule} from "./immigration/immigration.module";
import {ProfileGdcDetailsModule} from "./gdc-details/gdc-details.module";
import {ProfilePermissionModule} from "./permission/permission.module";
import {ProfilePersonModule} from "./person/person.module";
import {ProfilePersonalDetailsModule} from "./personal-details/personal-details.module";
import {ProfileQualificationModule} from "./qualification/qualification.module";
import {ProfileRoleModule} from "./role/role.module";
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
	imports: [
		ProfileManageRecordModule,
		ProfileEqualityAndDiversityModule,
		ProfileGmcDetailsModule,
		ProfileHeeUserModule,
		ProfileImmigrationModule,
		ProfileGdcDetailsModule,
		ProfilePermissionModule,
		ProfilePersonModule,
		ProfilePersonalDetailsModule,
		ProfileQualificationModule,
		ProfileRoleModule,
		/* jhipster-needle-add-entity-module - JHipster will add entity modules here */
	],
	declarations: [],
	entryComponents: [],
	providers: [],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProfileEntityModule {
}
