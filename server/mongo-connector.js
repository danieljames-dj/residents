const {MongoClient} = require('mongodb');
const {MONGO_URI} = require('./config');

module.exports.db = {}

module.exports.connect = async() => {
	const client = await MongoClient.connect(MONGO_URI, {
        useUnifiedTopology: true
    });
    console.log("MongoDB Connected")
	const db = client.db();
	Object.assign(module.exports.db, {
        users: db.collection('users'),
        groups: db.collection('groups')
    })
}