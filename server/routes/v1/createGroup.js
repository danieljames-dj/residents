module.exports = async function(req, res, firestore, uid) {
	const user = await firestore.users.where('uid', '==', uid).get()
	if (validate(req.body.gname)) {
		createGroup(req, res, firestore, uid, {
			gid: generateId(),
			gname: req.body.gname,
			summary: req.body.summary || "",
			subGroups: req.body.subGroups === "" ? [] : req.body.subGroups.split(","),
			inputDetails: req.body.inputDetails === "" ? [] : req.body.inputDetails.split(","),
			personDetails: req.body.personDetails === "" ? [] : req.body.personDetails.split(","),
			list: {},
			admins: [user.docs[0].data().email],
			members: []
		});
	} else {
		res.status(400).send("Validation failed")
	}
}

const validate = (name) => {
	return name && name.length > 0;
}

const generateId = () => {
	const { v4: uuidv4 } = require('uuid');
	return uuidv4();
}

async function createGroup(req, res, firestore, uid, groupJson) {
	const groupDoc = firestore.groups.doc(groupJson.gid);
	await groupDoc.set(groupJson);
	const user = await firestore.users.where('uid', '==', uid).get()
	await user.docs[0].ref.update({
		groups: firestore.FieldValue.arrayUnion(groupJson.gid + "1")
	})
	res.send();
	// await db.groups.insertOne(groupJson, (error, result) => {
	// 	if (error) {
	// 		res.send({
	// 			success: false,
	// 			message: "Something went wrong in insertion to database"
	// 		});
	// 	} else {
	// 		db.users.updateOne({
	// 			uid: uid
	// 		}, {
	// 			$push: {
	// 				groups: {
	// 					$each: [groupJson.gid + "1"]
	// 				}
	// 			}
	// 		}, (error, result) => {
	// 			if (error) {
	// 				res.send({
	// 					success: false,
	// 					message: "Something went wrong in insertion to database"
	// 				});
	// 			} else {
	// 				res.send({
	// 					success: true
	// 				});
	// 			}
	// 		});
	// 	}
	// });
}