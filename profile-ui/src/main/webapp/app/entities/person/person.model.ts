export class Person {
	constructor(public id?: number,
				public tisId?: number,
				public publicHealthId?: string,
				public active?: boolean,) {
		this.active = false;
	}
}
