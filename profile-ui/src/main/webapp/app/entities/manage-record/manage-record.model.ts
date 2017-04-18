export class ManageRecord {
	constructor(public id?: number,
				public tisId?: number,
				public recordType?: string,
				public role?: string,
				public recordStatus?: string,
				public inactiveFrom?: any,
				public changedBy?: number,
				public inactiveReason?: string,
				public inactiveDate?: any,
				public deletionReason?: string,) {
	}
}
