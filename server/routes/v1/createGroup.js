module.exports = function(req, res, db, uid) {
	if (validate(req.body.gname)) {
		createGroup(req, res, db, uid, {
			gid: generateId(),
			gname: req.body.gname,
			subGroups: req.body.subGroups.split(","),
			inputDetails: req.body.inputDetails.split(","),
			personDetails: req.body.personDetails.split(","),
			list: [],
			admins: [uid],
			members: []
		});
	} else {
		res.send({
			success: false,
			message: "Validation failed"
		});
	}
}

const validate = (name) => {
	return name.length > 0;
}

const generateId = () => {
	const { v4: uuidv4 } = require('uuid');
	return uuidv4();
}

async function createGroup(req, res, db, uid, groupJson) {
	await db.groups.insertOne(groupJson, (error, result) => {
		if (error) {
			res.send({
				success: false,
				message: "Something went wrong in insertion to database"
			});
		} else {
			db.users.updateOne({
				uid: uid
			}, {
				$push: {
					groups: {
						$each: [groupJson.gid + "1"]
					}
				}
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
	});
}