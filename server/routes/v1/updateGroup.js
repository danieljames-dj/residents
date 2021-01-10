module.exports = function(req, res, firestore, uid) {
	if (validate(req.body.gname)) {
		updateGroup(req, res, firestore, uid, {
			gid: req.body.gid,
			gname: req.body.gname,
			summary: req.body.summary,
			subGroups: req.body.subGroups === "" ? [] : req.body.subGroups.split(","),
			inputDetails: req.body.inputDetails === "" ? [] : req.body.inputDetails.split(","),
			personDetails: req.body.personDetails === "" ? [] : req.body.personDetails.split(",")
		});
	} else {
		res.status(400).send("Validation failed")
	}
}

const validate = (name) => {
	return name.length > 0;
}

async function updateGroup(req, res, firestore, uid, groupJson) {
	const groupDoc = await firestore.groups.doc(groupJson.gid).get();
	await groupDoc.ref.update({
		gname: groupJson.gname,
		summary: groupJson.summary,
		subGroups: groupJson.subGroups,
		inputDetails: groupJson.inputDetails,
		personDetails: groupJson.personDetails
	})
	res.send()
}