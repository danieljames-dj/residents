const { db } = require("../../mongo-connector");

module.exports = async function(req, res) {
	const gid = req.body.gid
	const email = req.body.email
	const uid = await getUidFromEmail(email)
	if (uid) {
		await addGroupMemberStatusToUser(uid, gid)
		await addUserToGroup(uid, gid)
	} else {
		await addGroupMemberStatusToDummyUser(email, gid)
		await addUserToGroup(email, gid)
	}
	res.send()
}

const getUidFromEmail = async (email) => {
	const result = await db.users.findOne({
		email: email
	})
	if (!result) {
		createDummyUser(email)
	}
	return result && result.uid
}

const addGroupMemberStatusToUser = async (uid, gid) => {
	await db.users.updateOne({
		uid: uid
	}, {
		$push: {
			groups: {
				$each: [gid + "0"]
			}
		}
	})
}

const addGroupMemberStatusToDummyUser = async (email, gid) => {
	await db.users.updateOne({
		email: email
	}, {
		$push: {
			groups: {
				$each: [gid + "0"]
			}
		}
	})
}

const addUserToGroup = async (user, gid) => {
	await db.groups.updateOne({
		gid: gid
	}, {
		$push: {
			members: {
				$each: [user]
			}
		}
	});
}

const createDummyUser = async (email) => {
	await db.users.insertOne({
		uid: null,
		email: email
	})
}