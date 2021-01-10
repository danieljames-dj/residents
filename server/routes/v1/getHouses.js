const { db } = {users: ""}

module.exports = async function(req, res, firestore) {
	const gid = req.body.gid
	const groupDoc = await firestore.groups.doc(gid).get();
	res.send(groupDoc.data().list)
}