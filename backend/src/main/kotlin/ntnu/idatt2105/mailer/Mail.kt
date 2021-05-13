package ntnu.idatt2105.mailer

import ntnu.idatt2105.mailer.HtmlTemplate

class Mail(
    private val from: String,
    private val to: String,
    private val subject: String,
    private val htmlTemplate: HtmlTemplate
)
