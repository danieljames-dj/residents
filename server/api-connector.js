const fs = require('fs');
const path = require('path');
const admin = require('firebase-admin');
const serviceAccount = require("./secrets/teamcontacts-251c5-firebase-adminsdk-mit2g-dc54552b20.json");

admin.initializeApp({
	credential: admin.credential.cert(serviceAccount),
	databaseURL: "https://teamcontacts-251c5.firebaseio.com"
});

const verifyToken = (token, req, res, db, controller) => {
	admin.auth().verifyIdToken(token).then((decodedToken) => {
		const uid = decodedToken.uid;
		controller(req, res, db, uid);
	}).catch((error) => {
		res.status(401).send('Invalid token');
	})
	// controller(req, res, db, "CE1VSnw0FMW8P5NIDJFaGvtmCYE2");
}

module.exports.connect = function(app, mongo) {
	const apiList = JSON.parse(fs.readFileSync(path.join(__dirname, 'apiList.json'), 'utf8'))
	const apiPrefix = '/api'
	const keys = Object.keys(apiList)
	for (var i = 0; i < keys.length; i++) {
		const apiSubList = apiList[keys[i]]
		for (var j = 0; j < apiSubList.length; j++) {
			const controller = require(apiSubList[j].path)
			if (apiSubList[j].method == "GET") {
				app.get(apiPrefix + (keys[i] != "" ? "/" + keys[i] : "") + apiSubList[j].route, (req, res) => {
					verifyToken(req.headers.authorization, req, res, mongo.db, controller);
				})
			} else if (apiSubList[j].method == "POST") {
				app.post(apiPrefix + (keys[i] != "" ? "/" + keys[i] : "") + apiSubList[j].route, (req, res) => {
					verifyToken(req.headers.authorization, req, res, mongo.db, controller);
				})
			}
		}
	}
}