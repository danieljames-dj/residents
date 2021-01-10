const { db } = {users: ""}

module.exports = async function(req, res, firestore) {
	const gid = req.body.gid
	const email = req.body.email
	let userObj = await firestore.users.where('email', '==', email).get()
	if (userObj.empty) {
		firestore.users.add({
			uid: null,
			email: email,
			name: "",
			groups: [gid + "0"]
		})
	} else {
		userObj.docs[0].ref.update({
			groups: firestore.FieldValue.arrayUnion(gid + "0")
		})
	}
	await addUserToGroup(email, gid, firestore)
	res.send()
}

const addUserToGroup = async (email, gid, firestore) => {
	const groupDoc = await firestore.groups.doc(gid).get();
	await groupDoc.ref.update({
		members: firestore.FieldValue.arrayUnion(email)
	})
}