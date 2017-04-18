export class EqualityAndDiversity {
	constructor(public id?: number,
				public tisId?: number,
				public maritalStatus?: string,
				public dateOfBirth?: any,
				public gender?: string,
				public nationality?: string,
				public dualNationality?: string,
				public sexualOrientation?: string,
				public religiousBelief?: string,
				public ethnicOrigin?: string,
				public disability?: boolean,
				public disabilityDetails?: string,) {
		this.disability = false;
	}
}
