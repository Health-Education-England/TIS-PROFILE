export class Immigration {
	constructor(public id?: number,
				public tisId?: number,
				public eeaResident?: boolean,
				public permitToWork?: string,
				public settled?: string,
				public visaIssued?: any,
				public visaValidTo?: any,
				public visaDetailsNumber?: string,) {
		this.eeaResident = false;
	}
}
