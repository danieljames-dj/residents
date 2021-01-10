module.exports = async function(req, res, firestore, uid) {
	const user = await firestore.users.where('uid', '==', uid).get()
	fetchGroupDetails(req, res, firestore, user.docs[0].data().groups);
}

const updateDataDateTime = (data) => {
	data.lastFetched = new Date().toLocaleString();
	return data;
}

async function fetchGroupDetails(req, res, firestore, groups) {
	data = {
		groups: []
	};
	for (let i = 0; i < groups.length; i++) {
		let groupId = groups[i].substring(0, groups[i].length-1);
		let permission = groups[i].substring(groups[i].length-1);
		let result = await firestore.groups.doc(groupId).get()
		let resultData = result.data()
		if (resultData) {
			data.groups.push({
				gid: groupId,
				permission: permission,
				gname: resultData.gname,
				summary: resultData.summary,
				list: resultData.list,
				subGroups: resultData.subGroups,
				inputDetails: resultData.inputDetails,
				personDetails: resultData.personDetails
			});
		}
	}
	res.send({
		data: updateDataDateTime(data)
	});
}