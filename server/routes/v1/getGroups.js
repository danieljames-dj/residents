module.exports = function(req, res, db, uid) {
	data = {
		groups: []
	};
	db.users.findOne({
		uid: uid
	}, (error, result) => {
		if (error || !result) {
			res.send({
				success: false,
				message: "Something went wrong in fetching database"
			});
		} else {
			if (result.groups != undefined) {
				fetchGroupDetails(req, res, db, data, result.groups);
			} else {
				res.send({
					success: true,
					data: updateDataDateTime(data)
				});
			}
		}
	});
}

const updateDataDateTime = (data) => {
	data.lastFetched = new Date().toLocaleString();
	return data;
}

async function fetchGroupDetails(req, res, db, data, groups) {
	for (let i = 0; i < groups.length; i++) {
		let groupId = groups[i].substring(0, groups[i].length-1);
		let permission = groups[i].substring(groups[i].length-1);
		let result = await db.groups.findOne({
			gid: groupId
		});
		if (result) {
			data.groups.push({
				gid: groupId,
				permission: permission,
				gname: result.gname,
				list: result.list,
				subGroups: result.subGroups,
				inputDetails: result.inputDetails,
				personDetails: result.personDetails
			});
		}
	}
	res.send({
		success: true,
		data: updateDataDateTime(data)
	});
}