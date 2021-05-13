package ntnu.idatt2105.mailer

import ntnu.idatt2105.mailer.HtmlTemplate

class Mail(
    val from: String,
    val to: String,
    val subject: String,
    val htmlTemplate: HtmlTemplate
)
