package ntnu.idatt2105.mailer

class Mail(
    val from: String,
    val to: String,
    val subject: String,
    val htmlTemplate: HtmlTemplate
)
