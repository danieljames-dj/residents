module.exports = async function(req, res, firestore) {
	bucket = firestore.bucket
	console.log(req.body.fileName)
	blob = await bucket.file(req.body.fileName).download({
		destination: req.body.fileName
	})
	res.download(req.body.fileName, req.body.fileName, (err) => {
		if (err) {
			res.status(500).send({
				message: "Could not download the file. " + err,
			});
		}
	})
}