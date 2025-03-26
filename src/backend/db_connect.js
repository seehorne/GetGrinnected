const mysql = require('mysql2');

// The information in here is purposefully left empty on our github since it is a public repository. 
// (See file on remote application server node)
// Additional note connection is only established from the remote application server node.
const connection = mysql.createConnection({
    hostname: '',
    user: '',
    password: '',
    database: 'GetGrinnected'
});

connection.connect((err) => {
    if(err){
        console.error('Error could not connect to the database: ', err);
        return;
    }      
    console.log('Connected to database successfully!');
});