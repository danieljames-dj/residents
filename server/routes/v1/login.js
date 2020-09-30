/*
Types of login:
1. New user, email ID/phone number does not exists in our database
2. New user, but email ID/phone number exists already
3. Already existing user
*/

module.exports = function(req, res, db, uid) {
	if (uid != req.body.uid) {
		res.send({
			success: false,
			message: "The token and uid doesn't match"
		});
	} else {
		db.users.findOne({
			uid: uid
		}, (error, result) => {
			if (error) {
				res.send({
					success: false,
					message: "Something went wrong in fetching database"
				});
			} else {
				if (result != undefined) { // case 3: already existing user
					res.send({
						success: true
					});
				} else {
					db.users.findOne({
						email: req.body.email
					}, (error, result) => {
						if (error) {
							res.send({
								success: false,
								message: "Something went wrong in fetching database"
							});
						} else {
							if (result == undefined) { // case 1: New user, email ID/phone number does not exists in our database
								createUser(req, res, db);
							} else { // case 2: New user, but email ID/phone number exists already
								if (result.uid) {
									res.send({
										success: false,
										message: "Email ID already used by another account"
									});
								} else {
									updateUid(req, res, db);
								}
							}
						}
					});
				}
			}
		});
	}
}

async function createUser(req, res, db) {
	await db.users.insertOne({
		uid: req.body.uid,
		email: req.body.email,
		name: req.body.name
	}, (error, result) => {
		if (error) {
			res.send({
				success: false,
				message: "Something went wrong in insertion to database"
			});
		} else {
			res.send({
				success: true
			});
		}
	});
}

async function updateUid(req, res, db) {
	await db.users.findOneAndUpdate(
		{ email: req.body.email },
		{$set: {
			uid: req.body.uid
		}},
		{ upsert: true, returnOriginal: false }
	);
	db.users.findOne({
		uid: uid
	}, (error, result) => {
		if (error) {
			res.send({
				success: false,
				message: "Something went wrong in fetching database"
			});
		} else {
			console.log(result)
			res.send({
				success: true
			});
		}
	});
}