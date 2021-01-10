module.exports = async function(req, res, firestore) {
	const gid = req.body.gid
	const groupDoc = await firestore.groups.doc(gid).get();
	let houseJson = {}
	let id = req.body.houseId || generateId()
	houseJson['list.' + id] = JSON.parse(req.body.houseDetails)
	var fs = require('fs');

	var img = req.body.image
	if (img) {
		var data = img.replace(/^data:image\/\w+;base64,/, "");
		var buf = Buffer.from(data, 'base64');
		fs.writeFile(id + '.png', buf, (err, res) => {
			// console.log(err)
			// console.log(res)
			var bucket = firestore.bucket.upload(id + '.png', {
					metadata: {
						contentType: req.body.imageType
					}
				}
			).then(() => {
				fs.unlinkSync(id + '.png')
			})
		});
	}
	// console.log(req.body.image)
	// console.log(image.type)
	await groupDoc.ref.update(houseJson)
	res.send()
}

const generateId = () => {
	const { v4: uuidv4 } = require('uuid');
	return uuidv4();
}