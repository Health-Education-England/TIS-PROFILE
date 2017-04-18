export class HeeUser {
	constructor(public id?: number,
				public name?: string,
				public firstName?: string,
				public lastName?: string,
				public gmcId?: string,
				public phoneNumber?: string,
				public emailAddress?: string,
				public active?: boolean,) {
		this.active = false;
	}
}
