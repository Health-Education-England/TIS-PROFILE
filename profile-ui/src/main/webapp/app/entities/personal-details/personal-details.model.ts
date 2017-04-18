export class PersonalDetails {
	constructor(public id?: number,
				public tisId?: number,
				public surnameNb?: string,
				public legalSurname?: string,
				public forenames?: string,
				public legalForenames?: string,
				public knownAs?: string,
				public maidenName?: string,
				public initials?: string,
				public title?: string,
				public telephoneNumber?: string,
				public mobileNumber?: string,
				public emailAddress?: string,
				public correspondenceAddress?: string,
				public correspondenceAddressPostCode?: string,
				public status?: string,) {
	}
}
