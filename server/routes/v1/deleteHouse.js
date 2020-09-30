const { db } = require("../../mongo-connector");

module.exports = async function(req, res) {
	const gid = req.body.gid
	const hid = req.body.hid
	console.log(gid, hid)
	await db.groups.updateOne({
		gid: gid
	}, {
		$pull: {
			"list": {
				id: hid
			}
		}
	});
	res.send()
}