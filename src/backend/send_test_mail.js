let nodemailer = require('nodemailer');
require('dotenv').config();

let mailTransporter = nodemailer.createTransport(
    {
        service: 'gmail',
        auth: {
            user: process.env.GMAIL_ACCOUNT,
            pass: process.env.GMAIL_PASSWORD
        }
    }
);

let mailDetails = {
    from: process.env.GMAIL_ACCOUNT,
    to: process.env.FAKE_EMAIL_DELETEME, // TODO: when deleting this code, delete this line in .env
    subject: "If you're reading this it WORKED!",
    text: "And I am so proud of my child the auto email maker, even though I did so little work."
};

mailTransporter.sendMail(
    mailDetails,
    function (err, data) {
        if (err) {
            console.error(err);
        } else {
            console.log('Email sent successfully');
        }
    }
);
