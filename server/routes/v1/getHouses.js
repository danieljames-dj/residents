const { db } = require("../../mongo-connector");

module.exports = async function(req, res) {
	const gid = req.body.gid
	res.send(await getHouses(gid))
}

const getHouses = async (gid) => {
	const result = await db.groups.findOne({
		gid: gid
	})
	return result ? result.list : []
}