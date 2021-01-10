const express = require('express')
const path = require('path')
const {PORT, SESSION_SECRET, PRODUCTION, BUILD_PATH} = require('./config');
const bodyParser = require('body-parser');

const initialize = async() => {
    const app = express()
    // app.use(bodyParser.urlencoded({limit: '50mb', extended: false, parameterLimit: 10000}));
    app.use(bodyParser.json({limit: '50mb'}));
    app.use(bodyParser.urlencoded({extended: true}) );

    if (!PRODUCTION) {
        const cors = require('cors')
        app.use(cors({origin: '*'}))
    }

    require('./api-connector').connect(app)

    const buildPath = path.join(__dirname, "../client/dist/client")
    app.use(express.static(buildPath));
    app.get('/*', (req, res) => {
        res.sendFile(path.join(buildPath, 'index.html'));
    });

    app.listen(PORT, () => {
        console.log(`App listening to port %s`, PORT)
    })

}

initialize().catch(error => {
    console.error(error);
});