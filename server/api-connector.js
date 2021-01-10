const fs = require('fs');
const path = require('path');
const admin = require('firebase-admin');
const serviceAccount = require("./secrets/teamcontacts-251c5-firebase-adminsdk-mit2g-dc54552b20.json");

admin.initializeApp({
	credential: admin.credential.cert(serviceAccount),
	databaseURL: "https://teamcontacts-251c5.firebaseio.com",
	storageBucket: "gs://teamcontacts-251c5.appspot.com"
});

const firestore = {
	groups: admin.firestore().collection('groups'),
	users: admin.firestore().collection('users'),
	FieldValue: admin.firestore.FieldValue,
	bucket: admin.storage().bucket()
}



// console.log(admin.storage().ref)

// bucket = admin.storage().bucket()
// blob = bucket.file('61be4066-322e-473f-a942-e288beb80cf7.png').download({
// 	destination: 'test.png'
// })
// bucket.upload("test.test", {
//     destination: "pictest",
//     metadata: {
//         contentType: "image/png",
//         cacheControl: 'public, max-age=31536000'
//     }
// })
// const blobWriter = blob.createWriteStream({
// 	metadata: {
// 		contentType: "req.file.mimetype"
// 	}
// })

// blobWriter.on('error', (err) => {
// 	console.log(err)
// })

// blobWriter.on('finish', () => {
// 	res.status(200).send("File uploaded.")
// })

// blobWriter.end("\0")
// console.log(admin.storage().bucket())
// console.log(admin.storage().bucket("gs://teamcontacts-251c5.appspot.com"))

const verifyToken = (token, req, res, controller) => {
	admin.auth().verifyIdToken(token).then((decodedToken) => {
		const uid = decodedToken.uid;
		controller(req, res, firestore, uid);
	}).catch((error) => {
		console.log(error)
		res.status(401).send('Invalid token');
	})
	// controller(req, res, firestore, "CE1VSnw0FMW8P5NIDJFaGvtmCYE2");
}

module.exports.connect = function(app) {
	const apiList = JSON.parse(fs.readFileSync(path.join(__dirname, 'apiList.json'), 'utf8'))
	const apiPrefix = '/api'
	const keys = Object.keys(apiList)
	for (var i = 0; i < keys.length; i++) {
		const apiSubList = apiList[keys[i]]
		for (var j = 0; j < apiSubList.length; j++) {
			const controller = require(apiSubList[j].path)
			if (apiSubList[j].method == "GET") {
				app.get(apiPrefix + (keys[i] != "" ? "/" + keys[i] : "") + apiSubList[j].route, (req, res) => {
					verifyToken(req.headers.authorization, req, res, controller);
				})
			} else if (apiSubList[j].method == "POST") {
				app.post(apiPrefix + (keys[i] != "" ? "/" + keys[i] : "") + apiSubList[j].route, (req, res) => {
					verifyToken(req.headers.authorization, req, res, controller);
				})
			}
		}
	}
}