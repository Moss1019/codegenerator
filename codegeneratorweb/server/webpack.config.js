
var path = require('path')

module.exports = {
    mode: 'production',
    entry: './app.js',
    output: {
        filename: 'app.js',
        path: path.resolve(__dirname, 'public')
    },
    target: 'node',
    node: {
        __dirname: false,
        __filename: false
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader'
                }
            }
        ]
    }
}