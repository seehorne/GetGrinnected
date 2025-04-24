const nodemailer = require('nodemailer');
const dotenv = require('dotenv');

/**
 * Create a nodemailer transporter for the configured gmail account.\
 * 
 * @returns a `nodemailer.Transporter` with its settings configured.
 */
function makeMailer() {
    let mailTransporter = nodemailer.createTransport(
        {
            service: 'gmail',
            auth: {
                user: process.env.GMAIL_ACCOUNT,
                pass: process.env.GMAIL_PASSWORD
            }
        }
    );
    return mailTransporter;
}

/**
 * Generate a one-time code randomly.
 * 
 * @param {int} length  Length of the code to generate.
 * @returns  String containing the generated code
 */
function genCode(length) {
    var code = '';
    for (var i = 0; i < length; i++) {
        const n = Math.floor(Math.random() * 10);
        code += n.toString();
    }
    return code;
}

/**
 * Send a one-time code to an email address.
 * 
 * @param {string} email Email to send.
 * @returns  The code that was sent.
 */
function sendCode(email) {
    // Get username and password from .env
    dotenv.config();
    const account = process.env.GMAIL_ACCOUNT;
    const password = process.env.GMAIL_PASSWORD;

    // Construct the message we want to send
    const code = genCode(6);
    let mailDetails = {
        from: account,
        to: email,
        subject: "Your GetGrinnected One Time Code",
        text: `Your one-time code is ${code}.`
    };
    
    // Use nodemailer to send that through our gmail account
    let mailer = makeMailer(account, password);    
    mailer.sendMail(
        mailDetails,
        function (err, _data) {
            if (err) {
                console.error(err)
                throw err;
            } else {
                console.log(`sent OTP to ${email}`);
            }
        }
    );

    return code;
}

if (require.main !== module) {
    module.exports = {
        sendCode
    }
}