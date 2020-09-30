const { db } = require("../../mongo-connector");

module.exports = async function(req, res) {
	const gid = req.body.gid
	let houseDetails = req.body.houseDetails
	houseDetails = JSON.parse(houseDetails)
	houseDetails.id = generateId()
	await addHouseDetails(houseDetails, gid)
	res.send()
}

const generateId = () => {
	const { v4: uuidv4 } = require('uuid');
	return uuidv4();
}

const addHouseDetails = async (houseDetails, gid) => {
	await db.groups.updateOne({
		gid: gid
	}, {
		$push: {
			list: {
				$each: [(houseDetails)]
			}
		}
	});
}